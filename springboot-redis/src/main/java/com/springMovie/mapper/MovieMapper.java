package com.springMovie.mapper;


import com.springMovie.domain.Movie;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


public interface MovieMapper {

    List<Movie> selectAllMovie();

}
