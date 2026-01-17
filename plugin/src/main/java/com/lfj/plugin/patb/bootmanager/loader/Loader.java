package com.lfj.plugin.patb.bootmanager.loader;

import com.lfj.plugin.patb.bootmanager.component.ExpansionHolder;
import com.lfj.plugin.patb.bootmanager.component.ThreadManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.nio.file.Path;
import java.util.logging.Logger;

public class Loader {
    private Path pathToBots;
    private Path pathToDependencies;
    private BotsLoader botsLoader;
    private DependenciesLoader dependLoader;
    private Loader(){}
    public Loader(Path pathToBots, JavaPlugin plugin, Path pathToDependencies, Logger logger, ExpansionHolder expansionHolder, ThreadManager threadManager){
        this.pathToBots = pathToBots;
        this.pathToDependencies = pathToDependencies;
        this.botsLoader = new BotsLoader(Logger.getLogger(String.format("%s|%s", logger.getName(), "BotsLoader")), pathToBots, threadManager, expansionHolder);
        this.dependLoader = new DependenciesLoader(plugin, pathToDependencies, logger, threadManager, expansionHolder);
    }
    public void allLoad(){
        loadDepends();
        loadBots();
    }
    public void loadBots(){ this.botsLoader.loadBots(); }
    public void loadBot(File bot){ this.botsLoader.loadBot(bot); }
    public void loadDepends(){ this.dependLoader.loadDependencies(); }
    public void loadDepend(File depend){ this.dependLoader.loadDependency(depend); }
}
