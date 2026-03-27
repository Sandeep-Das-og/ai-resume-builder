package com.airesume.builder.ats;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class AtsController {
  private final AtsFileValidator fileValidator;
  private final ResumeParser resumeParser;
  private final AtsScoringService scoringService;
  private final AtsProperties properties;

  public AtsController(AtsFileValidator fileValidator, ResumeParser resumeParser,
      AtsScoringService scoringService, AtsProperties properties) {
    this.fileValidator = fileValidator;
    this.resumeParser = resumeParser;
    this.scoringService = scoringService;
    this.properties = properties;
  }

  @PostMapping(value = "/api/ats/score", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<AtsScoreResult> scoreResume(
      @RequestPart("file") MultipartFile file,
      @RequestParam(value = "jobDescription", required = false) String jobDescription,
      @RequestParam(value = "targetRole", required = false) String targetRole
  ) throws IOException {
    fileValidator.validate(file);

    byte[] bytes = file.getBytes();
    String detectedType = resumeParser.detectContentType(bytes);
    if (detectedType == null || !properties.allowedContentTypes().contains(detectedType)) {
      throw new IllegalArgumentException("Unsupported file type.");
    }

    String text = resumeParser.parse(new ByteArrayInputStream(bytes));
    AtsScoreResult result = scoringService.score(text, jobDescription, targetRole);

    return ResponseEntity.ok(result);
  }
}
