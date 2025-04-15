package org.spring.springboot.Interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.context.i18n.LocaleContextHolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

@Component
public class AcceptLanguageInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(AcceptLanguageInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String language = request.getHeader("Accept-Language");

        Locale locale = Locale.SIMPLIFIED_CHINESE; // 默认简体中文
        if (language != null) {
            if (language.toLowerCase().startsWith("en")) {
                locale = Locale.US;
            } else if (language.toLowerCase().startsWith("zh")) {
                locale = Locale.SIMPLIFIED_CHINESE;
            }
        }

        // 手动设置当前线程的 Locale
        LocaleContextHolder.setLocale(locale);

        log.info("拦截器设置语言为：{}", locale);
        return true;
    }
}
