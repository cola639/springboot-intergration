package com.demo.domain;

import com.demo.annotation.Xls;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Student {
    @Xls(name = "ID", order = 1)
    private Long id;

    @Xls(name = "Gender", order = 2)
    private String gender;

    @Xls(name = "Class", order = 3, merge = true) // 👈 自动合并相同班级
    private String className;
}
