package com.lfj.plugin.patb.botmanager;

import com.lfj.plugin.api.BotPlugin;
import com.lfj.plugin.api.MetaData;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;

public class DataHolder {
    public static class Data{
        private BotPlugin plugin;
        private Method onUnload;
        private MetaData metaData;
        private File jarFile;
        private boolean running;
        public Data(BotPlugin plugin, Method onUnload, MetaData metaData, File jarFile, boolean running){
            this.plugin = plugin;
            this.onUnload = onUnload;
            this.metaData = metaData;
            this.jarFile = jarFile;
            this.running = running;
        }
        public BotPlugin getPlugin(){return this.plugin;}
        public Method getOnUnload(){return this.onUnload;}
        public MetaData getMetaData(){return this.metaData;}
        public File getJarFile(){return this.jarFile;}
        public boolean isRunning(){return this.running;}
        public void setRunning(boolean running){this.running = running;}
    }

    private static Map<String, Data> dataMap = new HashMap<>();
    public static void put(Data value){dataMap.put(value.getMetaData().getName(), value);}
    public static void remove(String key){dataMap.remove(key);}
    public static boolean hasKey(String key){return dataMap.containsKey(key);}
    public static int length(){return dataMap.size();}
    public static  Data get(String key){return dataMap.get(key);}
    public static Set<String> keySet(){return dataMap.keySet();}

    public static void close() {
        dataMap.clear();
        dataMap = null;
    }
}
