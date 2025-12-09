package com.lfj.plugin.patb;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import com.lfj.plugin.patb.botmanager.BotBootManager;

import java.io.File;

public class LoadCommand {
    public static LiteralCommandNode<CommandSourceStack> loadCommand(BotBootManager manager, File directory){
        File botsDirectory = directory.toPath().resolve("bots").toFile();
        SuggestionProvider<CommandSourceStack> suggestion = (context, builder) ->{
            File[] files = botsDirectory.listFiles((dir, name) -> name.endsWith(".jar"));
            if(files != null){
                for(File file : files)
                    builder.suggest(file.getName());
            }
            return builder.buildFuture();
        };
        return Commands.literal("tgplyai")
                .requires(sender ->{
                    if(sender.getSender() instanceof Player player)
                        return player.getName().equals("Leo_Forter_Jalis");
                    if(sender.getSender() instanceof ConsoleCommandSender) return true;
                    return false;
                })
                .then(Commands.literal("load")
                        .then(Commands.argument("jarName", StringArgumentType.string()).executes(ctx -> {
                            String fileName = StringArgumentType.getString(ctx, "jarName");
                            File file = new File(botsDirectory, fileName);
                            manager.load(file);
                            return Command.SINGLE_SUCCESS;
                        }))).build();

    }
}
