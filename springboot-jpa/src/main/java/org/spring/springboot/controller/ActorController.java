package org.spring.springboot.controller;

import lombok.extern.slf4j.Slf4j;
import org.spring.springboot.domain.Actors;
import org.spring.springboot.dto.ActorDTO;
import org.spring.springboot.repository.ActorRepository;
import org.spring.springboot.service.ActorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/actors")
@Slf4j
public class ActorController {

    @Autowired
    private ActorRepository actorRepository;

    @Autowired
    private ActorService actorService;


    @GetMapping("/{id}")
    public ActorDTO getActorById(@PathVariable Long id) {
        // 打印接收到的请求
        log.info("Received request to get actor with ID: {}", id);

        // 获取演员及出演的电影，并返回 DTO
        return actorService.getActorWithMovies(id);
    }
}
