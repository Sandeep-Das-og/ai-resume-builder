package com.airesume.builder.security;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class ValidationGuardTest {

  private final ValidationGuard guard = new ValidationGuard();

  @Test
  void rejectsBlank() {
    assertThatThrownBy(() -> guard.requireNotBlank(" ", "field"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("field");
  }

  @Test
  void rejectsTooLong() {
    assertThatThrownBy(() -> guard.requireMaxLength("a".repeat(5), 4, "field"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("max length");
  }
}
