package org.spring.springboot.exception.movie;

import org.spring.springboot.exception.base.BaseException;


/**
 * 电影模块异常类，支持 i18n 国际化提示
 */
public class MovieException extends BaseException {

    private static final long serialVersionUID = 1L;

    // 错误模块固定为 "movie"
    private static final String MODULE = "movie";

    /**
     * 构造：使用 i18n code + 参数
     */
    public MovieException(String code, Object[] args) {
        super(MODULE, code, args, null);
    }

    /**
     * 构造：只传 i18n code
     */
    public MovieException(String code) {
        super(MODULE, code, null, null);
    }

    /**
     * 构造：i18n code + 参数 + 默认消息
     */
    public MovieException(String code, Object[] args, String defaultMessage) {
        super(MODULE, code, args, defaultMessage);
    }


}
