package org.spring.springboot.aspectj;


import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.spring.springboot.annotation.SourceSwitch;
import org.spring.springboot.config.DynamicDataSourceContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Aspect
@Component
public class DataSourceAspect {

    @Before("@annotation(sourceSwitch)")
    public void switchDataSource(SourceSwitch sourceSwitch) {
        String dataSource = sourceSwitch.value();
        // 切换数据源逻辑
        DynamicDataSourceContextHolder.setDataSourceType(dataSource);
    }
}
