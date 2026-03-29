package com.airesume.builder.export;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ExportServiceTest {

  private final ExportService exportService = new ExportService();

  @Test
  void generatesPdfBytes() {
    byte[] bytes = exportService.generatePdf("Hello world\nLine two");

    assertThat(bytes).isNotEmpty();
    assertThat(bytes.length).isGreaterThan(100);
  }

  @Test
  void generatesDocxBytes() {
    byte[] bytes = exportService.generateDocx("Hello world\nLine two");

    assertThat(bytes).isNotEmpty();
    assertThat(bytes.length).isGreaterThan(100);
  }
}
