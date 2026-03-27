package com.airesume.builder.ats;

public record ApiError(
    String code,
    String message,
    String timestamp
) {
}
