package org.spring.springboot.service;

import org.spring.springboot.domain.Movie;

import java.util.List;

public interface IMovieService {

    /**
     * 查询所有电影
     */
    List<Movie> selectAllMovies();

    /**
     * 新增电影
     */
    Movie addMovie(Movie movie);
}
