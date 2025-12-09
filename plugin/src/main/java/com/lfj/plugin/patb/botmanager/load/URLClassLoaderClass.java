package com.lfj.plugin.patb.botmanager.load;

import java.io.IOException;
import java.net.URLClassLoader;
import java.net.URL;
import java.lang.ClassLoader;

public class URLClassLoaderClass implements AutoCloseable {
    private URLClassLoader classLoader;
    public URLClassLoaderClass(URL url, ClassLoader loader) {
        this.classLoader = new URLClassLoader(new URL[]{url}, loader);
    }
    public URL get(String resource){
        return this.classLoader.findResource(resource);
    }
    public Class<?> getClass(String resource1){
        try {
            return this.classLoader.loadClass(resource1);
        }catch(ClassNotFoundException e){
            e.printStackTrace();
            return null;
        }
    }
    public boolean isInit(){
        return classLoader != null;
    }
    @Override
    public void close(){
        try {
            if(classLoader != null) {
                this.classLoader.close();
                this.classLoader = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
