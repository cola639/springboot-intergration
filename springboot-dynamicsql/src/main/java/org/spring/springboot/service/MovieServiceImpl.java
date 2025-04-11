package org.spring.springboot.service;


import org.spring.springboot.annotation.SourceSwitch;
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


    @SourceSwitch("primary")
    @Override
    public List<Movie> selectAllMovieFromMaster() {
        return movieRepository.findAll();
    }

    @SourceSwitch("secondary")
    @Override
    public List<Movie> selectAllMovieFromSlave() {
        return movieRepository.findAll();
    }



}
