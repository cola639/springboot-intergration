package org.spring.springboot.config;

import org.spring.springboot.utils.MessageUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageInitConfig implements ApplicationContextAware {

    @Override
    public void setApplicationContext(ApplicationContext context) {
        MessageUtils.setMessageSource(context.getBean(MessageSource.class));
    }
}
