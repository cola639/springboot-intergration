package com.demo.domain;

import com.demo.annotation.Xls;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

import java.util.Date;

public class User {
    @Xls(name = "用户ID", order = 1, align = HorizontalAlignment.RIGHT, width = 10)
    private Long userId;

    @Xls(name = "用户名", order = 2, width = 18)
    private String userName;

    @Xls(name = "性别", order = 3, converterExp = "0=女,1=男,2=未知", width = 8)
    private String sex;

    @Xls(name = "状态", order = 4, dict = "sys_normal_disable", width = 10)
    private String status;

    @Xls(name = "年龄", order = 6, dict = "sys_age_range", width = 10)
    private String ageRange;

    @Xls(name = "创建时间", order = 5, dateFormat = "yyyy-MM-dd HH:mm:ss", width = 22)
    private Date createTime;



    public User(Long userId, String userName, String sex, String status, Date createTime) {
        this.userId = userId;
        this.userName = userName;
        this.sex = sex;
        this.status = status;
        this.createTime = createTime;
    }

}
