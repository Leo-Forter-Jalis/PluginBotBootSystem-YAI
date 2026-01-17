package com.lfj.plugin.patb.bootmanager.component;

import com.lfj.plugin.api.Expansion;
import com.lfj.plugin.patb.bootmanager.component.ExpansionHolder.ExpansionItem;
import com.lfj.plugin.api.bots.BotPlugin;
import com.lfj.plugin.api.bots.MetadataBot;
import com.lfj.plugin.api.dependencies.Dependency;
import com.lfj.plugin.api.dependencies.MetadataDep;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NonNull;

import java.io.File;
import java.util.concurrent.*;
import java.util.logging.Logger;

public class ThreadManager {
    private ExecutorService botsService;
    private ExecutorService dependsService;
    private Logger logger;

    private ThreadManager() {
    }

    public ThreadManager(Logger logger) {
        this.logger = logger;
        this.botsService = Executors.newCachedThreadPool(new NamedThreadFactory("bots"));
        this.dependsService = Executors.newCachedThreadPool(new NamedThreadFactory("depends"));
    }

    public Future<?> submitForExpansion(Expansion expansion, Object... arguments){
        Runnable task = switch (expansion){
            case BotPlugin botPlugin -> createBotTask(botPlugin, arguments);
            case Dependency dependency -> createDependTask(dependency, arguments);
            default -> throw new IllegalArgumentException("");
        };
        if(task == null) return null;
        return (expansion instanceof BotPlugin) ? botsService.submit(task) : dependsService.submit(task);
    }
    private Runnable createBotTask(BotPlugin botPlugin, Object... arguments){
        if (arguments.length < 3) {
            logger.severe(String.format("Failed creating bot thread. Argument length < 3"));
            return null;
        }

        if (!(arguments[0] instanceof Logger logger) || !(arguments[1] instanceof File directory) || !(arguments[2] instanceof MetadataBot metaData)) {
            logger.severe(String.format("Failed creating bot thread. Invalid Arguments"));
            return null;
        }
        return () -> botPlugin.onLoad(logger, directory, metaData);
    }
    private Runnable createDependTask(Dependency dependency, Object... arguments){
        if (arguments.length < 2) {
            logger.severe(String.format("Failed creating depend thread. Argument length < 2"));
            return null;
        }

        if (!(arguments[0] instanceof JavaPlugin javaPlugin) || !(arguments[1] instanceof MetadataDep metadata)) {
            logger.severe(String.format("Failed creating depend thread. Invalid Arguments"));
            return null;
        }
        return () -> dependency.init(javaPlugin, metadata);
    }
    public Thread createThreadForExpansion(Expansion expansion, Object... arguments) {
        switch (expansion) {
            case BotPlugin botPlugin -> {
                return createThreadForBot(botPlugin, arguments);
            }
            case Dependency dependency -> {
                return createThreadForDependency(dependency, arguments);
            }
            default -> throw new IllegalArgumentException("Invalid Expansion type");
        }
    }

    private Thread createThreadForBot(BotPlugin botPlugin, Object... arguments) throws IllegalArgumentException {
        if (arguments.length < 3) {
            logger.severe(String.format("Failed creating bot thread. Argument length < 3"));
            return null;
        }

        if (!(arguments[0] instanceof Logger logger) || !(arguments[1] instanceof File directory) || !(arguments[2] instanceof MetadataBot metaData)) {
            logger.severe(String.format("Failed creating bot thread. Invalid Arguments"));
            return null;
        }
        return new Thread(() -> {
            botPlugin.onLoad(logger, directory, metaData);
        }, metaData.name());
    }

    private Thread createThreadForDependency(Dependency dependency, Object... arguments) {
        if (arguments.length < 2) {
            logger.severe(String.format("Failed creating depend thread. Argument length < 2"));
            return null;
        }

        if (!(arguments[0] instanceof JavaPlugin javaPlugin) || !(arguments[1] instanceof MetadataDep metadata)) {
            logger.severe(String.format("Failed creating depend thread. Invalid Arguments"));
            return null;
        }

        return new Thread(() -> {
            dependency.init(javaPlugin, metadata);
        }, metadata.dependencyName());
    }

    public void stopThread(ExpansionItem item) {
        Expansion expansion = item.expansion();
        if (expansion instanceof BotPlugin botPlugin) botPlugin.onUnload();
        if (expansion instanceof Dependency dependency) dependency.shutdown();
        item.cancel();
    }
    public void shutdownAll(){
        botsService.shutdown();
        dependsService.shutdown();
        try{
            if(!botsService.awaitTermination(5, TimeUnit.SECONDS)) botsService.shutdownNow();
            if(!dependsService.awaitTermination(5, TimeUnit.SECONDS)) dependsService.shutdownNow();
        } catch (InterruptedException e) {
            botsService.shutdownNow();
            dependsService.shutdownNow();
            logger.severe(e.getMessage());
        }
    }
    static class NamedThreadFactory implements ThreadFactory {
        private final String threadPrefix;
        private int counter = 0;
        public NamedThreadFactory(String threadPrefix){
            this.threadPrefix = threadPrefix;
        }

        @Override
        public Thread newThread(@NonNull Runnable r) {
            Thread thread = new Thread(r, String.format("%s-%d", threadPrefix, counter++));
            thread.setDaemon(true);
            return thread;
        }
    }
}
