package org.spring.springboot.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spring.springboot.domain.AjaxResult;
import org.spring.springboot.domain.Movie;
import org.spring.springboot.exception.ServiceException;
import org.spring.springboot.exception.movie.MovieException;
import org.spring.springboot.service.IMovieService;
import org.spring.springboot.utils.MessageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/movie")
public class MovieController {
    private static final Logger log = LoggerFactory.getLogger(MessageUtils.class);

    @Autowired
    private IMovieService movieService;


    // 1. 获取所有电影
    @GetMapping("/allMovies")
    public List<Movie> getAllMovie() {
        return movieService.selectAllMovies();
    }

    // 2. 模拟 HttpRequestMethodNotSupportedException (不支持的方法)
    @PostMapping("/allMovies")  // 用POST方法会触发 HttpRequestMethodNotSupportedException
    public List<Movie> getAllMoviesPost() {
        return movieService.selectAllMovies();
    }

    // 3. 模拟 ServiceException (业务异常)
    @GetMapping("/serviceError")
    public AjaxResult triggerServiceException() {
        AjaxResult ajax = AjaxResult.success();
        movieService.deleteMovie(null);
        return ajax;
    }

    // 4. 模拟 RuntimeException (运行时异常)
    @GetMapping("/runtimeError")
    public List<Movie> triggerRuntimeException() {
        throw new RuntimeException("未知的运行时异常");
    }

    // 5. 模拟 Exception (系统异常)
    @GetMapping("/exception")
    public AjaxResult triggerException(HttpServletRequest request) throws Exception {
        log.info("请求头语言: {}", request.getHeader("Accept-Language"));
        return AjaxResult.error(MessageUtils.message("movie.system.error"));
    }

    // 6. 模拟 BindException (字段绑定异常)
    @PostMapping("/addMovie")
    public Movie addMovie(@RequestBody @Valid Movie movie) {
        return movieService.addMovie(movie);
    }

    // 7. 模拟 MethodArgumentNotValidException (方法参数验证异常)
    @PostMapping("/addValidatedMovie")
    public Movie addValidatedMovie(@RequestBody @Valid Movie movie) {
        return movieService.addMovie(movie);
    }
}
