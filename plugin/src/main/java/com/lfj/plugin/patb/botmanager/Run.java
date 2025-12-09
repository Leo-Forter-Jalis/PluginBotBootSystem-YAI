package com.lfj.plugin.patb.botmanager;

import com.lfj.plugin.patb.botmanager.load.ThreadManager;

public class Run {
    private DataHolder holder;
    private ThreadManager threadManager;
    public Run(ThreadManager threadManager){
        this.threadManager = threadManager;
    }
    public void run(){
        this.threadManager.run();
    }
    public void run(String key){
        this.threadManager.run(key);
    }
}
