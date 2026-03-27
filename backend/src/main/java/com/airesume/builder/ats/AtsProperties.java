package com.airesume.builder.ats;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.ats")
public record AtsProperties(
    long maxFileSizeBytes,
    List<String> allowedContentTypes,
    int maxParsedChars
) {
}
