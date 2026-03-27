package com.airesume.builder.ats;

import java.util.List;

public record AtsSuggestionResult(
    int suggestionCount,
    List<String> suggestions
) {
}
