package com.lfj.plugin.api.config;

import com.lfj.plugin.api.bots.MetadataBot;

import java.io.File;

public class Config {
    private File botPluginFolder;
    public Config(File directory, MetadataBot data){
        if(directory == null) throw new IllegalArgumentException("Argument 'directory' is null!");
        this.botPluginFolder = new File(directory, data.name());
        if(!botPluginFolder.exists()) createDirectory();
    }
    private void createDirectory(){
        this.botPluginFolder.mkdirs();
    }

}
