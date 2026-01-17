package com.lfj.plugin.patb.command;

import com.lfj.plugin.patb.bootmanager.BootManager;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;

import java.nio.file.Path;

import static com.lfj.plugin.patb.command.depend.Load.load;
import static com.lfj.plugin.patb.command.depend.Unload.unload;

public class DependCommand {
    private DependCommand(){  }
    public static LiteralArgumentBuilder<CommandSourceStack> depend(BootManager bootManager){
        return Commands.literal("depend").then(load(bootManager.getPathToDependencies(), bootManager)).then(unload(bootManager.getPathToDependencies(), bootManager));
    }
}
