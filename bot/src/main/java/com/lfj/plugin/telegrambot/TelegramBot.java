package com.lfj.plugin.telegrambot;

import com.lfj.plugin.api.bots.telegram.TelegramBotPlugin;

import com.lfj.plugin.telegrambot.handler.InlineHandle;
import org.telegram.telegrambots.meta.api.objects.Update;

import com.lfj.plugin.telegrambot.handler.Handle;

public class TelegramBot extends TelegramBotPlugin {
    private Handle handle;
    @Override
    public void onEnable(){
        //getPlugin().getLogger().info("Starting bots...");
        //this.application = new TelegramBotsLongPollingApplication();
        //this.token = readToken(getPlugin().getDataFolder());
        //this.client = super.getTelegramClient();
        //startBot();
        this.handle = new Handle();
    }

    @Override
    public void onDisable(){

    }

    @Override
    public void consume(Update update) {
        getLogger().info("join in consume");
        if(update.hasMessage()){
            getLogger().info("receive message");
            if(update.getMessage().hasText()){
                String text = update.getMessage().getText();
                if(text.startsWith("/start") || text.startsWith("/help")){
                    handle.handleStartOrHelp(telegramClient, update, getLogger());
                } else if (text.startsWith("/add_me")) {
                    String command = text.substring(0, text.indexOf(' '));
                    String a = text.substring(command.length()+1);
                    String[] args = a.split(" ");
                    getLogger().info("call Handle.handle");
                    handle.handle(command, args, telegramClient, update, getLogger());
                }
            }
        } else if (update.hasInlineQuery()) {
            InlineHandle.inlineHandle(update, telegramClient, getLogger());
        }
    }
}
