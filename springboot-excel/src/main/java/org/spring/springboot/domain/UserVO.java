package org.spring.springboot.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserVO {
    private Long id;
    private String name;
    private String status;
    private Double score;
    private String remark;
}
