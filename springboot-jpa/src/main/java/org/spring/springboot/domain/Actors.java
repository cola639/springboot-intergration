package org.spring.springboot.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "actors")
public class Actors {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自动递增
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToMany(mappedBy = "actors")
    @JsonBackReference  // 防止循环引用，避免序列化 movies
    private List<Movie> movies;
}
