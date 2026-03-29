package com.airesume.builder.export;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.stereotype.Service;

@Service
public class ExportService {

  public byte[] generatePdf(String content) {
    try (PDDocument document = new PDDocument(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
      PDPage page = new PDPage();
      document.addPage(page);

      try (PDPageContentStream stream = new PDPageContentStream(document, page)) {
        stream.beginText();
        stream.setFont(PDType1Font.HELVETICA, 12);
        stream.newLineAtOffset(50, 750);
        for (String line : content.split("\\R")) {
          stream.showText(line);
          stream.newLineAtOffset(0, -16);
        }
        stream.endText();
      }

      document.save(out);
      return out.toByteArray();
    } catch (IOException e) {
      throw new IllegalStateException("Failed to generate PDF.", e);
    }
  }

  public byte[] generateDocx(String content) {
    try (XWPFDocument document = new XWPFDocument(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
      XWPFParagraph paragraph = document.createParagraph();
      XWPFRun run = paragraph.createRun();
      run.setText(content);
      run.addBreak();
      document.write(out);
      return out.toByteArray();
    } catch (IOException e) {
      throw new IllegalStateException("Failed to generate DOCX.", e);
    }
  }
}
