package com.airesume.builder.security;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class RateLimitFilterTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void blocksAfterLimit() throws Exception {
    String clientIp = "198.51.100.42";
    for (int i = 0; i < 5; i++) {
      mockMvc.perform(get("/api/templates").header("X-Forwarded-For", clientIp))
          .andExpect(status().isOk());
    }

    mockMvc.perform(get("/api/templates").header("X-Forwarded-For", clientIp))
        .andExpect(status().isTooManyRequests());
  }
}
