package com.gavin.movie.model;

import io.swagger.v3.oas.annotations.media.Schema;


import java.io.Serializable;

/**
 * @author: zjl
 * @datetime: 2024/3/26
 * @desc:
 */
@Schema(description= "学生信息")
public class SwaggerApiModel implements Serializable {

    @Schema(description = "主键ID", required = true, example = "1")
    private Long id;

    @Schema(description = "手机号", required = true)
    private String phonenum;

    @Schema(description = "密码", required = true)
    private String password;

    @Schema(description = "年龄", required = true)
    private Integer age;

}
