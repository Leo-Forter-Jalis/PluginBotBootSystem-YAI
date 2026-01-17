package com.lfj.plugin.patb.bootmanager.component;

import com.lfj.plugin.api.bots.BotPlugin;
import com.lfj.plugin.api.bots.MetadataBot;
import com.lfj.plugin.api.dependencies.Dependency;
import com.lfj.plugin.api.dependencies.MetadataDep;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.logging.Logger;

public class InteractiveReflection {
    private InteractiveReflection(){
    }
    public static Optional<Method> getOnLoadMethod(BotPlugin botPlugin) throws NoSuchMethodException {
        return Optional.ofNullable(botPlugin.getClass().getSuperclass().getDeclaredMethod("onLoad", Logger.class, File.class, MetadataBot.class));
    }
    public static Optional<Method> getOnUnloadMethod(BotPlugin botPlugin) throws NoSuchMethodException {
        return Optional.ofNullable(botPlugin.getClass().getSuperclass().getDeclaredMethod("onUnload"));
    }
    public static Optional<Method> getInitMethod(Dependency dependency) throws NoSuchMethodException {
        return Optional.ofNullable(dependency.getClass().getSuperclass().getDeclaredMethod("init", JavaPlugin.class, MetadataDep.class));
    }
    public static Optional<Method> getShutdownMethod(Dependency dependency) throws NoSuchMethodException {
        return Optional.ofNullable(dependency.getClass().getSuperclass().getDeclaredMethod("shutdown"));
    }
}
