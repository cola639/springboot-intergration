package org.spring.springboot.exception.movie;

/**
 * 电影不存在异常（支持国际化）
 */
public class MovieNotFoundException extends MovieException {

    private static final long serialVersionUID = 1L;

    public MovieNotFoundException() {
        super("movie.not.found"); // 对应 i18n key
    }
}
