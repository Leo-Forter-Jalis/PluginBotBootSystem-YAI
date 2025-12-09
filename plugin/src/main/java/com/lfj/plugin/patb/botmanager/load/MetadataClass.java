package com.lfj.plugin.patb.botmanager.load;

import java.io.IOException;
import java.net.URL;

import com.lfj.plugin.api.MetaData;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonFactory;

public class MetadataClass {
    private MetaData metaData;
    public MetadataClass(){
    }
    public void add(URLClassLoaderClass loaderClass){
        if(!loaderClass.isInit()) return;
        URL metadata = loaderClass.get("bot.json");
        if(metadata != null){
            try {
                ObjectMapper mapper = new ObjectMapper(new JsonFactory());
                MetaData metaData = mapper.readValue(metadata, MetaData.class);
                if(this.metaData != null) this.metaData = null;
                if(metaData != null) this.metaData = metaData;

            }catch (IOException e){
                e.printStackTrace();
                return;
            }
        }
    }
    public MetaData get(){
        return this.metaData;
    }
}
