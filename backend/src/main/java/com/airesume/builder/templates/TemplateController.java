package com.airesume.builder.templates;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TemplateController {
  private final ResourceLoader resourceLoader;

  public TemplateController(ResourceLoader resourceLoader) {
    this.resourceLoader = resourceLoader;
  }

  @GetMapping(value = "/api/templates", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> listTemplates() throws IOException {
    Resource resource = resourceLoader.getResource("classpath:templates/templates.json");
    byte[] bytes = resource.getInputStream().readAllBytes();
    String json = new String(bytes, StandardCharsets.UTF_8);
    return ResponseEntity.ok(json);
  }
}
