package org.spring.springboot.utils;


import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * 获取i18n资源文件
 *
 * @author colaclub
 */
public class MessageUtils {

    private static MessageSource messageSource;

    public static void setMessageSource(MessageSource source) {
        messageSource = source;
    }

    public static String message(String code, Object... args) {
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }
}

