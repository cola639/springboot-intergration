package com.monitor.config;


import com.monitor.interceptor.UriStatsInterceptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WebMvc configuration to register interceptors
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final UriStatsInterceptor uriStatsInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.info("Registering UriStatsInterceptor for all requests...");
        registry.addInterceptor(uriStatsInterceptor)
                .addPathPatterns("/**"); // intercept all URIs
    }
}
