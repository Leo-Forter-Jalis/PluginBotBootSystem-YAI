package com.lfj.plugin.api.bots.telegram;

public interface SendMessage {
    void send(long chatID, String text);
}
