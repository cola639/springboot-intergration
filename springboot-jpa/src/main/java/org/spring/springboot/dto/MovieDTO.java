package org.spring.springboot.dto;

import lombok.Data;

@Data
public class MovieDTO {

    private Long movieId;
    private String movieName;
    private String movieType;


    public MovieDTO(Long movieId, String movieName, String movieType) {
        this.movieId = movieId;
        this.movieName = movieName;
        this.movieType = movieType;
    }
}

