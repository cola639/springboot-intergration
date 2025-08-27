package com.demo.domain;

import com.demo.annotation.Xls;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

/**
 * Department domain object
 */
@Data
@AllArgsConstructor
public class Department {
    @Xls(name = "Dept ID", order = 1)
    private Long id;

    @Xls(name = "Dept Name", order = 2, width = 20)
    private String name;

    @Xls(name = "Status", order = 3, dict = "sys_normal_disable")
    private String status;

    @Xls(name = "Created Time", order = 4, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
