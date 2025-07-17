package org.spring.springboot.config;

import lombok.Builder;
import lombok.Data;

import java.util.function.Function;

@Data
@Builder
public class ExcelColumnConfig {
    private String field;
    private String title;
    private StyleParam styleParam;
    private Function<Object, StyleParam> valueStyleMapper; // 新增：根据值动态返回StyleParam
    private int colSpan;
}
