package com.lfj.plugin.patb.command.bot;

import com.lfj.plugin.patb.bootmanager.BootManager;
import com.lfj.plugin.patb.bootmanager.loader.BotsLoader;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

public class Load {
    private Load(){  }
    public static LiteralArgumentBuilder<CommandSourceStack> load(Path path, BootManager bootManager){
        SuggestionProvider<CommandSourceStack> suggestionLoad = (context, builder) ->{
            List<File> files = List.of(BotsLoader.BotsFiles.bots(path).orElse(new File[] {}));
            if(!files.isEmpty()){
                for(File file : files)
                    if(!bootManager.isLoaded(file)) builder.suggest(file.getName());
            }
            return builder.buildFuture();
        };
        return Commands.literal("load").then(Commands.argument("loadArg", StringArgumentType.string())
                .suggests(suggestionLoad)
                .executes(ctx ->{
                    List<File> files = List.of(BotsLoader.BotsFiles.bots(path).orElse(new File[]{}));
                    String fileName = StringArgumentType.getString(ctx, "loadArg");
                    File file = files.stream()
                            .filter(e -> e.getName().equals(fileName))
                            .findFirst()
                            .orElse(null);
                    bootManager.loadBot(file);
                    return Command.SINGLE_SUCCESS;
                })
        );
    }
}
