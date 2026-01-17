package com.lfj.plugin.patb.command;

import com.lfj.plugin.patb.bootmanager.BootManager;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class PluginCommand {
    private PluginCommand(){  }
    public static LiteralArgumentBuilder<CommandSourceStack> command(BootManager bootManager){
        return Commands.literal("tgplyai")
                .requires(ctx ->{
                    if(ctx.getSender() instanceof Player player && player.isOp()) return true;
                    if(ctx.getSender() instanceof ConsoleCommandSender) return true;
                    return false;
                }).then(BotCommand.bot(bootManager.getPathToBots(), bootManager))
                .then(DependCommand.depend(bootManager));
    }
}
