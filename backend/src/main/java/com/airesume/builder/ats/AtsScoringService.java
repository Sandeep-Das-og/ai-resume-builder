package com.airesume.builder.ats;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

@Service
public class AtsScoringService {
  private static final Pattern EMAIL_PATTERN = Pattern.compile("[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}", Pattern.CASE_INSENSITIVE);
  private static final Pattern PHONE_PATTERN = Pattern.compile("(\\+?\\d[\\d\\s().-]{8,}\\d)");
  private static final Pattern DIGIT_PATTERN = Pattern.compile("\\d");
  private static final Set<String> SECTION_HEADINGS = Set.of("summary", "objective", "experience", "skills", "education");
  private static final Pattern SECTION_PATTERN = Pattern.compile("(?m)^\\s*(summary|objective|experience|skills|education)\\b");
  private static final Pattern LOCATION_PATTERN = Pattern.compile("\\b(usa|united states|india)\\b", Pattern.CASE_INSENSITIVE);
  private static final Set<String> STOP_WORDS = Set.of(
      "a", "an", "and", "the", "for", "with", "to", "of", "in", "on", "at", "by", "from",
      "looking", "seeking", "role", "experience", "skills", "skill", "required", "responsibilities"
  );

  public AtsScoreResult score(String resumeText, String jobDescription, String targetRole) {
    String normalized = resumeText == null ? "" : resumeText.toLowerCase(Locale.ROOT);

    int structureScore = scoreStructure(normalized);
    int formattingScore = scoreFormatting(resumeText);
    int keywordScore = scoreKeywords(normalized, jobDescription);
    int contactScore = scoreContact(resumeText);
    int impactScore = scoreImpact(resumeText);

    int rulesScore = structureScore + formattingScore + keywordScore + contactScore + impactScore;

    int aiClarityScore = scoreClarity(resumeText);
    int aiFitScore = scoreFit(normalized, jobDescription, targetRole);
    int aiImprovementScore = scoreImprovement(normalized);

    int aiScore = aiClarityScore + aiFitScore + aiImprovementScore;

    int totalScore = Math.min(100, Math.max(0, rulesScore + aiScore));
    int wordCount = countWords(resumeText);

    List<String> fixes = buildTopFixes(structureScore, keywordScore, contactScore, impactScore, jobDescription);

    AtsScoreBreakdown breakdown = new AtsScoreBreakdown(
        structureScore,
        formattingScore,
        keywordScore,
        contactScore,
        impactScore,
        aiClarityScore,
        aiFitScore,
        aiImprovementScore
    );

    return new AtsScoreResult(totalScore, rulesScore, aiScore, wordCount, breakdown, fixes);
  }

  private int scoreStructure(String normalized) {
    Set<String> found = new HashSet<>();
    Matcher matcher = SECTION_PATTERN.matcher(normalized);
    while (matcher.find()) {
      found.add(matcher.group(1));
    }
    if (found.size() >= 4) {
      return 15;
    }
    return Math.round(15f * found.size() / SECTION_HEADINGS.size());
  }

  private int scoreFormatting(String resumeText) {
    if (resumeText == null) {
      return 0;
    }
    long pipes = resumeText.chars().filter(ch -> ch == '|').count();
    if (pipes >= 8) {
      return 8;
    }
    return 15;
  }

  private int scoreKeywords(String normalized, String jobDescription) {
    if (jobDescription == null || jobDescription.isBlank()) {
      return 10;
    }
    Set<String> resumeTokens = tokenize(normalized, true);
    Set<String> jdTokens = tokenize(jobDescription.toLowerCase(Locale.ROOT), true);
    jdTokens.removeIf(token -> token.length() < 3);
    if (jdTokens.isEmpty()) {
      return 10;
    }
    int overlap = 0;
    for (String token : jdTokens) {
      if (resumeTokens.contains(token)) {
        overlap++;
      }
    }
    float ratio = (float) overlap / (float) jdTokens.size();
    return Math.min(15, Math.round(15 * ratio));
  }

