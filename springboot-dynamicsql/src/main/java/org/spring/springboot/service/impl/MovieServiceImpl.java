package org.spring.springboot.service.impl;



import org.spring.springboot.dao.MovieMapper;
import org.spring.springboot.domain.Movie;
import org.spring.springboot.enums.DataSourceType;
import org.spring.springboot.service.IMovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MovieServiceImpl implements IMovieService {


    @Override
    public List<Movie> selectAllMovie() {

    }

    @Override
    public List<Movie> selectAllMovieFromMaster() {

    }

 }
