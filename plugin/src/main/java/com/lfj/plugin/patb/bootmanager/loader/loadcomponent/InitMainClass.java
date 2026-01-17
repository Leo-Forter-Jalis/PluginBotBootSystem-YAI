package com.lfj.plugin.patb.bootmanager.loader.loadcomponent;

import java.net.URLClassLoader;

public class InitMainClass {
    private InitMainClass(){
    }
    public static Class<?> returnClass(URLClassLoader classLoader, String mainClassName) throws ClassNotFoundException, NoClassDefFoundError {
        return classLoader.loadClass(mainClassName);
    }
}
