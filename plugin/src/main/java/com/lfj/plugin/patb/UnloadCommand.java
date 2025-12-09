package com.lfj.plugin.patb;

import com.lfj.plugin.patb.botmanager.load.JarFileList;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;

import com.lfj.plugin.patb.botmanager.BotBootManager;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.List;

public class UnloadCommand {
    public static LiteralCommandNode<CommandSourceStack> unloadCommand(BotBootManager manager){
        List<File> files = JarFileList.getCopyFileList();
        SuggestionProvider<CommandSourceStack> suggestion = (context, builder) ->{
            if(!files.isEmpty()){
                for(File file : files)
                    builder.suggest(file.getName());
            }
            return builder.buildFuture();
        };
        return Commands.literal("tgplyai")
                .requires(sender ->{
                    if(sender.getSender() instanceof Player player)
                        return player.getName().equals("Leo_Forter_Jalis");
                    return false;
                })
                .then(Commands.literal("unload")
                        .then(Commands
                                .argument("jarName", StringArgumentType.string())
                                .suggests(suggestion)).executes(ctx -> {
                                    String fileName = StringArgumentType.getString(ctx, "jarName");
                                    File file = files.stream()
                                            .filter(a -> a.getName().equals(fileName))
                                            .findFirst().orElse(new File("bot", fileName));
                                    if(!file.exists()) return 0;
                                    manager.unload(file);
                                    return Command.SINGLE_SUCCESS;
                        })).build();
    }
}
