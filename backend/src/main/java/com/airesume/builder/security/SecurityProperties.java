package com.airesume.builder.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.security")
public record SecurityProperties(
    int rateLimitPerMinute
) {
}
