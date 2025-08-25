package com.demo.annotation;

import java.lang.annotation.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Xls {
    /** 表头显示名 */
    String name();
    /** 排序（越小越靠前） */
    int order() default Integer.MAX_VALUE;
    /** 列宽（字符数） */
    int width() default 16;
    /** 日期格式（如 "yyyy-MM-dd"） */
    String dateFormat() default "";
    /** 值转换表达式（如 "0=禁用,1=正常"） */
    String converterExp() default "";
    /** 字典类型（可选；配合 DictProvider 使用） */
    String dict() default "";
    /** 水平对齐（可选） */
    HorizontalAlignment align() default HorizontalAlignment.CENTER;
    /** 表头背景色（可选） */
    IndexedColors headerBg() default IndexedColors.GREY_50_PERCENT;
    /** 表头字体色（可选） */
    IndexedColors headerFont() default IndexedColors.WHITE;
}
