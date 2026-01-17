package com.lfj.plugin.patb.command;

import com.lfj.plugin.patb.bootmanager.BootManager;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;

import java.nio.file.Path;

import static com.lfj.plugin.patb.command.bot.Load.load;
import static com.lfj.plugin.patb.command.bot.Unload.unload;


public class BotCommand {
    private BotCommand(){  }
    public static LiteralArgumentBuilder<CommandSourceStack> bot(Path path, BootManager bootManager){
        return Commands.literal("bot").then(load(path, bootManager)).then(unload(path, bootManager));
    }
}
