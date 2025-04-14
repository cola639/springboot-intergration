package org.spring.springboot.service;


import org.spring.springboot.exception.movie.MovieException;
import org.spring.springboot.repository.MovieRepository;
import org.spring.springboot.domain.Movie;
import org.spring.springboot.service.IMovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieServiceImpl implements IMovieService {
    @Autowired
    private MovieRepository movieRepository;


    @Override
    public List<Movie> selectAllMovies() {
        return movieRepository.findAll();
    }

    /**
     * 新增电影
     *
     * @param movie
     */
    @Override
    public Movie addMovie(Movie movie) {
        if (movie.getMovieName() == null) {
            throw new MovieException("movie.not.found");
        }


        return movieRepository.save(movie);
    }
}
