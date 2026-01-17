package com.lfj.plugin.patb.bootmanager;

import com.lfj.plugin.api.bots.MetadataBot;
import com.lfj.plugin.api.dependencies.MetadataDep;

public enum MetadataType {
    DEPENDENCIES(MetadataDep.class),
    BOT(MetadataBot.class);
    private final Class<?> clazz;
    MetadataType(Class<?> clazz){
        this.clazz = clazz;
    }
    public Class<?> getMetadataType(){
        return clazz;
    }
}
