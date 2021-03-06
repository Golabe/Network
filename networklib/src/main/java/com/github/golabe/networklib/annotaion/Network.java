package com.github.golabe.networklib.annotaion;

import com.github.golabe.networklib.type.NetType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Network {
    NetType netType() default NetType.AUTO;
}
