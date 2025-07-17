package org.spring.springboot.config;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StyleParam {
    private String fontFamily;
    private Integer fontSize;
    private Boolean bold;
    private Short fontColor;
    private Short backgroundColor; // 老写法，继续兼容
    private byte[] backgroundRgb;  // 支持byte数组（兼容）
    private String backgroundHex;  // 新增，支持Hex格式
    private Integer colWidth;
}

