package com.lfj.plugin.patb.botmanager;

import java.io.File;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

import java.util.List;
import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.lfj.plugin.api.telegram.TelegramBotPlugin;
import com.lfj.plugin.api.MetaData;
import org.bukkit.plugin.java.JavaPlugin;

public class BotLoadAndUnload {
    private List<BotLoaderContainer> containers;
    private JavaPlugin plugin;
    public BotLoadAndUnload(JavaPlugin plugin){
        this.containers = new ArrayList<>();
        this.plugin = plugin;
    }
    private record BotLoaderContainer(
            URLClassLoader classLoader,
            TelegramBotPlugin botPlugin,
            MetaData metaData,
            Thread botThread
    ){}
    public void init() {
        String dirName = "bots";
        if(!checkExistsDirectory(dirName)) return;
        File directory = new File(plugin.getDataFolder(), dirName);
        File[] files = directory.listFiles((dir, name) -> name.endsWith(".jar"));
        try {
            bodyInit(files);
        }catch (Exception e){
            plugin.getLogger().severe(e.getMessage());
            e.printStackTrace();
        }
    }
    private boolean checkExistsDirectory(String dirName){
        File dir = new File(plugin.getDataFolder(), dirName);
        if(dir.exists()) return true;
        dir.mkdirs();
        return false;
    }
    private void bodyInit(File[] files) throws Exception{
        for(File jar : files){
            URL jarUrl = jar.toURI().toURL();
            URLClassLoader classLoader = new URLClassLoader(new URL[]{jarUrl}, this.getClass().getClassLoader());
            URL botMetaDataUrl = classLoader.findResource("bot.json");
            ObjectMapper mapper = new ObjectMapper(new JsonFactory());
            MetaData data = mapper.readValue(botMetaDataUrl, MetaData.class);
            Class<?> clazz = classLoader.loadClass(data.getMainClass());
            if(TelegramBotPlugin.class.isAssignableFrom(clazz)){
                TelegramBotPlugin botPlugin = (TelegramBotPlugin) clazz.getDeclaredConstructor().newInstance();
                callMethods(botPlugin, data);
                configurationContainer(classLoader, botPlugin, data);
            }else{
                classLoader.close();
                return;
            }
        }
        load();
    }
    private void configurationContainer(URLClassLoader loader, TelegramBotPlugin plugin, MetaData data){
        Thread botThread = new Thread(()->{
            try {
                Method method = plugin.getClass().getSuperclass().getDeclaredMethod("onLoad");
                method.setAccessible(true);
                method.invoke(plugin);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
        botThread.setName(data.getName());
        this.containers.add(new BotLoaderContainer(loader, plugin, data, botThread));
    }
    private void load() {
         for(BotLoaderContainer container : containers){
             startThreadTask(container.botPlugin, container.metaData.getName(), container);
         }
    }
    private void startThreadTask(TelegramBotPlugin botPlugin, String name, BotLoaderContainer container){

    }

    private void callMethods(TelegramBotPlugin botPlugin, MetaData data) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method setPlugin = botPlugin.getClass().getSuperclass().getDeclaredMethod("setPlugin", JavaPlugin.class);
        Method setMetaData = botPlugin.getClass().getSuperclass().getDeclaredMethod("setMetaData", MetaData.class);
        setPlugin.setAccessible(true);
        setMetaData.setAccessible(true);
        setPlugin.invoke(botPlugin, this.plugin);
        setMetaData.invoke(botPlugin, data);
    }

    public void unload() throws IOException {
        plugin.getLogger().info("Unloaded bots...");
        for(BotLoaderContainer container : containers){
            unloadBot(container.botPlugin, container.botThread);
            unloadClassLoader(container.classLoader);
        }
        containers.clear();
        plugin.getLogger().info("Unload bots!");
    }
    private void unloadBot(TelegramBotPlugin plugin, Thread thread){
        plugin.onDisable();
        thread.interrupt();
        try {
            thread.join(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    private void unloadClassLoader(URLClassLoader classLoader){
        try {
            classLoader.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
