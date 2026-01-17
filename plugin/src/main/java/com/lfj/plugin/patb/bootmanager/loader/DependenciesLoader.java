package com.lfj.plugin.patb.bootmanager.loader;

import com.lfj.plugin.api.bots.MetadataBot;
import com.lfj.plugin.api.dependencies.Dependency;
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
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Future;
import java.util.logging.Logger;

public class DependenciesLoader {
    private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(DependenciesLoader.class);
    private JavaPlugin plugin;
    private Path pathToDependencies;
    private boolean globalRunning = false;
    private Logger logger;
    private ThreadManager threadManager;
    private ExpansionHolder expansionHolder;
    private DependenciesLoader(){
    }
    public DependenciesLoader(JavaPlugin plugin, Path pathToDependencies, Logger logger, ThreadManager threadManager, ExpansionHolder expansionHolder){
        this.plugin = plugin;
        this.pathToDependencies = pathToDependencies;
        this.logger = logger;
        this.threadManager = threadManager;
        this.expansionHolder = expansionHolder;
    }
    public void loadDependencies(){
        logger.info("Dependencies loader started.");
        System.out.println(globalRunning);
        if(globalRunning) return;
        File[] files = DependencyFiles.dependencies(pathToDependencies).orElse(new File[]{});
        for(File file : files){
            logger.info(String.format("File > '%s'", file.getName()));
            loadDependency(file);
        }
        globalRunning = true;
    }
    public void loadDependency(File depend){
        if(!List.of(DependencyFiles.dependencies(pathToDependencies).orElse(new File[]{})).contains(depend)) return;
        if(expansionHolder.contains(depend)) return;
        URLClassLoader classLoader = null;
        try{
            logger.info(String.format("Initialize depend '%s'...", depend.getName()));
            classLoader = new URLClassLoader(new URL[]{depend.toURI().toURL()}, this.getClass().getClassLoader());
            if(!(MetadataComponent.loadMetadata(classLoader, "depend.json", MetadataType.DEPENDENCIES).orElse(new MetadataBot("", "", "", "", "")) instanceof MetadataDep metadata)){
                logger.severe(String.format("Failed initialize depend '%s'. Metadata is null or bots type", depend.getName()));
                classLoader.close();
                return;
            }
            logger.info(String.format("Initialize main class..."));
            Class<?> clazz = InitMainClass.returnClass(classLoader, metadata.mainClass());
            if(clazz == null || !Dependency.class.isAssignableFrom(clazz)){
                logger.severe(String.format("Failed initialize depend '%s'. Class is null or not Dependency realization", depend.getName()));
                classLoader.close();
                return;
            }
            Dependency dependency = (Dependency) clazz.getDeclaredConstructor().newInstance();
            Future<?> future = this.threadManager.submitForExpansion(dependency, this.plugin, metadata);
            if(future == null){
                this.logger.severe(String.format("Failed initialize depend '%s'. Thread is null", depend.getName()));
                classLoader.close();
                return;
            }
            this.expansionHolder.add(new ExpansionItem(dependency, depend.toPath(), ExpansionType.DEPENDENCY, metadata, classLoader, future));
            this.logger.info(String.format("Depend '%s' loaded&started", metadata.dependencyName()));
        } catch (Exception e) {
            if(classLoader != null) try{classLoader.close();}catch (IOException ignore){}
            e.printStackTrace();
        }
    }
    public static class DependencyFiles{
        private DependencyFiles(){
        }
        public static Optional<File[]> dependencies(Path pathToDependencies){
            return Optional.ofNullable(pathToDependencies.toFile().listFiles((dir, name) -> name.endsWith("jar")));
        }
    }
}
