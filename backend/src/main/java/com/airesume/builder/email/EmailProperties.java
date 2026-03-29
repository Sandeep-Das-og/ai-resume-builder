package com.airesume.builder.email;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.email")
public record EmailProperties(
    String apiKey,
    String senderEmail,
    String senderName,
    int maxContentChars
) {
}
