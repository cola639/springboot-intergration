package com.springMovie.domain;

import lombok.Data;

import java.util.Date;

@Data
public class Director {
    private Integer directorId;
    private String directorName;
    private String directorDescription;
    private String directorImage;
    private String directorAddress;
    private Date directorBirthday;
    private String directorGender;
    private String directorMovies;
    private Date createTime;
    private Date updateTime;
    private Integer directorStatus;
    private String remarks;
}