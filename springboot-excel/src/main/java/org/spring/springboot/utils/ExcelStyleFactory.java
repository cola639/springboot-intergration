package org.spring.springboot.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.spring.springboot.config.StyleParam;

@Slf4j
public class ExcelStyleFactory {
    public CellStyle get(StyleParam param, Workbook wb) {
        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();

        if (param != null) {
            if (param.getFontSize() != null) font.setFontHeightInPoints(param.getFontSize().shortValue());
            if (param.getFontFamily() != null) font.setFontName(param.getFontFamily());
            if (Boolean.TRUE.equals(param.getBold())) font.setBold(true);
            if (param.getFontColor() != null) font.setColor(param.getFontColor());

            // 1. 先判断 Hex 优先
            if (wb instanceof XSSFWorkbook && param.getBackgroundHex() != null && !param.getBackgroundHex().isEmpty()) {
                byte[] rgb = hexToRgb(param.getBackgroundHex());
                XSSFCellStyle xssfStyle = (XSSFCellStyle) style;
                XSSFColor color = new XSSFColor(rgb, null);
                xssfStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                xssfStyle.setFillForegroundColor(color);
                log.debug("Set cell background with hex: {}, rgb: {}", param.getBackgroundHex(), rgb);
            }
            // 2. 其次判断 backgroundRgb
            else if (wb instanceof XSSFWorkbook && param.getBackgroundRgb() != null) {
                XSSFCellStyle xssfStyle = (XSSFCellStyle) style;
                XSSFColor color = new XSSFColor(param.getBackgroundRgb(), null);
                xssfStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                xssfStyle.setFillForegroundColor(color);
                log.debug("Set cell background with RGB: {}", param.getBackgroundRgb());
            }
            // 3. 再判断 short色板
            else if (param.getBackgroundColor() != null) {
                style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                style.setFillForegroundColor(param.getBackgroundColor());
                log.debug("Set cell background with palette color: {}", param.getBackgroundColor());
            }
        }
        style.setFont(font);
        return style;
    }

    /**
     * Convert hex color string to rgb byte array
     * Support: #fff, #ffffff, fff, ffffff (大小写均可)
     */
    private byte[] hexToRgb(String hex) {
        String colorStr = hex.replace("#", "");
        if (colorStr.length() == 3) {
            // #fff -> #ffffff
            colorStr = "" + colorStr.charAt(0) + colorStr.charAt(0)
                    + colorStr.charAt(1) + colorStr.charAt(1)
                    + colorStr.charAt(2) + colorStr.charAt(2);
        }
        if (colorStr.length() != 6) {
            throw new IllegalArgumentException("Hex color must be 3 or 6 chars: " + hex);
        }
        int r = Integer.parseInt(colorStr.substring(0, 2), 16);
        int g = Integer.parseInt(colorStr.substring(2, 4), 16);
        int b = Integer.parseInt(colorStr.substring(4, 6), 16);
        return new byte[]{(byte) r, (byte) g, (byte) b};
    }
}

