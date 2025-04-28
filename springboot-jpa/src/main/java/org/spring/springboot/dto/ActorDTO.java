package org.spring.springboot.dto;

import lombok.Data;

import java.util.List;

@Data
public class ActorDTO {

    private Long id;
    private String name;
    private List<MovieDTO> movies;  // ActorDTO 中嵌套 MovieDTO

    public ActorDTO(Long id, String name, List<MovieDTO> movies) {
        this.id = id;
        this.name = name;
        this.movies = movies;
    }
}
