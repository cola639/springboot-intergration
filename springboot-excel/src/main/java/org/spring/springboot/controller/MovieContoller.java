package org.spring.springboot.controller;


import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.spring.springboot.domain.Movie;
import org.spring.springboot.service.IMovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/movie")
public class MovieContoller {

    @Autowired
    private IMovieService movieService;

    @GetMapping("/allMoviesFromSlave")
    public List<Movie> getAllMovie() {
        return movieService.selectAllMovieFromSlave();
    }

    @GetMapping("/allMoviesFromMater")
    public List<Movie> MoviesFromMater() {
        return movieService.selectAllMovieFromMaster();
    }


    @GetMapping("/export/poi")
    public void exportWithPoi(HttpServletResponse response) throws IOException {
        List<Movie> movies =  movieService.selectAllMovieFromSlave();

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("电影列表");

        // 表头
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("电影ID");
        header.createCell(1).setCellValue("电影名称");
        header.createCell(2).setCellValue("电影类型");
        header.createCell(3).setCellValue("上映时间");

        // 内容
        int rowIndex = 1;
        for (Movie movie : movies) {
            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(movie.getMovieId());
            row.createCell(1).setCellValue(movie.getMovieName());
            row.createCell(2).setCellValue(movie.getMovieType());}

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment;filename=movies-poi.xlsx");

        workbook.write(response.getOutputStream());
        workbook.close();
    }


}
