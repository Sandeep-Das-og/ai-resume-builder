package com.airesume.builder.email;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EmailSendRequest(
    @Email @NotBlank String toEmail,
    String toName,
    @NotBlank String subject,
    String htmlContent,
    String textContent
) {
}
