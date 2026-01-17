package com.test;

import com.lfj.plugin.api.bots.telegram.TelegramBotPlugin;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class Main extends TelegramBotPlugin {
    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    @Override
    public void consume(Update update) {
        if(update.hasMessage()){
            if(update.getMessage().hasText()){
                try {
                    Handle.send(update.getMessage().getText(), update.getMessage().getChatId(), telegramClient);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
