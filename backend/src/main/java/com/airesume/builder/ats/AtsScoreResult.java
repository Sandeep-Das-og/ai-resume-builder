package com.airesume.builder.ats;

import java.util.List;

public record AtsScoreResult(
    int totalScore,
    int rulesScore,
    int aiScore,
    int wordCount,
    AtsScoreBreakdown breakdown,
    List<String> topFixes
) {
}
