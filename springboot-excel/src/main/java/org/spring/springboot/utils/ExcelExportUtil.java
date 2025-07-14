package org.spring.springboot.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.spring.springboot.config.ExcelColumnConfig;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

public class ExcelExportUtil {

    public <T> void exportMultiSheet(Map<String, List<T>> sheetDataMap, // 多sheet数据
                                     List<ExcelColumnConfig> columnConfigList, OutputStream out) throws IOException {
        Workbook wb = new XSSFWorkbook();
        ExcelStyleFactory styleFactory = new ExcelStyleFactory();
        styleFactory.initStyles(wb);

        int sheetIdx = 0;
        for (Map.Entry<String, List<T>> entry : sheetDataMap.entrySet()) {
            Sheet sheet = wb.createSheet(entry.getKey());

            // 写表头，合并单元格举例
            Row header = sheet.createRow(0);
            for (int i = 0, colIdx = 0; i < columnConfigList.size(); i++) {
                ExcelColumnConfig col = columnConfigList.get(i);
                Cell cell = header.createCell(colIdx);
                cell.setCellValue(col.getTitle());
                cell.setCellStyle(styleFactory.get("header", wb));
                if (col.getColSpan() > 1) {
                    sheet.addMergedRegion(new CellRangeAddress(0, 0, colIdx, colIdx + col.getColSpan() - 1));
                    colIdx += col.getColSpan();
                } else {
                    colIdx++;
                }
            }

            // 写内容（斑马线样式）
            List<T> dataList = entry.getValue();
            for (int r = 0; r < dataList.size(); r++) {
                Row row = sheet.createRow(r + 1);
                T data = dataList.get(r);
                CellStyle rowStyle = styleFactory.get((r % 2 == 0) ? "evenRow" : "oddRow", wb);

                for (int c = 0; c < columnConfigList.size(); c++) {
                    ExcelColumnConfig col = columnConfigList.get(c);
                    Object value = getProperty(data, col.getField());
                    Cell cell = row.createCell(c);
                    String val = col.getFormatter() == null ? (value == null ? "" : value.toString()) : col.getFormatter().apply(value);
                    cell.setCellValue(val);
                    cell.setCellStyle(rowStyle);
                }
            }

            // 自动列宽
            for (int c = 0; c < columnConfigList.size(); c++) {
                sheet.autoSizeColumn(c);
            }
        }
        wb.write(out);
        wb.close();
    }

    private Object getProperty(Object obj, String field) {
        try {
            PropertyDescriptor pd = new PropertyDescriptor(field, obj.getClass());
            return pd.getReadMethod().invoke(obj);
        } catch (Exception e) {
            return null;
        }
    }
}

