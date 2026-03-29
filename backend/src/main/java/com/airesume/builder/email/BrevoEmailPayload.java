package com.airesume.builder.email;

import java.util.List;

public record BrevoEmailPayload(
    BrevoSender sender,
    List<BrevoRecipient> to,
    String subject,
    String htmlContent,
    String textContent
) {
}

record BrevoSender(
    String name,
    String email
) {
}

record BrevoRecipient(
    String email,
    String name
) {
}
