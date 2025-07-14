package org.spring.springboot.controller;

import org.spring.springboot.config.ExcelColumnConfig;
import org.spring.springboot.domain.UserVO;
import org.spring.springboot.utils.ExcelExportUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/excel")
public class ExcelExportController {

    @GetMapping("/multiSheet")
    public void exportMultiSheet(HttpServletResponse response) throws IOException {

        List<UserVO> sheet1 = Arrays.asList(UserVO.builder().id(1L).name("张三").status("Yes").score(90.0).remark("优秀").build(), UserVO.builder().id(2L).name("李四").status("No").score(60.5).remark("待提升").build());

        List<UserVO> sheet2 = Arrays.asList(UserVO.builder().id(1L).name("王五").status("Yes").score(90.0).remark("优秀").build(), UserVO.builder().id(2L).name("赵六").status("No").score(60.5).remark("待提升").build());
        Map<String, List<UserVO>> sheetMap = new LinkedHashMap<>();
        sheetMap.put("一班", sheet1);
        sheetMap.put("二班", sheet2);

        List<ExcelColumnConfig> columns = Arrays.asList(ExcelColumnConfig.builder().field("id").title("ID").styleKey("normal").colSpan(1).build(), ExcelColumnConfig.builder().field("name").title("姓名").styleKey("normal").colSpan(1).build(), ExcelColumnConfig.builder().field("status").title("状态").styleKey("normal").colSpan(1).build(), ExcelColumnConfig.builder().field("score").title("分数").styleKey("normal").colSpan(1).build(), ExcelColumnConfig.builder().field("remark").title("备注").styleKey("normal").colSpan(1).build());

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=multi_sheet.xlsx");

        new ExcelExportUtil().exportMultiSheet(sheetMap, columns, response.getOutputStream());
    }
}

