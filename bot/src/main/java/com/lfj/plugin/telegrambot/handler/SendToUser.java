package com.lfj.plugin.telegrambot.handler;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public class SendToUser implements com.lfj.plugin.api.bots.telegram.SendMessage {
    private TelegramClient client;
    public SendToUser(TelegramClient client){
        this.client = client;
    }

    @Override
    public void send(long chatID, String text){
        SendMessage message = SendMessage
                .builder()
                .chatId(chatID)
                .text(text)
                .build();
        try {
            client.execute(message);
        }catch (TelegramApiException e){e.printStackTrace();}
    }
}
