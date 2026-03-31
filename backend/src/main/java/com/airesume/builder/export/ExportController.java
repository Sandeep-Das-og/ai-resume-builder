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
  private final com.airesume.builder.security.ValidationGuard validationGuard;

  public ExportController(ExportService exportService, ExportProperties properties,
      com.airesume.builder.security.ValidationGuard validationGuard) {
    this.exportService = exportService;
    this.properties = properties;
    this.validationGuard = validationGuard;
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
    validationGuard.requireNotBlank(content, "content");
    validationGuard.requireMaxLength(content, properties.maxContentChars(), "content");
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
