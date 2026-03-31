package com.airesume.builder.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RateLimitConfig {

  @Bean
  public RateLimiter rateLimiter(SecurityProperties properties) {
    return new RateLimiter(properties.rateLimitPerMinute(), 60);
  }
}
