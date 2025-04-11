package org.spring.springboot.utils;


import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.spring.springboot.domain.Movie;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Stream;

public class PdfUtil {

    public static void exportMovieListPdf(List<Movie> movieList, OutputStream outputStream) throws DocumentException, IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter writer = PdfWriter.getInstance(document, outputStream);
        document.open();

        // 标题
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
        Paragraph title = new Paragraph("电影导出列表", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(new Paragraph("\n"));

        // 表格
        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);

        // 表头
        Stream.of("电影名称", "类型", "上映时间").forEach(columnTitle -> {
            PdfPCell header = new PdfPCell();
            header.setPhrase(new Phrase(columnTitle));
            table.addCell(header);
        });

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (Movie movie : movieList) {
            table.addCell(movie.getMovieName());
            table.addCell(movie.getMovieType());
            table.addCell(sdf.format(movie.getMovieTime()));
        }

        document.add(table);

        // 加盖章（右下角）
        Image stamp = Image.getInstance(PdfUtil.class.getClassLoader().getResource("static/stamp.png"));
        stamp.scaleAbsolute(120, 120);
        stamp.setAbsolutePosition(400, 100); // 控制位置（可调）
        document.add(stamp);

        document.close();
        writer.close();
    }
}
