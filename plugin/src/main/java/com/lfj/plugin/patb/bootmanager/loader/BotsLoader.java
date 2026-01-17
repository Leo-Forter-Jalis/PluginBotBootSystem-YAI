package com.lfj.plugin.patb.bootmanager.loader;

import com.lfj.plugin.api.bots.BotPlugin;
import com.lfj.plugin.api.bots.MetadataBot;
import com.lfj.plugin.api.dependencies.MetadataDep;
import com.lfj.plugin.patb.bootmanager.ExpansionType;
import com.lfj.plugin.patb.bootmanager.MetadataType;
import com.lfj.plugin.patb.bootmanager.component.ExpansionHolder;
import com.lfj.plugin.patb.bootmanager.component.ExpansionHolder.ExpansionItem;
import com.lfj.plugin.patb.bootmanager.component.InteractiveReflection;
import com.lfj.plugin.patb.bootmanager.component.ThreadManager;
import com.lfj.plugin.patb.bootmanager.loader.loadcomponent.InitMainClass;
import com.lfj.plugin.patb.bootmanager.loader.loadcomponent.MetadataComponent;
import org.apache.logging.log4j.LogManager;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Future;
import java.util.logging.Logger;

public class BotsLoader {
    private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(BotsLoader.class);
    private Logger logger;
    private Path pathToBots;
    private boolean globalRunning = false;
    private ThreadManager threadManager;
    private ExpansionHolder expansionHolder;
    private BotsLoader(){
    }
    public BotsLoader(Logger logger, Path pathToBots, ThreadManager threadManager, ExpansionHolder expansionHolder){
        this.logger = logger;
        this.pathToBots = pathToBots;
        this.threadManager = threadManager;
        this.expansionHolder = expansionHolder;
    }
    public void loadBots(){
        if(globalRunning) return;
        File[] files = BotsFiles.bots(pathToBots).orElse(new File[]{});
        for(File file : files)
            loadBot(file);
        globalRunning = true;
    }
    public void loadBot(File botJar){
        if(!List.of(BotsFiles.bots(pathToBots).orElse(new File[]{})).contains(botJar)) return;
        if(this.expansionHolder.contains(botJar)) return;
        URLClassLoader classLoader = null;
        try{
            logger.info(String.format("Start load '%s'", botJar.getName()));
            classLoader = new URLClassLoader(new URL[]{botJar.toURI().toURL()}, this.getClass().getClassLoader());
            if(!(MetadataComponent.loadMetadata(classLoader, "bot.json", MetadataType.BOT).orElse(new MetadataDep("", "", "", "")) instanceof MetadataBot metaData)){
                logger.severe(String.format("Failed initialize bot '%s'. Metadata is null or dependencies", botJar.getName()));
                classLoader.close();
                return;
            }
            logger.info(String.format("Name >> '%s', Author >> '%s', Type >> '%s', Version >> '%s', Main >> '%s'", metaData.name(), metaData.author(), metaData.botType(), metaData.version(), metaData.mainClass()));
            Class<?> clazz = InitMainClass.returnClass(classLoader, metaData.mainClass());
            if(clazz == null || !BotPlugin.class.isAssignableFrom(clazz)){
                logger.severe(String.format("Failed initialize main class bot '%s'. Class is null or not BotPlugin realization"));
                classLoader.close();
                return;
            }
            logger.info(String.format("Creating instance..."));
            BotPlugin botPlugin = (BotPlugin) clazz.getDeclaredConstructor().newInstance();
            Method onLoad = InteractiveReflection.getOnLoadMethod(botPlugin).orElse(null);
            Method onUnload = InteractiveReflection.getOnUnloadMethod(botPlugin).orElse(null);
            if(onLoad == null || onUnload == null){
                logger.severe(String.format("Failed initialize bot '%s' methods onLoad() or onUnload. Methods is null"));
                classLoader.close();
                return;
            }
            logger.info(String.format("Creating thread..."));
            Future<?> future = threadManager.submitForExpansion(botPlugin, logger, pathToBots.resolve(metaData.name()).toFile(), metaData);
            if(future == null){
                logger.severe(String.format("Failed init bot '%s'. Future is null", botJar.getName()));
                classLoader.close();
                return;
            }
            ExpansionItem item = new ExpansionItem(botPlugin, botJar.toPath(), ExpansionType.BOT, metaData, classLoader, future);
            this.expansionHolder.add(item);
            logger.info(String.format("Bot '%s' load complete", metaData.name()));
        } catch (Exception | NoClassDefFoundError e) {
            if(classLoader != null ) try { classLoader.close(); }catch (IOException ignore){  }
            e.printStackTrace();
        }
    }
    public static class BotsFiles{
        private BotsFiles(){  }
        public static Optional<File[]> bots(Path pathToBots){ return Optional.ofNullable(pathToBots.toFile().listFiles((dir, name) -> name.endsWith("jar"))); }
    }
}
