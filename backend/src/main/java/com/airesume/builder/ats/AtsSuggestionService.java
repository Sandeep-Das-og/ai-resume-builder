package com.airesume.builder.ats;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

@Service
public class AtsSuggestionService {
  private static final Pattern SECTION_PATTERN = Pattern.compile("(?m)^\\s*(summary|objective|experience|skills|education)\\b");
  private static final Pattern DIGIT_PATTERN = Pattern.compile("\\d");
  private static final Set<String> STOP_WORDS = Set.of(
      "a", "an", "and", "the", "for", "with", "to", "of", "in", "on", "at", "by", "from",
      "looking", "seeking", "role", "experience", "skills", "skill", "required", "responsibilities"
  );

  public AtsSuggestionResult suggest(String resumeText, String jobDescription, String targetRole) {
    String normalized = resumeText == null ? "" : resumeText.toLowerCase(Locale.ROOT);

    List<String> suggestions = new ArrayList<>();

    Set<String> sections = detectSections(normalized);
    if (!sections.contains("summary") && !sections.contains("objective")) {
      suggestions.add("Add a Summary section that clearly states your role focus and impact.");
    }
    if (!sections.contains("experience")) {
      suggestions.add("Add an Experience section with measurable achievements.");
    }
    if (!sections.contains("skills")) {
      suggestions.add("Add a Skills section to highlight relevant tools and technologies.");
    }
    if (!sections.contains("education")) {
      suggestions.add("Add an Education section with degree, school, and year.");
    }

    if (!hasQuantifiedImpact(resumeText)) {
      suggestions.add("Add quantified impact to bullets (percentages, time saved, cost reduction)." );
    }

    if (jobDescription != null && !jobDescription.isBlank()) {
      Set<String> missingKeywords = findMissingKeywords(normalized, jobDescription);
      if (!missingKeywords.isEmpty()) {
        suggestions.add("Improve keyword alignment with the job description: " + String.join(", ", missingKeywords));
      }
    }

    if (suggestions.isEmpty()) {
      suggestions.add("Great baseline. Tighten language and add more measurable results.");
    }

    List<String> top = suggestions.subList(0, Math.min(6, suggestions.size()));
    return new AtsSuggestionResult(top.size(), top);
  }

  private Set<String> detectSections(String normalized) {
    Set<String> found = new HashSet<>();
    Matcher matcher = SECTION_PATTERN.matcher(normalized);
    while (matcher.find()) {
      found.add(matcher.group(1));
    }
    return found;
  }

  private boolean hasQuantifiedImpact(String resumeText) {
    if (resumeText == null) {
      return false;
    }
    String[] lines = resumeText.split("\\R");
    for (String line : lines) {
      if (line.strip().startsWith("-") || line.strip().startsWith("•")) {
        if (DIGIT_PATTERN.matcher(line).find()) {
          return true;
        }
      }
    }
    return false;
  }

  private Set<String> findMissingKeywords(String normalized, String jobDescription) {
    Set<String> resumeTokens = tokenize(normalized);
    Set<String> jdTokens = tokenize(jobDescription.toLowerCase(Locale.ROOT));
    jdTokens.removeIf(token -> STOP_WORDS.contains(token));
    Set<String> missing = new HashSet<>();
    for (String token : jdTokens) {
      if (!resumeTokens.contains(token)) {
        missing.add(token);
      }
    }
    return missing;
  }

  private Set<String> tokenize(String text) {
    String normalized = text.replaceAll("[^a-zA-Z0-9 ]", " ");
    String[] tokens = normalized.split("\\s+");
    Set<String> results = new HashSet<>();
    for (String token : tokens) {
      if (!token.isBlank()) {
        results.add(token);
      }
    }
    return results;
  }
}
