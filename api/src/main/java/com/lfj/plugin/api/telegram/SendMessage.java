package com.lfj.plugin.api.telegram;

public interface SendMessage {
    void send(long chatID, String text);
}
