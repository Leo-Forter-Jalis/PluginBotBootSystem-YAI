package com.lfj.plugin.api;

import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;

public interface BotPlugin {
    void onLoad(JavaPlugin plugin, File directory, MetaData data, String hash);
    void onEnable();
    void onDisable();
    JavaPlugin getPlugin();
    MetaData getMetaData();
    File getDataFolder();
}
