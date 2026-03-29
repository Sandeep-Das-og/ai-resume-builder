package com.airesume.builder.email;

public record EmailSendResult(
    String status,
    String messageId
) {
}
