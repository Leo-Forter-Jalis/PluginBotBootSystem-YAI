package com.lfj.plugin.patb;

import com.lfj.plugin.patb.bootmanager.BootManager;
import org.bukkit.plugin.java.JavaPlugin;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import static com.lfj.plugin.patb.command.PluginCommand.command;

public final class Main extends JavaPlugin {
    private BootManager bootManager;
    @Override
    public void onEnable() {
        if(!getDataFolder().exists()) getDataFolder().mkdirs();
        this.bootManager = new BootManager(this);
        this.bootManager.allLoad();
        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, command ->{
            command.registrar().register(command(bootManager).build());
        });
    }

    @Override
    public void onDisable() {
        bootManager.unloadAll();
    }
}
