package com.lfj.plugin.api.dependencies;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public abstract class AbstractDependencies implements Dependency {
    private JavaPlugin javaPlugin;
    private Logger logger;
    private MetadataDep metadata;
    private boolean isEnabled = false;

    private void load(JavaPlugin javaPlugin, MetadataDep metaData) {
        this.javaPlugin = javaPlugin;
        this.metadata = metaData;
        this.logger = this.javaPlugin.getLogger();
        this.isEnabled = true;
    }

    private void unload() {
        isEnabled = false;
        this.logger = null;
        this.metadata = null;
        this.javaPlugin = null;
        System.gc();
    }

    @Override
    abstract public void mainDep();

    @Override
    public void init(JavaPlugin javaPlugin, MetadataDep metadata) { load(javaPlugin, metadata); }

    @Override
    public void shutdown() { unload(); }

    @Override
    public boolean isEnabled() { return isEnabled; }

    protected JavaPlugin getJavaPlugin() { return this.javaPlugin; }
    protected Logger getLogger() { return this.logger; }
    protected MetadataDep getMetadata() { return this.metadata; }
}
