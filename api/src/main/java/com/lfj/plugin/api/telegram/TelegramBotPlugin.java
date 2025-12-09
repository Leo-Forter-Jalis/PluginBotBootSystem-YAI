package com.lfj.plugin.api.telegram;

import com.lfj.plugin.api.BotPlugin;

import com.lfj.plugin.api.MetaData;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org. bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Level;

import static com.lfj.plugin.api.token.Read.readToken;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.longpolling.BotSession;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;

public abstract class TelegramBotPlugin implements LongPollingSingleThreadUpdateConsumer, BotPlugin {
    // Common field
    private JavaPlugin plugin;
    private MetaData data;
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
    public void onLoad(JavaPlugin plugin, File directory, MetaData data, String hash){
        if(isLoaded) return;
        if(plugin == null) throw new IllegalArgumentException("Argument 'plugin' is null!");
        if(directory == null) throw new IllegalArgumentException("Argument 'directory' is null!");
        if(data == null) throw new IllegalArgumentException("Argument 'data' is null!");
        this.plugin = plugin; this.directory = directory; this.data = data;
        if(!this.directory.exists()) this.directory.mkdirs();
        plugin.getLogger().info("Bot '" + data.getName() + "' info:");
        plugin.getLogger().info("\n{\n" + "\t\"Name\": \"" + data.getName() + "\",\n\t\"Author\": \"" + data.getAuthor() + "\",\n\t\"Type\": \"Telegram Bot" + "\",\n\t\"Version\": \"" + data.getMajorVersion() + "." + data.getMinorVersion() + "\",\n\t\"Main-Class\": \"" + data.getMainClass() + "\"\n}");
        if(botInit()) {
            onEnable();
            isLoaded = true;
        }else{
            this.plugin.getLogger().warning("Failed init bot '" + data.getName() + "' onLoad method.");
        }
    }

    private boolean botInit(){
        this.plugin.getLogger().info( "Bot '" + data.getName() + "' init...");
        this.application = new TelegramBotsLongPollingApplication();
        this.token = readToken(this.directory);
        this.telegramClient = new OkHttpTelegramClient(token);
        return botStart();
    }

    private boolean botStart(){
        this.plugin.getLogger().info("Bot ' " + this.data.getName() + " 'starting...");
        try{
            if(token != null && !token.equals("TOKEN")){
                this.botSession = this.application.registerBot(this.token, this);
                this.plugin.getLogger().info("Bot '" + this.data.getName() + "' started!");
                return true;
            }
            return false;
        }catch (TelegramApiException e){
            e.printStackTrace();
            return false;
        }
    }

    // Unload
    private void onUnload(){
        onDisable();
        this.plugin.getLogger().info("Bot '" + data.getName() + "' stoping...");
        try {
            if(this.botSession != null && this.botSession.isRunning()){
                this.plugin.getLogger().info("Close bot session...");
                this.botSession.stop();
                this.botSession.close();
                this.botSession = null;
            }
            this.plugin.getLogger().info("Close bot application...");
            this.application.unregisterBot(token);
            this.application.stop();
            this.application.close();
        }catch (TelegramApiException e){
            this.plugin.getLogger().warning("Warning close session or application. Exception >> " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            this.plugin.getLogger().warning("Warning close session or application. Exception >> " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public MetaData getMetaData(){return data;}
    @Override
    public JavaPlugin getPlugin(){return this.plugin;}
    @Override
    public File getDataFolder(){return directory;}

    public abstract void onEnable();
    public abstract void onDisable();
}
