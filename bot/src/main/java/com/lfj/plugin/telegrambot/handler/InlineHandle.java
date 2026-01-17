package com.lfj.plugin.telegrambot.handler;

import org.bukkit.plugin.java.JavaPlugin;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.inlinequery.inputmessagecontent.InputTextMessageContent;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import org.telegram.telegrambots.meta.api.objects.inlinequery.InlineQuery;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResult;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResultArticle;
import org.telegram.telegrambots.meta.api.methods.AnswerInlineQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class InlineHandle {
    public static void inlineHandle(Update update, TelegramClient client, Logger logger){
        InlineQuery inlineQuery = update.getInlineQuery();
        User user = inlineQuery.getFrom();

        List<InlineQueryResult> results = new ArrayList<>();

        InputTextMessageContent content = InputTextMessageContent
                .builder()
                .messageText("Доброго дня, " + user.getFirstName() + ".\nВы можете получить инструкции как подключиться на сервер, написав <a href=\"https://t.me/ahfahfkahalkwndlkand_bot\">мне в лс </a>/start. \n\nДАННАЯ ФУНКЦИЯ В БЕТЕ!")
                .parseMode("HTML")
                .build();

        InlineQueryResultArticle article = InlineQueryResultArticle
                .builder()
                .id("1")
                .title("Присоединиться к серверу")
                .inputMessageContent(content)
                .build();

        results.add(article);
        AnswerInlineQuery answer = AnswerInlineQuery
                .builder()
                .inlineQueryId(inlineQuery.getId())
                .results(results)
                .cacheTime(0)
                .build();
        try {
            client.execute(answer);
            logger.info("userID >> " + user.getId());
        } catch (TelegramApiException e){ e.printStackTrace(); }

    }
}
