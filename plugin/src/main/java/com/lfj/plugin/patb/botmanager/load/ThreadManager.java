package com.lfj.plugin.patb.botmanager.load;

import java.lang.Thread;
import java.lang.reflect.Method;

import com.lfj.plugin.api.BotPlugin;

import java.io.File;
import com.lfj.plugin.api.MetaData;
import org.bukkit.plugin.java.JavaPlugin;

import com.lfj.plugin.patb.botmanager.DataHolder;

import static com.lfj.plugin.patb.botmanager.load.ReflectionClass.invokeMethod;

public class ThreadManager {
    public ThreadManager(){
    }
    public void add(BotPlugin plugin, Method onLoad, JavaPlugin javaPlugin, File directory , MetaData metaData){
        Thread thread = new Thread(()->{
            invokeMethod(onLoad, plugin, javaPlugin, directory, metaData);
        }, metaData.getName() + "-thread");
        DataHolder.put(new DataHolder.Data());
    }
    public void run(){

    }
    public void run(String key){

    }
    public void stop(String key) throws InterruptedException {

    }
    public void stop() throws InterruptedException {

    }
    private void clear(){

    }
    private void removeBot(){

    }
}
