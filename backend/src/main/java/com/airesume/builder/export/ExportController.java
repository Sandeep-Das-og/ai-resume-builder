package com.airesume.builder.export;

import java.util.Locale;

import jakarta.validation.Valid;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExportController {
  private final ExportService exportService;
  private final ExportProperties properties;

  public ExportController(ExportService exportService, ExportProperties properties) {
    this.exportService = exportService;
    this.properties = properties;
  }

  @PostMapping(value = "/api/export/pdf", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<byte[]> exportPdf(@Valid @RequestBody ExportRequest request) {
    String content = sanitize(request.content());
    validateContent(content);

    byte[] bytes = exportService.generatePdf(content);
    String fileName = safeFileName(request.fileName(), "resume.pdf");

    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
        .contentType(MediaType.APPLICATION_PDF)
        .body(bytes);
  }

  @PostMapping(value = "/api/export/docx", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<byte[]> exportDocx(@Valid @RequestBody ExportRequest request) {
    String content = sanitize(request.content());
    validateContent(content);

    byte[] bytes = exportService.generateDocx(content);
    String fileName = safeFileName(request.fileName(), "resume.docx");

    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
        .contentType(MediaType.parseMediaType(
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document"))
        .body(bytes);
  }

  private void validateContent(String content) {
    if (content.isBlank()) {
      throw new IllegalArgumentException("Content is required.");
    }
    if (content.length() > properties.maxContentChars()) {
      throw new IllegalArgumentException("Content exceeds max size.");
    }
  }

  private String sanitize(String content) {
    return content.replaceAll("\\p{Cntrl}", "").trim();
  }

  private String safeFileName(String fileName, String fallback) {
    if (fileName == null || fileName.isBlank()) {
      return fallback;
    }
    String sanitized = fileName.replaceAll("[^a-zA-Z0-9._-]", "_");
    return sanitized.toLowerCase(Locale.ROOT);
  }
}
