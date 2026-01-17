package com.lfj.plugin.telegrambot.handler;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.OfflinePlayer;

import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import org.telegram.telegrambots.meta.api.objects.Update;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import static com.lfj.plugin.telegrambot.algorithm.Enumeration.enumerationElements;

import com.lfj.datacontroller.DataController;
import com.lfj.datacontroller.PlayerData;

import java.util.logging.Logger;

public class Handle {

    public void handle(String command, String[] arguments, TelegramClient client, Update update, Logger logger){
        //if(command.equals("/add_me"))
            //handleAddMeCommand(arguments, client, update, plugin);
        /*else*/ if(command.equals("/start") || command.equals("/help"))
            handleStartOrHelp(client, update, logger);
    }
    /*
    private  void handleAddMeCommand(String[] args, TelegramClient client, Update update, JavaPlugin plugin){
        User user = update.getMessage().getFrom();
        DataController controller = plugin.getServer().getServicesManager().load(DataController.class);
        if(controller.contains(user.getId())){
            sendMessageToUser(client, user, "Вы уже есть в списке!", plugin);
            return;
        }
        PlayerData data = new PlayerData();
        plugin.getServer().getScheduler().runTask(plugin, ()->{
            OfflinePlayer player = plugin.getServer().getOfflinePlayer(args[1]);
            player.setWhitelisted(true);
            if(!player.isWhitelisted()){
                plugin.getLogger().info("Add new player call error.");
                sendMessageToUser(client, user, "В процессе добавления в список, произошла ошибка!", plugin);
                return;
            }
            plugin.getLogger().info("Player " + args[1] + " added to whitelist.");
            sendMessageToUser(client, user, "Вы были успешно добавлены в список!\n\nIp >> e2.aurorix.net:25352\n\nПриятной игры! :)", plugin);
        });
        enumerationElements(args, data, user, plugin);
        controller.add(args[1], data);
    }*/
    /*
    private  void putData(String[] args, PlayerData data, User user){
        for(int i = 2; i < args.length; i++){
            if(args[i].startsWith("true") || args[i].startsWith("false")){
                if(args[i].startsWith("true")) data.setSendCodeVerification(true);
                data.setSendCodeVerification(false);
            } else if(args[i].startsWith("#")){
                data.setHexCode(args[i]);
            } else if(args[i].startsWith("D") || args[i].startsWith("d")){
                data.setUserName(args[i]);
            }
        }
        data.setUserId(user.getId());
        data.setUuid(null);
    }
    */
    public  void handleStartOrHelp(TelegramClient client, Update update, Logger logger){
        String text = update.getMessage().getText();
        if(text.equals("/start"))
            sendMessageToUser(client, update.getMessage().getFrom(), "Доброго времени суток!\n" +
                    "Данный бот добавит вас в список сервера и вы сможете зайти, но чтобы это сделать, нужно добавить себя в список. Как? Все очень просто:\n\n" +
                    "/add_me [NickName] (dDisplayName) (true/false) (#hexcode)" +
                    "Что значат все эти аргументы?\n" +
                    "[NickName] - Ваш ник, под которым вы зайдете на сервер, он всегда идет первым после команды.\n" +
                    "(dDisplayName) - Ваше отображаемое имя. Можно ввести любое имя, но для его добавления нужно добавить перед вашим именем d. Если вы не укажите параметр, то будет взято имя из вашего аккаунта.\n" +
                    "(true/false) - Параметр нужен для того, чтобы в будущем разработчик сервера мог вам отправлять код подтверждения. Сейчас это просто балванка.\n" +
                    "(#hexcode) - Цвет вашего отображаемого имени. Формат HEX.\n\n" +
                    "[] - обязательный параметр.\n" +
                    "() - не обязательный параметр\n\n" +
                    "Пример:\n" +
                    "№1 /add_me Example dExample true #FFFFFF\n" +
                    "№2 /add_me Example true #f5b618\n" +
                    "№3 /add_me Example", logger);
        else if(text.equals("/help"))
            sendMessageToUser(client, update.getMessage().getFrom(), "Доброго времени суток!\n" +
                    "Данный бот добавит вас в список сервера и вы сможете зайти, но чтобы это сделать, нужно добавить себя в список. Как? Все очень просто:\n\n" +
                    "/add_me [NickName] (dDisplayName) (true/false) (#hexcode)" +
                    "Что значат все эти аргументы?\n" +
                    "[NickName] - Ваш ник, под которым вы зайдете на сервер, он всегда идет первым после команды.\n" +
                    "(dDisplayName) - Ваше отображаемое имя. Можно ввести любое имя, но для его добавления нужно добавить перед вашим именем d. Если вы не укажите параметр, то будет взято имя из вашего аккаунта.\n" +
                    "(true/false) - Параметр нужен для того, чтобы в будущем разработчик сервера мог вам отправлять код подтверждения. Сейчас это просто балванка.\n" +
                    "(#hexcode) - Цвет вашего отображаемого имени. Формат HEX.\n\n" +
                    "[] - обязательный параметр.\n" +
                    "() - не обязательный параметр\n\n" +
                    "Пример:\n" +
                    "№1 /add_me Example dExample true #FFFFFF\n" +
                    "№2 /add_me Example true #f5b618\n" +
                    "№3 /add_me Example", logger);
        else{
            sendMessageToUser(client, update.getMessage().getFrom(), "Введите команду /start или /help для подробностей.", logger);
        }
    }
    private  void sendMessageToUser(TelegramClient client, User user, String text, Logger logger){
        SendMessage message = SendMessage
                .builder()
                .chatId(user.getId())
                .text(text)
                .build();
        try{client.execute(message);}catch (TelegramApiException e){e.printStackTrace();}
        StringBuilder sb = new StringBuilder();
        sb.append("[UserID >> " + user.getId() + "], ")
                .append("[Answer >> " + text + "]");
        logger.info(sb.toString());
    }
}
