package com.lfj.plugin.api.dependencies;

import com.lfj.plugin.api.Expansion;
import org.bukkit.plugin.java.JavaPlugin;

public interface Dependency extends Expansion {
    void mainDep();
    void init(JavaPlugin javaPlugin, MetadataDep metadata);
    void shutdown();
    boolean isEnabled();
}
