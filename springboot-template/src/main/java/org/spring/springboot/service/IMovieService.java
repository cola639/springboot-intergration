package org.spring.springboot.service;


import org.spring.springboot.domain.Movie;

import java.util.List;

public interface IMovieService {

    List<Movie> selectAllMovie();


}