  private int scoreContact(String resumeText) {
    if (resumeText == null) {
      return 0;
    }
    int score = 0;
    if (EMAIL_PATTERN.matcher(resumeText).find()) {
      score += 2;
    }
    if (PHONE_PATTERN.matcher(resumeText).find()) {
      score += 2;
    }
    if (containsLocation(resumeText)) {
      score += 1;
    }
    return score;
  }

  private int scoreImpact(String resumeText) {
    if (resumeText == null) {
      return 0;
    }
    String[] lines = resumeText.split("\\R");
    int impactful = 0;
    for (String line : lines) {
      if (line.strip().startsWith("-") || line.strip().startsWith("•")) {
        if (DIGIT_PATTERN.matcher(line).find()) {
          impactful++;
        }
      }
    }
    if (impactful >= 3) {
      return 10;
    }
    if (impactful == 2) {
      return 8;
    }
    if (impactful == 1) {
      return 5;
    }
    return 2;
  }

  private int scoreClarity(String resumeText) {
    if (resumeText == null || resumeText.isBlank()) {
      return 0;
    }
    String[] sentences = resumeText.split("[.!?]+\\s*");
    int totalWords = 0;
    int sentenceCount = 0;
    for (String sentence : sentences) {
      String trimmed = sentence.trim();
      if (!trimmed.isEmpty()) {
        sentenceCount++;
        totalWords += countWords(trimmed);
      }
    }
    if (sentenceCount == 0) {
      return 0;
    }
    float avg = (float) totalWords / sentenceCount;
    if (avg <= 20) {
      return 15;
    }
    if (avg <= 28) {
      return 10;
    }
    return 6;
  }

  private int scoreFit(String normalized, String jobDescription, String targetRole) {
    if (jobDescription == null || jobDescription.isBlank()) {
      return 8;
    }
    int keywordScore = scoreKeywords(normalized, jobDescription);
    if (keywordScore >= 12) {
      return 15;
    }
    if (keywordScore >= 8) {
      return 11;
    }
    return 6;
  }

  private int scoreImprovement(String normalized) {
    if (!normalized.contains("summary") && !normalized.contains("objective")) {
      return 4;
    }
    if (!normalized.contains("skills")) {
      return 6;
    }
    return 10;
  }

  private List<String> buildTopFixes(int structureScore, int keywordScore, int contactScore, int impactScore,
      String jobDescription) {
    List<String> fixes = new ArrayList<>();
    if (structureScore < 10) {
      fixes.add("Add missing core sections like Summary, Experience, Skills, or Education.");
    }
    if (keywordScore < 8 && jobDescription != null && !jobDescription.isBlank()) {
      fixes.add("Improve keyword alignment with the job description.");
    }
    if (contactScore < 4) {
      fixes.add("Add a clear email, phone number, and location.");
    }
    if (impactScore < 6) {
      fixes.add("Add quantified impact to experience bullets (%, $, scale).");
    }
    if (fixes.isEmpty()) {
      fixes.add("Strong baseline resume. Tighten language and add more measurable impact.");
    }
    return fixes.subList(0, Math.min(fixes.size(), 5));
  }

  private Set<String> tokenize(String text, boolean removeStopWords) {
    String normalized = text.replaceAll("[^a-zA-Z0-9 ]", " ");
    String[] tokens = normalized.split("\\s+");
    Set<String> results = new HashSet<>(Arrays.asList(tokens));
    results.removeIf(token -> token == null || token.isBlank());
    if (removeStopWords) {
      results.removeIf(token -> STOP_WORDS.contains(token));
    }
    return results;
  }

  private int countWords(String text) {
    if (text == null || text.isBlank()) {
      return 0;
    }
    return (int) Arrays.stream(text.trim().split("\\s+")).filter(token -> !token.isBlank()).count();
  }

  private boolean containsLocation(String resumeText) {
    return LOCATION_PATTERN.matcher(resumeText).find();
  }
}
