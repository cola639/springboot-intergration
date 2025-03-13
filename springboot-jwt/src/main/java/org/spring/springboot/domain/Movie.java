package org.spring.springboot.domain;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import java.util.Date;

@Data
public class Movie {

    private Long movieId;

    private String movieName;

    private String movieDescription;

    private String movieCover;

    private Float movieScore;

    private String movieRates;

    private Integer movieStatus;

    private String movieType;

    private Integer movieDuration;

    private Integer movieViews;

    private Integer movieComments;

    private Integer movieLikes;

    private Integer movieDislikes;

    private Integer movieDownloads;

    private String movieDownloadUrl;

    private Integer movieDownloadCount;

    private Date movieTime;

    private Date movieReleaseTime;

    private Date createTime;

    private Date updateTime;

    private String remarks;
    
}
