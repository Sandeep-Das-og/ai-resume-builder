package com.airesume.builder.security;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class RateLimiter {
  private final int limit;
  private final int windowSeconds;
  private final Map<String, Window> windows = new ConcurrentHashMap<>();

  public RateLimiter(int limit, int windowSeconds) {
    this.limit = limit;
    this.windowSeconds = windowSeconds;
  }

  public boolean allow(String key) {
    long now = Instant.now().getEpochSecond();
    Window window = windows.compute(key, (k, current) -> {
      if (current == null || now - current.startEpoch > windowSeconds) {
        return new Window(now, new AtomicInteger(1));
      }
      current.count.incrementAndGet();
      return current;
    });
    return window.count.get() <= limit;
  }

  private record Window(long startEpoch, AtomicInteger count) {
  }
}
