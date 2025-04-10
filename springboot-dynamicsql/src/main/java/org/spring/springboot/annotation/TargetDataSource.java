package org.spring.springboot.annotation;

import org.spring.springboot.enums.DataSourceType;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface TargetDataSource {
    String value();
}