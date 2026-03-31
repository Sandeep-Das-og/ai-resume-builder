package com.airesume.builder.ats;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class AtsFileValidator {
  private final AtsProperties properties;
  private final com.airesume.builder.security.ValidationGuard validationGuard;

  public AtsFileValidator(AtsProperties properties, com.airesume.builder.security.ValidationGuard validationGuard) {
    this.properties = properties;
    this.validationGuard = validationGuard;
  }

  public void validate(MultipartFile file) {
    if (file == null || file.isEmpty()) {
      throw new IllegalArgumentException("File is required.");
    }
    if (file.getSize() > properties.maxFileSizeBytes()) {
      throw new IllegalArgumentException("File exceeds max size.");
    }
    String contentType = file.getContentType();
    if (contentType == null || properties.allowedContentTypes() == null
        || !properties.allowedContentTypes().contains(contentType)) {
      throw new IllegalArgumentException("Unsupported file type.");
    }
    validationGuard.requireMaxLength(file.getOriginalFilename(), 120, "fileName");
  }
}
