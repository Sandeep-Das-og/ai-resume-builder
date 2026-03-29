package com.airesume.builder.export;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.export")
public record ExportProperties(
    int maxContentChars
) {
}
