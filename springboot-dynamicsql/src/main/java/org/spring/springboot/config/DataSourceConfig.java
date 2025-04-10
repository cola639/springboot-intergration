package org.spring.springboot.config;

import org.spring.springboot.annotation.TargetDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class DataSourceConfig {

    // 主数据源配置
    @Primary
    @Bean(name = "primaryDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.primary")
    public TargetDataSource primaryDataSource() {
        return (TargetDataSource) DataSourceBuilder.create().build();
    }

    // 次数据源配置
    @Bean(name = "secondaryDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.secondary")
    public TargetDataSource secondaryDataSource() {
        return (TargetDataSource) DataSourceBuilder.create().build();
    }
}
