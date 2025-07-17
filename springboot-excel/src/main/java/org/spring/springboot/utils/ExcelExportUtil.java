package org.spring.springboot.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.spring.springboot.config.ExcelColumnConfig;
import org.spring.springboot.config.StyleParam;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class ExcelExportUtil {

    public <T> void exportSheet(String sheetName, List<T> dataList, List<ExcelColumnConfig> columns, OutputStream out) throws IOException {
        Workbook wb = new XSSFWorkbook();
        ExcelStyleFactory styleFactory = new ExcelStyleFactory();

        Sheet sheet = wb.createSheet(sheetName);

        // 设置列宽
        for (int c = 0; c < columns.size(); c++) {
            ExcelColumnConfig col = columns.get(c);
            if (col.getStyleParam() != null && col.getStyleParam().getColWidth() != null) {
                sheet.setColumnWidth(c, col.getStyleParam().getColWidth() * 256);
            } else {
                sheet.autoSizeColumn(c);
            }
        }

        // 写表头
        Row header = sheet.createRow(0);
        for (int c = 0; c < columns.size(); c++) {
            ExcelColumnConfig col = columns.get(c);
            Cell cell = header.createCell(c);
            cell.setCellValue(col.getTitle());
            cell.setCellStyle(styleFactory.get(col.getStyleParam(), wb));
        }

        // 写内容
        for (int r = 0; r < dataList.size(); r++) {
            Row row = sheet.createRow(r + 1);
            for (int c = 0; c < columns.size(); c++) {
                ExcelColumnConfig col = columns.get(c);
                Object value = getProperty(dataList.get(r), col.getField());
                Cell cell = row.createCell(c);
                String cellValue = value == null ? "" : value.toString();

                // 按值动态样式优先
                StyleParam styleParam = null;
                if (col.getValueStyleMapper() != null) {
                    styleParam = col.getValueStyleMapper().apply(value);
                }
                if (styleParam == null) styleParam = col.getStyleParam();

                cell.setCellValue(cellValue);
                cell.setCellStyle(styleFactory.get(styleParam, wb));
            }
        }

        wb.write(out);
        wb.close();
    }

    // 反射取字段值
    private Object getProperty(Object obj, String field) {
        try {
            PropertyDescriptor pd = new PropertyDescriptor(field, obj.getClass());
            return pd.getReadMethod().invoke(obj);
        } catch (Exception e) {
            return null;
        }
    }
}
