package org.spring.springboot.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

/**
 * 国际化消息工具类
 */
public class MessageUtils {

    private static final Logger log = LoggerFactory.getLogger(MessageUtils.class);

    private static MessageSource messageSource;

    /**
     * 设置 MessageSource（由 Spring 初始化注入）
     */
    public static void setMessageSource(MessageSource source) {
        messageSource = source;
        log.info("MessageSource 已注入成功: {}", source);
    }

    /**
     * 获取国际化消息（自动根据当前 Locale 获取）
     *
     * @param code 国际化 key
     * @param args 占位参数
     * @return 国际化翻译内容，如果未找到则返回 code 本身
     */
    public static String message(String code, Object... args) {
        Locale locale = LocaleContextHolder.getLocale();

        log.info("当前 Locale: {}", locale);
        log.info("尝试获取国际化消息 -> key: [{}], locale: [{}], args: {}", code, locale, args);

        try {
            String msg = messageSource.getMessage(code, args, code, locale);
            log.info("成功获取国际化消息: {}", msg);
            return msg;
        } catch (Exception e) {
            log.info("未找到国际化消息 key='{}', 使用默认值返回: {}", code, code);
            return code;
        }
    }
}