package org.spring.springboot.service;


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
    public List<Movie> selectAllMovie() {
        return movieRepository.findAll();
    }


    // 根据演员ID查找所有电影
    public List<Movie> getMoviesByActorId(Long actorId) {
        return movieRepository.findMoviesByActorId(actorId);
    }

}
