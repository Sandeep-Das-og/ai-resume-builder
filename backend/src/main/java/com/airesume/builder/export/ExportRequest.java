package com.airesume.builder.export;

import jakarta.validation.constraints.NotBlank;

public record ExportRequest(
    @NotBlank String content,
    String fileName
) {
}
