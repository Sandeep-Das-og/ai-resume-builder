package com.airesume.builder.ats;

public record AtsScoreBreakdown(
    int structureScore,
    int formattingScore,
    int keywordScore,
    int contactScore,
    int impactScore,
    int aiClarityScore,
    int aiFitScore,
    int aiImprovementScore
) {
}
