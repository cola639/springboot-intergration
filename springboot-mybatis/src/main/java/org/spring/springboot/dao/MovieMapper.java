package org.spring.springboot.dao;


import org.spring.springboot.domain.Movie;

import java.util.List;

public interface MovieMapper {

    List<Movie> selectAllMovie();

    List<Movie> selectByName(Movie movie);

    int insertMovie(Movie movie);

    int updateMovie(Movie movie);

    int deleteMovieByIds(Long[] ids);

    List<Movie> selectMovieList(Movie movie);
}
