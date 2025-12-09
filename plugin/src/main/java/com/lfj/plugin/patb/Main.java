package com.lfj.plugin.patb;

import com.lfj.plugin.patb.botmanager.BotBootManager;
import org.bukkit.plugin.java.JavaPlugin;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;

public final class Main extends JavaPlugin {
    private BotBootManager botBootManager;
    @Override
    public void onEnable() {
        if(!getDataFolder().exists())
            getDataFolder().mkdirs();
        getServer().getPluginManager();
        botBootManager = new BotBootManager(getDataFolder(), this);
        botBootManager.load();
        botBootManager.run();
        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, command ->{
            command.registrar().register(LoadCommand.loadCommand(botBootManager, this.getDataFolder()));
            command.registrar().register(UnloadCommand.unloadCommand(botBootManager));
        });
    }

    @Override
    public void onDisable() {
        botBootManager.unload();
    }
}
