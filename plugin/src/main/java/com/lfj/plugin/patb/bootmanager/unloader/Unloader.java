package com.lfj.plugin.patb.bootmanager.unloader;

import com.lfj.plugin.patb.bootmanager.component.ExpansionHolder;
import com.lfj.plugin.patb.bootmanager.component.ExpansionHolder.ExpansionItem;
import com.lfj.plugin.patb.bootmanager.component.ThreadManager;

import java.io.IOException;
import java.nio.file.Path;

public class Unloader {
    private ThreadManager threadManager;
    private ExpansionHolder expansionHolder;
    private Unloader(){  }
    public Unloader(ThreadManager threadManager, ExpansionHolder expansionHolder){
        this.threadManager = threadManager;
        this.expansionHolder = expansionHolder;
    }
    public void unloadAll(){

        this.expansionHolder.clear();
    }
    public void unload(ExpansionItem item){
        this.threadManager.stopThread(item);
        try {
            this.expansionHolder.remove(item);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
