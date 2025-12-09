package com.lfj.plugin.patb.botmanager;

import com.lfj.plugin.patb.botmanager.load.ThreadManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Logger;

public class BotBootManager {
    private boolean loading = false;
    private File directory;
    private NewLoad newLoad;
    private Run run;
    private Unload unload;
    private Logger logger;
    public BotBootManager(File directory, JavaPlugin plugin){
        this.logger = plugin.getLogger();
        this.logger.info("BotBootManager init...");
        ThreadManager threadManager = new ThreadManager();
        this.directory = new File(directory, "bots");
        if(!Init.init(this.directory)) {
            return;
        }
        this.newLoad = new NewLoad(threadManager, this.directory, plugin);
        this.run = new Run(threadManager);
        this.unload = new Unload(threadManager);
    }
    public void load(){
        this.logger.info("Loading bots...");
        if(this.loading) {
            this.logger.warning("Load stadia stopped. Bots is loaded.");
            return;
        }
        this.newLoad.load();
        this.loading = true;
    }
    public void load(File file){
        this.logger.info("Loading '" + file.getName() + "' ...");
    }
    public void run(){
        this.logger.info("Running bots...");
        this.run.run();
    }
    public void run(String botName){
        this.logger.info("Running '" + botName + "'...");
        this.run.run(botName);
    }
    public void unload(){
        this.logger.info("Unloaded bots...");
        if(!this.loading) {
            this.logger.warning("Unload statia stopped. Bot is unloaded.");
            return;
        }
        this.unload.unload();
        this.loading = false;
    }
    public void unload(File file){
        this.logger.info("Unloaded '" + file.getName() + "' bot...");
        this.unload.unload(file);
    }
}
