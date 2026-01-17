package com.lfj.plugin.api.bots.telegram;

import com.lfj.plugin.api.bots.BotPlugin;

import com.lfj.plugin.api.bots.MetadataBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;

import java.io.File;
import java.util.logging.Logger;

import static com.lfj.plugin.api.bots.token.Read.readToken;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.longpolling.BotSession;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;

public abstract class TelegramBotPlugin implements LongPollingSingleThreadUpdateConsumer, BotPlugin {
    // Common field
    private Logger logger;
    private MetadataBot data;
    private File directory;
    private boolean isLoaded = false;
    private String hash;
    // Specific field
    protected BotSession botSession;
    protected String token;
    protected TelegramBotsLongPollingApplication application;
    protected TelegramClient telegramClient;

    // Load
    @Override
    public void onLoad(Logger logger, File directory, MetadataBot data){
        if(isLoaded) return;
        if(directory == null) throw new IllegalArgumentException("Argument 'directory' is null!");
        if(data == null) throw new IllegalArgumentException("Argument 'data' is null!");
        this.logger = Logger.getLogger(String.format("TelegramBot-PL-YAI|%s", data.name()));
        if(logger != null) this.logger = Logger.getLogger(String.format("%s|%s", logger.getName(), data.name()));
        this.directory = directory; this.data = data;
        if(!this.directory.exists()) this.directory.mkdirs();
        logger.info(String.format("Bot '%s' info:", data.name()));
        logger.info(
                String.format(
                        """
                        {
                            "bot_name": "%s",
                            "author": "%s",
                            "bot_type": "%s",
                            "version":, "%s",
                            "main_class": "%s"
                        }
                        """, data.name(), data.author(), data.botType(), data.version(), data.mainClass()
                )
        );
        if(botInit()) {
            onEnable();
            isLoaded = true;
        }else{
            logger.warning("Failed init bot '" + data.name() + "' onLoad method.");
        }
    }

    private boolean botInit(){
        logger.info( "Bot '" + data.name() + "' init...");
        this.application = new TelegramBotsLongPollingApplication();
        this.token = readToken(this.directory);
        this.telegramClient = new OkHttpTelegramClient(token);
        return botStart();
    }

    private boolean botStart(){
        logger.info("Bot ' " + this.data.name() + " 'starting...");
        try{
            if(token != null && !token.equals("TOKEN")){
                this.botSession = this.application.registerBot(this.token, this);
                logger.info("Bot '" + this.data.name() + "' started!");
                return true;
            }
            return false;
        }catch (TelegramApiException e){
            logger.severe(e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Unload
    @Override
    public void onUnload(){
        onDisable();
        logger.info("Bot '" + data.name() + "' stoping...");
        try {
            if(this.botSession != null && this.botSession.isRunning()){
                logger.info("Close bot session...");
                this.botSession.stop();
                this.botSession.close();
                this.botSession = null;
            }
            logger.info("Close bot application...");
            this.application.unregisterBot(token);
            this.application.stop();
            this.application.close();
        }catch (TelegramApiException e){
            logger.warning("Warning close session or application. Exception >> " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            logger.warning("Warning close session or application. Exception >> " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public MetadataBot getMetaData(){return data;}
    @Override
    public Logger getLogger(){return this.logger;}
    @Override
    public File getDataFolder(){return directory;}

    public abstract void onEnable();
    public abstract void onDisable();
}
