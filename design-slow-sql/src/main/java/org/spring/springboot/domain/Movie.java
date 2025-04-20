package org.spring.springboot.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
public class Movie {
    @Id
    private Long movieId;

    private String movieName;
    private String movieUuid;
    private Date createTime;

//    public Long getMovieId() {
//        return movieId;
//    }
//
//    public String getMovieName() {
//        return movieName;
//    }
//
//    public String getMovieUuid() {
//        return movieUuid;
//    }
//
//    public Date getCreateTime() {
//        return createTime;
//    }
//
//    public String toString() {
//        return "Movie{" +
//                "movieId=" + movieId +
//                ", movieName='" + movieName + '\'' +
//                ", movieUuid='" + movieUuid + '\'' +
//                ", createTime=" + createTime +
//                '}';
//    }
}