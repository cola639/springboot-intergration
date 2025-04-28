package org.spring.springboot.service;

import org.spring.springboot.domain.Actors;
import org.spring.springboot.dto.ActorDTO;
import org.spring.springboot.dto.MovieDTO;
import org.spring.springboot.repository.ActorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ActorService {

    @Autowired
    private ActorRepository actorRepository;

    // 根据演员ID查找演员及出演的电影
    public ActorDTO getActorWithMovies(Long actorId) {
        Actors actor = actorRepository.findById(actorId)
                .orElseThrow(() -> new RuntimeException("Actor not found with ID: " + actorId));

        // 将 Actor 实体转换为 ActorDTO
        List<MovieDTO> movieDTOs = actor.getMovies().stream()
                .map(movie -> new MovieDTO(movie.getMovieId(), movie.getMovieName(), movie.getMovieType()))
                .collect(Collectors.toList());

        return new ActorDTO(actor.getId(), actor.getName(), movieDTOs);
    }
}

