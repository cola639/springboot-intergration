package org.spring.springboot.Interceptor;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class LocaleInterceptorConfig implements WebMvcConfigurer {

    private final AcceptLanguageInterceptor acceptLanguageInterceptor;

    public LocaleInterceptorConfig(AcceptLanguageInterceptor acceptLanguageInterceptor) {
        this.acceptLanguageInterceptor = acceptLanguageInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(acceptLanguageInterceptor).addPathPatterns("/**");
    }
}
