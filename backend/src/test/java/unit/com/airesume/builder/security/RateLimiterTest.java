package com.airesume.builder.security;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class RateLimiterTest {

  @Test
  void blocksAfterLimit() {
    RateLimiter limiter = new RateLimiter(3, 60);
    String key = "127.0.0.1";

    assertThat(limiter.allow(key)).isTrue();
    assertThat(limiter.allow(key)).isTrue();
    assertThat(limiter.allow(key)).isTrue();
    assertThat(limiter.allow(key)).isFalse();
  }

  @Test
  void allowsDifferentKeysIndependently() {
    RateLimiter limiter = new RateLimiter(1, 60);

    assertThat(limiter.allow("a")).isTrue();
    assertThat(limiter.allow("b")).isTrue();
    assertThat(limiter.allow("a")).isFalse();
  }
}
