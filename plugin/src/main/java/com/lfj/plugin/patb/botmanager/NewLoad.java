package com.lfj.plugin.patb.botmanager;

import com.lfj.plugin.api.BotPlugin;
import com.lfj.plugin.api.MetaData;
import com.lfj.plugin.patb.botmanager.load.JarFileList;
import com.lfj.plugin.patb.botmanager.load.MetadataClass;
import com.lfj.plugin.patb.botmanager.load.ThreadManager;
import com.lfj.plugin.patb.botmanager.load.URLClassLoaderClass;
import com.lfj.plugin.patb.botmanager.load.InitMainClass;
import static com.lfj.plugin.patb.botmanager.load.ReflectionClass.getMethod;
import org.bukkit.plugin.java.JavaPlugin;
import com.lfj.plugin.patb.botmanager.DataHolder.Data;

import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.nio.file.NoSuchFileException;

public class NewLoad {
    private File directory;
    private JavaPlugin plugin;

    private MetadataClass metadataClass;
    private ThreadManager threadManager;
    private DataHolder dataHolder;
    public NewLoad(ThreadManager threadManager, File directory, JavaPlugin plugin){
        this.directory = directory;
        this.plugin = plugin;
        this.metadataClass = new MetadataClass();
        this.threadManager = threadManager;
    }
    public void load(){
        try{
            JarFileList.init(directory);
        }catch (NoSuchFileException | UnsupportedOperationException e){
            this.plugin.getLogger().warning(e.getMessage());
            e.printStackTrace();
            return;
        }
        for(File file : JarFileList.getCopyFileList()){
            load(file);
        }
    }
    public void load(File file){
        try(URLClassLoaderClass loaderClass = new URLClassLoaderClass(file.toURI().toURL(), this.getClass().getClassLoader())) {
            this.plugin.getLogger().info(file.getName() + " init...");
            metadataClass.add(loaderClass);
            MetaData metaData = metadataClass.get();
            if(metaData == null){
                this.plugin.getLogger().warning("MetaData '" + file.getName() + "' is null!");
                return;
            }
            BotPlugin botPlugin = InitMainClass.initPlugin(loaderClass, metaData);
            if(botPlugin == null){
                this.plugin.getLogger().warning("BotPlugin '" + file.getName() + "' is null!");
                return;
            }
            Method onLoad = getMethod(botPlugin, "onLoad", JavaPlugin.class, File.class, MetaData.class);
            Method onUnload = getMethod(botPlugin, "onUnload");
            if(onLoad == null || onUnload == null) {
                this.plugin.getLogger().warning("Methods in '" + file.getName() + "' onLoad or onUnload is null!");
                return;
            }
            onUnload.setAccessible(true);
            this.threadManager.add(botPlugin, onLoad, plugin, new File(directory, metaData.getName()), metaData);
            Data data = new Data(botPlugin, onUnload, metaData, file, false);
            this.dataHolder.put(data);
        } catch (MalformedURLException e) {
            this.plugin.getLogger().severe(e.getMessage() + " | " + e.getLocalizedMessage());
            e.printStackTrace();
        }
    }
}
