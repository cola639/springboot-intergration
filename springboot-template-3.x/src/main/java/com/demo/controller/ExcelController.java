package com.demo.controller;


import com.demo.domain.User;
import com.demo.utils.DictProvider;
import com.demo.utils.SimpleExcelWriter;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RestController
public class ExcelController {

    /** 模拟数据 */
    private List<User> mockUsers() {
        return Arrays.asList(
                new User(1L, "张三", "1", "0", new Date()),
                new User(2L, "李四", "0", "1", new Date()),
                new User(3L, "王五", "2", "0", new Date())
        );
    }

    /** 简单字典转换 */
    private String convertDict(String dict, String value) {
        if ("sys_normal_disable".equals(dict)) {
            return "0".equals(value) ? "正常" : "停用";
        }
        return value;
    }

    @GetMapping("/demo/users/export")
    public void exportUsers(HttpServletResponse response) {


        // 示例的字典提供者（可接 RuoYi 的 DictUtils，也可自实现）
        DictProvider dict = (dictType, value) -> {
            if ("sys_normal_disable".equals(dictType)) {
                if ("0".equals(value)) return "正常";
                if ("1".equals(value)) return "停用";
            }
            return null;
        };

        SimpleExcelWriter.export(mockUsers(), User.class, response,
                "用户导出", "用户列表", dict);
    }

    @GetMapping("/excel/download")
    public void download(HttpServletResponse response) {
        try (Workbook wb = new XSSFWorkbook()) {
            // 1) 创建 Sheet、表头
            Sheet sheet = wb.createSheet("示例Sheet");
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("ID");
            header.createCell(1).setCellValue("姓名");
            header.createCell(2).setCellValue("年龄");

            // 2) 简单数据
            Row row1 = sheet.createRow(1);
            row1.createCell(0).setCellValue(1);
            row1.createCell(1).setCellValue("张三");
            row1.createCell(2).setCellValue(20);

            Row row2 = sheet.createRow(2);
            row2.createCell(0).setCellValue(2);
            row2.createCell(1).setCellValue("李四");
            row2.createCell(2).setCellValue(22);

            // 可选：自动列宽
            for (int i = 0; i < 3; i++) sheet.autoSizeColumn(i);

            // 3) 设置响应头并写出
            String fileName = URLEncoder.encode("demo.xlsx", StandardCharsets.UTF_8).replace("+", "%20");
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + fileName);
            wb.write(response.getOutputStream());
            // try-with-resources 会自动关闭 wb
        } catch (Exception e) {
            throw new RuntimeException("导出失败", e);
        }
    }
}
