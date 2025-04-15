package org.spring.springboot.config;

import org.spring.springboot.utils.MessageUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Configuration;

/**
 * 国际化消息初始化配置类
 *
 * ✅ 功能说明：
 * 1. 实现 ApplicationContextAware 接口，获取 Spring 容器上下文。
 * 2. 在容器启动时，主动获取容器中的 MessageSource（国际化核心接口）。
 * 3. 将 MessageSource 注入到 MessageUtils 工具类中，供全局静态方法使用。
 *
 * ✅ 使用场景：
 * - 用于在代码中通过 MessageUtils.message("xxx") 静态方式获取国际化提示信息。
 * - 支持 i18n 多语言，如 messages_zh_CN.properties、messages_en_US.properties 等。
 *
 * ⚠️ 注意：MessageUtils 是自定义的静态工具类，不能使用 @Autowired 注入，因此必须通过这种方式传入 MessageSource。
 *
 * @author colaclub
 */
@Configuration
public class MessageInitConfig implements ApplicationContextAware {

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        // 从 Spring 容器中获取 MessageSource（多语言资源加载器）
        MessageSource messageSource = applicationContext.getBean(MessageSource.class);

        // 注入到工具类中，供 MessageUtils 静态调用
        MessageUtils.setMessageSource(messageSource);
    }
}
