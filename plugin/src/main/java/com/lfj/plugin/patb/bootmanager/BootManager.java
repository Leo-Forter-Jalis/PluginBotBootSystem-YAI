package com.lfj.plugin.patb.bootmanager;

import com.lfj.plugin.api.bots.BotPlugin;
import com.lfj.plugin.patb.bootmanager.component.ExpansionHolder;
import com.lfj.plugin.patb.bootmanager.component.ThreadManager;
import com.lfj.plugin.patb.bootmanager.loader.Loader;
import com.lfj.plugin.patb.bootmanager.unloader.Unloader;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Logger;

public class BootManager {
    private Path pathToBots;
    private Path pathToDependencies;
    private JavaPlugin javaPlugin;
    private Logger logger;
    private ExpansionHolder expansionHolder;
    private ThreadManager threadManager;
    private Loader loader;
    private Unloader unloader;

    private BootManager(){  }
    public BootManager(JavaPlugin javaPlugin){
        this.javaPlugin = javaPlugin;
        this.logger = javaPlugin.getLogger();
        this.expansionHolder = new ExpansionHolder();
        pathToBots = this.javaPlugin.getDataPath().resolve("bots");
        pathToDependencies = this.javaPlugin.getDataPath().resolve("depends");
        CheckPath.checkPath(pathToBots, pathToDependencies);
        this.threadManager = new ThreadManager(logger);
        this.loader = new Loader(pathToBots, javaPlugin, pathToDependencies, logger, expansionHolder, threadManager);
        this.unloader = new Unloader(this.threadManager, this.expansionHolder);
    }

    public void allLoad(){
        this.loader.allLoad();
    }

    public void loadBot(File bot){
        if(bot == null) return;
        this.loader.loadBot(bot);
    }
    public void loadDepend(File depend){
        if(depend == null){ logger.warning("depend is null"); return; }
        this.loader.loadDepend(depend);
    }
    public void unloadAll(){
        this.unloader.unloadAll();
    }

    public void unload(File botOrDepend){
        if(botOrDepend == null) return;
        if(this.expansionHolder.contains(botOrDepend)) this.unloader.unload(this.expansionHolder.get(botOrDepend.toPath()).orElse(null));
    }
    public boolean isLoaded(File file){
        if(CheckCondition.checkCondition(file, this.expansionHolder) == null) return false;
        return true;
    }

    public Path getPathToBots(){ return this.pathToBots; }
    public Path getPathToDependencies() { return this.pathToDependencies; }

    static class CheckCondition{
        private CheckCondition(){  }
        public static ExpansionHolder.ExpansionItem checkCondition(File file, ExpansionHolder expansionHolder){
            return expansionHolder.getList().stream()
                    .filter(e -> e.expansionPath().equals(file.toPath()))
                    .findFirst()
                    .orElse(null);
        }
    }
    static class CheckPath{
        private CheckPath(){  }
        public static void checkPath(Path pathToBots, Path pathToDependencies){
            try{
                if(!Files.exists(pathToBots)) Files.createDirectories(pathToBots);
                if(!Files.exists(pathToDependencies)) Files.createDirectories(pathToDependencies);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
