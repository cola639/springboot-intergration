package org.spring.springboot.controller;

import org.apache.poi.ss.usermodel.IndexedColors;
import org.spring.springboot.config.ExcelColumnConfig;
import org.spring.springboot.config.StyleParam;
import org.spring.springboot.domain.UserVO;
import org.spring.springboot.utils.ExcelExportUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/excel")
public class ExcelExportController {

    @GetMapping("/dynamic")
    public void exportDynamic(HttpServletResponse response) throws IOException {
        List<UserVO> data = Arrays.asList(UserVO.builder().id(1L).name("张三").status("1").score(90.0).remark("优秀").build(), UserVO.builder().id(2L).name("李四").status("2").score(60.5).remark("待提升").build(), UserVO.builder().id(3L).name("王五").status("3").score(75.0).remark("合格").build());

        List<ExcelColumnConfig> columns = Arrays.asList(ExcelColumnConfig.builder().field("id").title("ID").styleParam(StyleParam.builder().fontSize(14).bold(true).colWidth(6).backgroundHex("#000").build()).build(),
                ExcelColumnConfig.builder().field("name").title("姓名").styleParam(StyleParam.builder().fontFamily("Arial").fontSize(12).colWidth(10).build()).build(),
                ExcelColumnConfig.builder().field("status").title("状态")
                        // 按值变色
                        .valueStyleMapper(val -> {
                            if ("1".equals(val)) {
                                return StyleParam.builder().fontColor(IndexedColors.RED.getIndex()).bold(true).build();
                            } else if ("2".equals(val)) {
                                return StyleParam.builder().fontColor(IndexedColors.GREEN.getIndex()).bold(true).build();
                            } else {
                                return StyleParam.builder().fontColor(IndexedColors.BLACK.getIndex()).build();
                            }
                        }).colSpan(1).build(), ExcelColumnConfig.builder().field("score").title("分数").styleParam(StyleParam.builder().bold(true).colWidth(8).build()).build(), ExcelColumnConfig.builder().field("remark").title("备注").styleParam(StyleParam.builder().fontFamily("宋体").colWidth(12).build()).build());

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=dynamic_style.xlsx");
        new ExcelExportUtil().exportSheet("测试动态", data, columns, response.getOutputStream());
    }
}
