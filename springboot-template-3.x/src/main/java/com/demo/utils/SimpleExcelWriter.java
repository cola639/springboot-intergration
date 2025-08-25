package com.demo.utils;

import com.demo.annotation.Xls;
import com.demo.utils.DictProvider;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;


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
            Cell c = header.createCell(i);
            c.setCellValue(cols.get(i).name);
            c.setCellStyle(styles.header.get(i)); // 每列可有不同表头色
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
                // 仅做字符串写入（最简）；也可按需判断写 number
                cell.setCellStyle(styles.data.get(i));
                cell.setCellValue(str);
            }
        }
    }

    private static class Styles {
        List<CellStyle> header;
        List<CellStyle> data;
    }

    private static Styles buildStyles(Workbook wb, List<Col> cols) {
        Styles s = new Styles();
        s.header = new ArrayList<>(cols.size());
        s.data   = new ArrayList<>(cols.size());

        for (Col c : cols) {
            // header
            CellStyle h = wb.createCellStyle();
            h.setAlignment(HorizontalAlignment.CENTER);
            h.setVerticalAlignment(VerticalAlignment.CENTER);
            h.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            h.setFillForegroundColor(c.headerBg.getIndex());
            setBorder(h);

            Font hf = wb.createFont();
            hf.setBold(true);
            hf.setColor(c.headerFont.getIndex());
            hf.setFontName("Arial");
            hf.setFontHeightInPoints((short)10);
            h.setFont(hf);

            // data
            CellStyle d = wb.createCellStyle();
            d.setAlignment(c.align);
            d.setVerticalAlignment(VerticalAlignment.CENTER);
            setBorder(d);
            Font df = wb.createFont();
            df.setFontName("Arial");
            df.setFontHeightInPoints((short)10);
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
        int    width;
        String dateFormat;
        String converterExp;
        String dictType;
        HorizontalAlignment align;
        IndexedColors headerBg;
        IndexedColors headerFont;
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
                    c.name  = x.name();
                    c.width = x.width();
                    c.dateFormat = x.dateFormat();
                    c.converterExp = x.converterExp();
                    c.dictType = x.dict();
                    c.align = x.align();
                    c.headerBg = x.headerBg();
                    c.headerFont = x.headerFont();
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

    private static String formatValue(Object val, Col col, DictProvider dictProvider) {
        if (val == null) return "";
        // 1) 日期
        if (!col.dateFormat.isEmpty()) {
            Date date = null;
            if (val instanceof Date) {
                date = (Date) val;
            } else if (val instanceof java.time.LocalDateTime) {
                date = java.util.Date.from(((java.time.LocalDateTime) val)
                        .atZone(java.time.ZoneId.systemDefault()).toInstant());
            } else if (val instanceof java.time.LocalDate) {
                date = java.util.Date.from(((java.time.LocalDate) val)
                        .atStartOfDay(java.time.ZoneId.systemDefault()).toInstant());
            }
            if (date != null) {
                return new SimpleDateFormat(col.dateFormat).format(date);
            }
            // 若给了 dateFormat 但不是日期，退化为 toString
            return String.valueOf(val);
        }

        String s = String.valueOf(val);

        // 2) converterExp: "0=禁用,1=正常"
        if (!col.converterExp.isEmpty()) {
            String mapped = mapByExp(s, col.converterExp);
            if (mapped != null) return mapped;
        }

        // 3) 字典
        if (!col.dictType.isEmpty() && dictProvider != null) {
            String label = dictProvider.getLabel(col.dictType, s);
            if (label != null) return label;
        }

        // 4) BigDecimal 去除多余小数（可选）
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
}
