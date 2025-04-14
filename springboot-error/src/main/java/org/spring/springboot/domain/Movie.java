package org.spring.springboot.domain;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Entity
@Table(name = "movies")
public class Movie {

    @Id  // 指定主键
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // 主键生成策略（这里使用数据库自增策略）
    @Column(name = "movie_id") // ✅ 改这里为数据库实际主键字段名
    private Long movieId;

    @NotNull(message = "{movie.name.notnull}")
    @Column(name = "movie_name", nullable = false)  // 映射数据库字段，指定字段名和非空约束
    private String movieName;


    @Column(name = "movie_description")  // 映射到数据库字段，类型为 TEXT
    private String movieDescription;

    @Column(name = "movie_cover")
    private String movieCover;

    @NotNull(message = "Movie name cannot be null")
    @Column(name = "movie_score")
    private Float movieScore;

    @Column(name = "movie_rates")
    private String movieRates;

    @Column(name = "movie_status")
    private Integer movieStatus;

    @Column(name = "movie_type")
    private String movieType;

    @Column(name = "movie_duration")
    private Integer movieDuration;

    @Column(name = "movie_views")
    private Integer movieViews;

    @Column(name = "movie_comments")
    private Integer movieComments;

    @Column(name = "movie_likes")
    private Integer movieLikes;

    @Column(name = "movie_dislikes")
    private Integer movieDislikes;

    @Column(name = "movie_downloads")
    private Integer movieDownloads;

    @Column(name = "movie_download_url")
    private String movieDownloadUrl;

    @Column(name = "movie_download_count")
    private Integer movieDownloadCount;

    @Temporal(TemporalType.TIMESTAMP)  // 映射为数据库的时间戳
    @Column(name = "movie_time")
    private Date movieTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "movie_release_time")
    private Date movieReleaseTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_time", nullable = false)
    private Date createTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "update_time", nullable = false)
    private Date updateTime;

    @Column(name = "remarks")
    private String remarks;

}
