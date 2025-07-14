package org.spring.springboot.utils;

import org.apache.poi.ss.usermodel.*;

import java.util.HashMap;
import java.util.Map;

public class ExcelStyleFactory {
    private final Map<String, CellStyle> styleMap = new HashMap<>();

    public void initStyles(Workbook wb) {
        // 表头样式
        CellStyle header = wb.createCellStyle();
        Font headerFont = wb.createFont();
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.WHITE.getIndex());
        headerFont.setFontHeightInPoints((short) 14);
        header.setFont(headerFont);
        header.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        header.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        header.setAlignment(HorizontalAlignment.CENTER);
        styleMap.put("header", header);

        // 斑马线-偶数行
        CellStyle evenRow = wb.createCellStyle();
        evenRow.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        evenRow.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styleMap.put("evenRow", evenRow);

        // 斑马线-奇数行
        CellStyle oddRow = wb.createCellStyle();
        oddRow.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        oddRow.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styleMap.put("oddRow", oddRow);

        // 其它样式可扩展...
    }

    public CellStyle get(String key, Workbook wb) {
        return styleMap.getOrDefault(key, wb.createCellStyle());
    }
}

