package com.custom.interceptor.Annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomAnnotation {
    String key() default "defaultKey";

    int intKey() default 0;

    String[] stringArray() default {"default1", "default2"};

    int[] intArray() default {1, 2};
}
