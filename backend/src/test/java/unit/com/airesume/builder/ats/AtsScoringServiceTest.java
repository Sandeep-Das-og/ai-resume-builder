package com.airesume.builder.ats;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class AtsScoringServiceTest {

  private final AtsScoringService scoringService = new AtsScoringService();

  @Test
  void scoresStructureAndContactAndImpact() {
    String resumeText = """
        John Doe\n
        Summary\n
        Results-driven engineer.\n
        Experience\n
        - Improved latency by 35% for 2M users.\n
        Skills\n
        Java, Spring Boot, AWS\n
        Education\n
        B.Tech, Computer Science\n
        Contact: john@example.com | +1 (415) 555-1212 | San Francisco, USA\n
        """;

    AtsScoreResult result = scoringService.score(resumeText, null, null);

    assertThat(result.totalScore()).isBetween(40, 100);
    assertThat(result.breakdown().structureScore()).isEqualTo(15);
    assertThat(result.breakdown().contactScore()).isGreaterThanOrEqualTo(3);
    assertThat(result.breakdown().impactScore()).isGreaterThanOrEqualTo(5);
  }

  @Test
  void scoresKeywordOverlapWhenJobDescriptionProvided() {
    String resumeText = "Java Spring Boot AWS Kubernetes";
    String jobDescription = "Looking for Java, AWS, and Kubernetes experience.";

    AtsScoreResult result = scoringService.score(resumeText, jobDescription, null);

    assertThat(result.breakdown().keywordScore()).isGreaterThanOrEqualTo(8);
  }

  @Test
  void penalizesMissingSections() {
    String resumeText = "Just a paragraph without headings.";

    AtsScoreResult result = scoringService.score(resumeText, null, null);

    assertThat(result.breakdown().structureScore()).isLessThan(10);
  }
}
