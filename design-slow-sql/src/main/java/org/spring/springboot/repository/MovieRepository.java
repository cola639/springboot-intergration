package org.spring.springboot.repository;

import org.spring.springboot.domain.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {


    Movie findByMovieUuid(String movieUuid);
}
