package com.lfj.plugin.api.dependencies;

import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
public @interface DepInfo {
    String name() default "Untitled";
    String description() default "none";
}
