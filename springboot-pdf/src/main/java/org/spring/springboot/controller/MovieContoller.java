package org.spring.springboot.controller;


import com.itextpdf.text.DocumentException;
import org.spring.springboot.domain.Movie;
import org.spring.springboot.service.IMovieService;
import org.spring.springboot.utils.PdfUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
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

    @GetMapping("/export/pdf")
    public void exportPdf(HttpServletResponse response) throws IOException, DocumentException {
        List<Movie> movies = movieService.selectAllMovieFromMaster();

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment;filename=movies.pdf");

        OutputStream out = response.getOutputStream();
        PdfUtil.exportMovieListPdf(movies, out);
        out.flush();
    }
}
