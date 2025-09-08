package com.monitor.config;

import com.p6spy.engine.spy.P6SpyOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;

/**
 * P6Spy configuration class
 */
@Slf4j
@Configuration
public class P6SpyConfig {

    @PostConstruct
    public void init() {
        log.info("Initializing P6Spy with custom SQL logger...");
        // use Slf4jLogger for output
        P6SpyOptions.getActiveInstance().setAppender("com.p6spy.engine.spy.appender.Slf4JLogger");
        // use our custom MessageFormattingStrategy
        P6SpyOptions.getActiveInstance().setLogMessageFormat("com.monitor.listener.P6SpySqlLogger");
    }
}
