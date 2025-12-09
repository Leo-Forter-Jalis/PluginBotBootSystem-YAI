package com.lfj.plugin.patb.botmanager;

import com.lfj.plugin.patb.botmanager.load.ThreadManager;

import java.io.File;

import static com.lfj.plugin.patb.botmanager.load.ReflectionClass.invokeMethod;

public class Unload {
    private DataHolder dataHolder;
    private ThreadManager threadManager;

    public Unload(ThreadManager threadManager){
        this.threadManager = threadManager;
    }
    /*
    public void unload(){
        for(String key : this.dataHolder.keySet()){
            Data data = this.dataHolder.get(key);
            data.getPlugin().onDisable();
            data.getThread().interrupt();
            try {
                data.getThread().join(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            try {
                data.getClassLoader().close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            this.dataHolder.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    */
    public void unload(){
        try {
            this.threadManager.stop();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    public void unload(File file){
        try{
            for(DataHolder.Data data : this.dataHolder.values()){
                if(data.getJarFile().equals(file) && data.isRunning()){
                    data.setRunning(false);
                    this.threadManager.stop(data.getMetaData().getName());
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
