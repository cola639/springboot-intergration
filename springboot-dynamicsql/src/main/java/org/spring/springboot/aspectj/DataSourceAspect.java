package org.spring.springboot.aspectj;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.spring.springboot.annotation.TargetDataSource;
import org.spring.springboot.config.DataSourceContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class DataSourceAspect {

    @Before("@annotation(targetDataSource)")
    public void changeDataSource(JoinPoint point, TargetDataSource targetDataSource) throws Throwable {
        DataSourceContextHolder.setDataSourceType(targetDataSource.value());
    }

    @After("@annotation(targetDataSource)")
    public void clearDataSource(JoinPoint point, TargetDataSource targetDataSource) {
        DataSourceContextHolder.clearDataSourceType();
    }
}