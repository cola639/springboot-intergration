package org.spring.springboot.controller;


import org.spring.springboot.domain.Movie;
import org.spring.springboot.service.IMovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movie")
public class MovieContoller {
    @Autowired
    private IMovieService movieService;

    @GetMapping("/allMovies")
    public List<Movie> getAllMovie() {
        System.out.println(movieService.selectAllMovie());
        return movieService.selectAllMovie();
    }

    @GetMapping("/allMoviesFromMater")
    public List<Movie> MoviesFromMater() {
        System.out.println(movieService.selectAllMovieFromMaster());
        return movieService.selectAllMovie();
    }


}
