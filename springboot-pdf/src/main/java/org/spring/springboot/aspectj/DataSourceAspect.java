package org.spring.springboot.aspectj;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.spring.springboot.annotation.SourceSwitch;
import org.spring.springboot.config.DynamicDataSourceContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Aspect
@Component
public class DataSourceAspect {

    // 比 @Before + @After error的时候也可以清除
    @Around("@annotation(sourceSwitch)")
    public Object switchDataSource(ProceedingJoinPoint joinPoint, SourceSwitch sourceSwitch) throws Throwable {
        try {
            // 设置数据源
            String dataSource = sourceSwitch.value();
            DynamicDataSourceContextHolder.setDataSourceType(dataSource);
            return joinPoint.proceed();
        } finally {
            // 关键操作：执行完成后清除上下文，防止线程复用引发错乱
            DynamicDataSourceContextHolder.clearDataSourceType();
        }
    }
}