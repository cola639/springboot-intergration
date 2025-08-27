package com.demo.utils;

import com.demo.annotation.Xls;
import com.demo.annotation.XlsRichText;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;

import java.io.OutputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class SimpleExcelWriter {

    /** 导出入口（写到响应） */
    public static <T> void export(List<T> data, Class<T> clazz, HttpServletResponse resp,
                                  String fileName, String sheetName, DictProvider dictProvider) {
        resp.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        resp.setCharacterEncoding("utf-8");
        try (SXSSFWorkbook wb = new SXSSFWorkbook(200)) {
            writeWorkbook(wb, data, clazz, sheetName, dictProvider);

            String encoded = URLEncoder.encode(fileName, StandardCharsets.UTF_8.name()).replace("+", "%20");
            resp.setHeader("Content-Disposition", "attachment;filename*=utf-8''" + encoded + ".xlsx");
            wb.write(resp.getOutputStream());
        } catch (Exception e) {
            throw new RuntimeException("导出失败", e);
        }
    }

    /** 导出入口（写到任意输出流） */
    public static <T> void export(List<T> data, Class<T> clazz, OutputStream os,
                                  String sheetName, DictProvider dictProvider) {
        try (SXSSFWorkbook wb = new SXSSFWorkbook(200)) {
            writeWorkbook(wb, data, clazz, sheetName, dictProvider);
            wb.write(os);
        } catch (Exception e) {
            throw new RuntimeException("导出失败", e);
        }
    }

    /* ================== 内部实现 ================== */

    private static <T> void writeWorkbook(Workbook wb, List<T> data, Class<T> clazz,
                                          String sheetName, DictProvider dictProvider) throws Exception {
        if (data == null) data = Collections.emptyList();
        Sheet sheet = wb.createSheet(sheetName == null ? "Sheet1" : sheetName);

        // 1) 解析字段元数据
        List<Col> cols = parseCols(clazz);

        // 2) 样式
        Styles styles = buildStyles(wb, cols);

        // 3) 表头
        Row header = sheet.createRow(0);
        for (int i = 0; i < cols.size(); i++) {
            Cell cell = header.createCell(i);
            Col col = cols.get(i);

            if (col.headerRichTexts != null && col.headerRichTexts.length > 0) {
                // ========== 构建 RichText ==========
                XSSFRichTextString rich = new XSSFRichTextString();
                for (XlsRichText rt : col.headerRichTexts) {
                    XSSFFont xf = (XSSFFont) wb.createFont();
                    xf.setFontName(col.headerFontFamily != null ? col.headerFontFamily : "Arial");
                    xf.setFontHeightInPoints(rt.fontSize());
                    xf.setBold(rt.bold());
                    xf.setItalic(rt.italic());

                    // 设置颜色
                    if (rt.color().startsWith("#") && rt.color().length() == 7) {
                        java.awt.Color awtColor = new java.awt.Color(
                                Integer.valueOf(rt.color().substring(1, 3), 16),
                                Integer.valueOf(rt.color().substring(3, 5), 16),
                                Integer.valueOf(rt.color().substring(5, 7), 16)
                        );
                        XSSFColor poiColor = new XSSFColor(awtColor, null);
                        xf.setColor(poiColor);
                    }

                    // 拼接 marginRight（追加空格或全角空格）
                    StringBuilder textWithMargin = new StringBuilder(rt.text());
                    if (rt.marginRight() > 0) {
                        for (int j = 0; j < rt.marginRight(); j++) {
                            textWithMargin.append(" "); // 或 "\u2003" 全角空格
                        }
                    }

                    rich.append(textWithMargin.toString(), xf);
                }

                cell.setCellValue(rich);

                // ========== 应用 header 样式（但不覆盖字体） ==========
                CellStyle baseStyle = wb.createCellStyle();
                baseStyle.cloneStyleFrom(styles.header.get(i));
                baseStyle.setFont(null); // ⚠️ 移除原字体，避免覆盖 RichText 的 XSSFFont
                cell.setCellStyle(baseStyle);

            } else {
                // 普通表头
                cell.setCellValue(col.name);
                cell.setCellStyle(styles.header.get(i));
            }

            // 设置列宽
            sheet.setColumnWidth(i, (int) ((cols.get(i).width + 0.72) * 256));
        }

        // 4) 数据
        int r = 1;
        for (T rowObj : data) {
            Row row = sheet.createRow(r++);
            for (int i = 0; i < cols.size(); i++) {
                Cell cell = row.createCell(i);
                Col col = cols.get(i);
                Object raw = fieldValue(rowObj, col.field);
                String str = formatValue(raw, col, dictProvider);

                // 默认样式
                CellStyle cellStyle = styles.data.get(i);

                // extra: set dynamic color if available
                String hexColor = resolveColor(raw, col, dictProvider);
                if (hexColor != null) {
                    if (hexColor.startsWith("#")) {
                        hexColor = hexColor.substring(1);
                    }
                    if (hexColor.length() == 6) { // 安全校验
                        java.awt.Color awtColor = new java.awt.Color(
                                Integer.valueOf(hexColor.substring(0, 2), 16),
                                Integer.valueOf(hexColor.substring(2, 4), 16),
                                Integer.valueOf(hexColor.substring(4, 6), 16)
                        );
                        XSSFColor poiColor = new XSSFColor(awtColor, null);
                        CellStyle colored = wb.createCellStyle();
                        colored.cloneStyleFrom(styles.data.get(i));
                        ((XSSFCellStyle) colored).setFillForegroundColor(poiColor);
                        colored.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                        cellStyle = colored; // 使用动态颜色 style
                    }
                }

                // 应用最终样式 + 值
                cell.setCellStyle(cellStyle);
                cell.setCellValue(str);
            }



        }

        int lastRow = sheet.getLastRowNum();
        int lastCol = cols.size() - 1;

        fillRemaining(wb, sheet, lastRow, lastCol);
    }

    private static class Styles {
        List<CellStyle> header;
        List<CellStyle> data;
    }

    private static Styles buildStyles(Workbook wb, List<Col> cols) {
        Styles s = new Styles();
        s.header = new ArrayList<>(cols.size());
        s.data = new ArrayList<>(cols.size());

        for (Col c : cols) {
            // header
            CellStyle h = wb.createCellStyle();
            h.setAlignment(HorizontalAlignment.CENTER);
            h.setVerticalAlignment(VerticalAlignment.CENTER);
            h.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            h.setFillForegroundColor(c.headerBg.getIndex());
            setBorder(h);

            Font hf = wb.createFont();
            hf.setBold(c.headerBold);
            hf.setColor(c.headerFont.getIndex());
            hf.setFontName(c.headerFontFamily != null ? c.headerFontFamily : "Arial");
            hf.setFontHeightInPoints(c.headerFontSize);
            h.setFont(hf);

            // data
            CellStyle d = wb.createCellStyle();
            d.setAlignment(c.align);
            d.setVerticalAlignment(VerticalAlignment.CENTER);
            setBorder(d);
            Font df = wb.createFont();
            df.setFontName(c.fontFamily);
            df.setFontHeightInPoints(c.fontSize);
            df.setBold(c.bold);
            d.setFont(df);

            s.header.add(h);
            s.data.add(d);
        }
        return s;
    }

    private static void setBorder(CellStyle cs) {
        cs.setBorderBottom(BorderStyle.THIN);
        cs.setBorderTop(BorderStyle.THIN);
        cs.setBorderLeft(BorderStyle.THIN);
        cs.setBorderRight(BorderStyle.THIN);
        cs.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        cs.setTopBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        cs.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        cs.setRightBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
    }

    /* ---------- 字段解析与值格式化 ---------- */
    private static class Col {
        Field field;
        String name;
        int width;
        String dateFormat;
        String converterExp;
        String dictType;
        HorizontalAlignment align;
        IndexedColors headerBg;
        IndexedColors headerFont;
        // extra config
        short fontSize;
        boolean bold;
        String fontFamily;
        short headerFontSize;
        boolean headerBold;
        String headerFontFamily;
        XlsRichText[] headerRichTexts;
    }

    private static <T> List<Col> parseCols(Class<T> clazz) {
        List<Field> all = new ArrayList<>();
        if (clazz.getSuperclass() != null) {
            all.addAll(Arrays.asList(clazz.getSuperclass().getDeclaredFields()));
        }
        all.addAll(Arrays.asList(clazz.getDeclaredFields()));

        return all.stream()
                .filter(f -> f.isAnnotationPresent(Xls.class))
                .sorted(Comparator.comparingInt(f -> f.getAnnotation(Xls.class).order()))
                .map(f -> {
                    Xls x = f.getAnnotation(Xls.class);
                    Col c = new Col();
                    c.field = f;
                    c.name = x.name();
                    c.width = x.width();
                    c.dateFormat = x.dateFormat();
                    c.converterExp = x.converterExp();
                    c.dictType = x.dict();
                    c.align = x.align();
                    c.headerBg = x.headerBg();
                    c.headerFont = x.headerFont();
                    c.fontSize = x.fontSize();
                    c.bold = x.bold();
                    c.fontFamily = x.fontFamily();
                    c.headerFontSize = x.headerFontSize();
                    c.headerBold = x.headerBold();
                    c.headerFontFamily = x.headerFontFamily();
                    c.headerRichTexts = x.headerRichText();
                    return c;
                })
                .collect(Collectors.toList());
    }

    private static Object fieldValue(Object obj, Field f) {
        try {
            f.setAccessible(true);
            return f.get(obj);
        } catch (Exception e) {
            return null;
        }
    }

    private static String resolveColor(Object val, Col col, DictProvider dictProvider) {
        if (val == null) return null;
        if (!col.dictType.isEmpty() && dictProvider != null) {
            DictResult dr = dictProvider.getDict(col.dictType, String.valueOf(val));
            if (dr != null) {
                return dr.getColor();
            }
        }
        return null;
    }

    private static String formatValue(Object val, Col col, DictProvider dictProvider) {
        if (val == null) return "";
        String s = String.valueOf(val);

        // 1) 日期格式
        if (!col.dateFormat.isEmpty()) {
            if (val instanceof Date) {
                return new SimpleDateFormat(col.dateFormat).format((Date) val);
            }
            return s;
        }

        // 2) converterExp
        if (!col.converterExp.isEmpty()) {
            String mapped = mapByExp(s, col.converterExp);
            if (mapped != null) return mapped;
        }

        // 3) dict
        if (!col.dictType.isEmpty() && dictProvider != null) {
            DictResult dr = dictProvider.getDict(col.dictType, s);
            if (dr != null && dr.getLabel() != null) {
                return dr.getLabel();
            }
        }

        // 4) BigDecimal 去除多余小数
        if (val instanceof BigDecimal) {
            return ((BigDecimal) val).stripTrailingZeros().toPlainString();
        }

        return s;
    }

    private static String mapByExp(String value, String exp) {
        // 形如 "0=禁用,1=正常"
        String[] pairs = exp.split(",");
        for (String p : pairs) {
            String[] kv = p.split("=");
            if (kv.length == 2 && Objects.equals(kv[0], value)) {
                return kv[1];
            }
        }
        return null;
    }

    /* ---------- 合并行列 ---------- */
    private static void fillRemaining(Workbook wb, Sheet sheet, int lastDataRow, int lastDataCol) {
        // 我们限定 200 行 × 50 列 的区域，避免 SXSSF 崩溃
        int targetRow = lastDataRow + 200;  // 向下扩展 200 行
        int targetCol = lastDataCol + 50;   // 向右扩展 50 列

        // 白色背景样式
        CellStyle whiteStyle = wb.createCellStyle();
        whiteStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        whiteStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // === 1) 右侧空白列合并 ===
        if (targetCol > lastDataCol + 1) {
            CellRangeAddress rightRegion = new CellRangeAddress(
                    0, lastDataRow,           // 从表头到最后一行数据
                    lastDataCol + 1, targetCol
            );
            sheet.addMergedRegion(rightRegion);

            Row row = sheet.getRow(0);
            if (row == null) row = sheet.createRow(0);
            Cell cell = row.createCell(lastDataCol + 1);
            cell.setCellStyle(whiteStyle);
        }

        // === 2) 底部空白行合并 ===
        if (targetRow > lastDataRow + 1) {
            CellRangeAddress bottomRegion = new CellRangeAddress(
                    lastDataRow + 1, targetRow,
                    0, lastDataCol
            );
            sheet.addMergedRegion(bottomRegion);

            Row row = sheet.createRow(lastDataRow + 1);
            Cell cell = row.createCell(0);
            cell.setCellStyle(whiteStyle);
        }

        // === 3) 右下角整块空白合并 ===
        CellRangeAddress cornerRegion = new CellRangeAddress(
                lastDataRow + 1, targetRow,
                lastDataCol + 1, targetCol
        );
        sheet.addMergedRegion(cornerRegion);

        Row cornerRow = sheet.createRow(lastDataRow + 1);
        Cell cornerCell = cornerRow.createCell(lastDataCol + 1);
        cornerCell.setCellStyle(whiteStyle);
    }
}
