package com.airesume.builder.ats;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class AtsSuggestionServiceTest {

  private final AtsSuggestionService suggestionService = new AtsSuggestionService();

  @Test
  void suggestsMissingSectionsAndKeywords() {
    String resumeText = "Experience\n- Built APIs\nSkills\nJava";
    String jobDescription = "Looking for Java, AWS, Kubernetes, and leadership.";

    AtsSuggestionResult result = suggestionService.suggest(resumeText, jobDescription, null);

    assertThat(result.suggestions()).isNotEmpty();
    assertThat(result.suggestions().get(0)).contains("Summary");
  }

  @Test
  void providesQuantifiedImpactSuggestion() {
    String resumeText = "Experience\n- Improved latency for users";

    AtsSuggestionResult result = suggestionService.suggest(resumeText, null, null);

    assertThat(result.suggestions()).anyMatch(s -> s.contains("quantified"));
  }
}
