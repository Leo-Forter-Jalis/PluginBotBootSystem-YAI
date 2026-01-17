package com.lfj.plugin.patb.bootmanager.component;

import com.lfj.plugin.api.Expansion;
import com.lfj.plugin.api.MetadataInterface;
import com.lfj.plugin.patb.bootmanager.ExpansionType;

import java.io.File;
import java.io.IOException;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Future;

public class ExpansionHolder {
    public record ExpansionItem (
            Expansion expansion,
            Path expansionPath,
            ExpansionType expansionType,
            MetadataInterface metadataInterface,
            URLClassLoader classLoader,
            Future<?> future
    ) {
        public void cancel(){
            if(future != null || future.isDone()){
                future.cancel(true);
            }
        }
        public void classLoaderClear() throws IOException { classLoader.close(); }
    }
    private List<ExpansionItem> expansionItems = new ArrayList<>();
    public void add(ExpansionItem item){ if(item != null) expansionItems.add(item); }
    public void remove(ExpansionItem item) throws IOException {
        if(expansionItems.contains(item)) {
            item.classLoaderClear();
            expansionItems.remove(item);
        }
    }
    public boolean contains(File expansionFile){
        for(ExpansionItem item : expansionItems)
            if(item.expansionPath.equals(expansionFile.toPath())) return true;
        return false;
    }
    public List<ExpansionItem> getList(){ return this.expansionItems; }
    public Optional<ExpansionItem> get(Path expansionsPath) {
        for(ExpansionItem item : expansionItems){
            if(item.expansionPath.equals(expansionsPath)) return Optional.of(item);
        }
        return Optional.empty();
    }
    public void clear(){
        for(ExpansionItem item : expansionItems){
            try{
                item.classLoaderClear();
            }catch (IOException ex){
                ex.printStackTrace();
            }
        }
        expansionItems.clear();
    }
}
