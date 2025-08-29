package com.demo.controller;

import com.demo.domain.Department;
import com.demo.domain.Student;
import com.demo.domain.User;
import com.demo.domain.Vulnerability;
import com.demo.utils.DictProvider;
import com.demo.utils.DictResult;
import com.demo.utils.MultiSheetExcelWriter;
import com.demo.utils.SimpleExcelWriter;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

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

    /** Mock departments */
    private List<Department> mockDepartments() {
        return Arrays.asList(
                new Department(100L, "研发部", "0", new Date()),
                new Department(101L, "市场部", "1", new Date()),
                new Department(102L, "人事部", "0", new Date())
        );
    }

    @GetMapping("/demo/basic")
    public void exportUsers(HttpServletResponse response) {

        DictProvider dict = (dictType, value) -> {
            if ("sys_normal_disable".equals(dictType)) {
                if ("0".equals(value)) return new DictResult("正常", "#FF0000");
                if ("1".equals(value)) return new DictResult("停用", "#00FF00");
            }
            return new DictResult(value, null);
        };

        Map<String, List<?>> dataMap = new LinkedHashMap<>();
        dataMap.put("用户列表", mockUsers());
        dataMap.put("部门列表", mockDepartments());

        Map<String, Class<?>> clazzMap = new HashMap<>();
        clazzMap.put("用户列表", User.class);
        clazzMap.put("部门列表", Department.class);

        MultiSheetExcelWriter.export(dataMap, clazzMap, response, "多Sheet导出", dict);

    }

    @GetMapping("/demo/mergeRow")
    public void exportMergeRow(HttpServletResponse response) {
        // mock 数据
        List<Student> students = Arrays.asList(
                new Student(1L, "man", "class 1"),
                new Student(2L, "man", "class 1"),
                new Student(3L, "man", "class 1"),
                new Student(4L, "man", "class 2"),
                new Student(5L, "man", "class 2")
        );

        // 空字典实现（这里不做翻译）
        DictProvider dict = (dictType, value) -> null;

        // 调用导出工具
        SimpleExcelWriter.export(students, Student.class, response,
                "学生导出", "学生列表", dict);
    }

    @GetMapping("/demo/multipleLink")
    public void exportMultipleLink(HttpServletResponse response) {
        // mock 数据
        List<Vulnerability> vulns = Arrays.asList(
                new Vulnerability(1L, "High", "http://fix1.com;http://fix2.com"),
                new Vulnerability(2L, "Medium", "http://fix3.com"),
                new Vulnerability(4L, "High", "http://patch1.com http://patch2.com"),
                new Vulnerability(5L, "Critical", "http://dbfix.com")
        );

        // 空字典实现（这里不做翻译）
        DictProvider dict = (dictType, value) -> null;

        // 导出
        SimpleExcelWriter.export(vulns, Vulnerability.class, response,
                "multipleLink", "漏洞列表", dict);
    }

    @GetMapping("/demo/simple")
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
