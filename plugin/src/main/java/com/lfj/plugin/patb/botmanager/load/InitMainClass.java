package com.lfj.plugin.patb.botmanager.load;

import com.lfj.plugin.api.BotPlugin;
import com.lfj.plugin.api.MetaData;
import com.lfj.plugin.api.telegram.TelegramBotPlugin;

import java.lang.reflect.InvocationTargetException;

public class InitMainClass {
    public static BotPlugin initPlugin(URLClassLoaderClass loaderClass, MetaData metaData){
        Class<?> clazz = loaderClass.getClass(metaData.getMainClass());
        if(clazz == null) return null;
        if(TelegramBotPlugin.class.isAssignableFrom(clazz)) {
            try {
                return (TelegramBotPlugin) clazz.getDeclaredConstructor().newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
