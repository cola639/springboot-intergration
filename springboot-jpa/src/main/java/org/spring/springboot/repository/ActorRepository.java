package org.spring.springboot.repository;

import org.spring.springboot.domain.Actors;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ActorRepository extends JpaRepository<Actors, Long> {
    // JpaRepository 提供的查询方法，按 id 查找演员

    @Query("SELECT a FROM Actors a LEFT JOIN FETCH a.movies WHERE a.id = :id")
    Optional<Actors> findByIdWithMovies(@Param("id") Long id);
}
