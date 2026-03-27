package com.airesume.builder.ats;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class AtsFileValidator {
  private final AtsProperties properties;

  public AtsFileValidator(AtsProperties properties) {
    this.properties = properties;
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
  }
}
