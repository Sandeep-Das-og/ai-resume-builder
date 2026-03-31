package com.airesume.builder.security;

import org.springframework.stereotype.Component;

@Component
public class ValidationGuard {

  public void requireMaxLength(String value, int max, String fieldName) {
    if (value != null && value.length() > max) {
      throw new IllegalArgumentException(fieldName + " exceeds max length.");
    }
  }

  public void requireNotBlank(String value, String fieldName) {
    if (value == null || value.isBlank()) {
      throw new IllegalArgumentException(fieldName + " is required.");
    }
  }
}
