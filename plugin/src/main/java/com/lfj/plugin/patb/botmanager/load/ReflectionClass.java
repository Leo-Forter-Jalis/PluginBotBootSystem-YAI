package com.lfj.plugin.patb.botmanager.load;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.lfj.plugin.api.BotPlugin;

public class ReflectionClass {
    public static Method getMethod(BotPlugin plugin, String methodName, Class<?>... parameters){
        try{
            Method method;
            if(parameters == null || parameters.length == 0) return plugin.getClass().getSuperclass().getDeclaredMethod(methodName);
            else{
                return plugin.getClass().getSuperclass().getDeclaredMethod(methodName, parameters);
            }
        }catch (NoSuchMethodException e){
            e.printStackTrace();
            return null;
        }
    }
    public static void invokeMethod(Method method, Object instance, Object... args){
        try {
            method.setAccessible(true);
            method.invoke(instance, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
