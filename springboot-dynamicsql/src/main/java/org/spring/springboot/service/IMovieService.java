package org.spring.springboot.service;


import org.spring.springboot.domain.Movie;

import java.util.List;

public interface IMovieService {
    List<Movie> selectAllMovie();

    List<Movie> selectAllMovieFromMaster();

    List<Movie> selectByName(Movie movie);

    int insertMovie(Movie movie);

    int updateMovie(Movie movie);

    int deleteMovieByIds(Long[] ids);

    List<Movie> selectMovieList(Movie movie);

}
