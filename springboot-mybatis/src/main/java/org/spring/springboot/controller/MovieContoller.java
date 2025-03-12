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
        System.out.println("all movies");
        System.out.println(movieService.selectAllMovie());
        return movieService.selectAllMovie();
    }

    @PostMapping("/selectMovieList")
    public List<Movie> selectMovieList(@RequestBody Movie movie) {
        System.out.println("movie" + movie);
        List<Movie> movieList;
        movieList = movieService.selectMovieList(movie);

        return movieList;
    }

    @PostMapping("/getMovieByName")
    public List<Movie> getMovieByName(@RequestBody Movie movie) {
        System.out.println("movie" + movie);
        return movieService.selectByName(movie);
    }

    @PostMapping("/generateMovie")
    public Integer generateMovie(@RequestBody Movie movie) {
        System.out.println("movie" + movie);
        return movieService.insertMovie(movie);
    }

    @PostMapping("/updateMovie")
    public Integer updateMovie(@RequestBody Movie movie) {
        System.out.println("movie" + movie);
        return movieService.updateMovie(movie);
    }

    @PostMapping("/deleteMovieByIds/{movieIds}")
    public int delMovieByIds(@PathVariable Long[] ids) {
        return movieService.deleteMovieByIds(ids);
    }

}
