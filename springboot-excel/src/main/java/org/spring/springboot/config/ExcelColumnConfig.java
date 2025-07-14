package org.spring.springboot.config;

import lombok.Builder;
import lombok.Data;

import java.util.function.Function;

@Data
@Builder
public class ExcelColumnConfig {
    private String field;         // 字段名
    private String title;         // 表头
    private String styleKey;      // 样式key
    private Function<Object, String> formatter;
    private int colSpan;          // 合并多少列（如有合并单元格）
}