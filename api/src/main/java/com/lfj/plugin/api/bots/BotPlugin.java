package com.lfj.plugin.api.bots;

import com.lfj.plugin.api.Expansion;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;
import java.util.logging.Logger;

public interface BotPlugin extends Expansion {
    void onLoad(Logger logger, File directory, MetadataBot data);
    void onUnload();
    void onEnable();
    void onDisable();
    MetadataBot getMetaData();

    Logger getLogger();

    File getDataFolder();
}
