package com.airesume.builder.export;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
class ExportControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void returnsPdfBytes() throws Exception {
    String payload = "{\"content\":\"Hello world\"}";

    MvcResult result = mockMvc.perform(
        post("/api/export/pdf")
            .contentType(MediaType.APPLICATION_JSON)
            .content(payload)
    )
        .andExpect(status().isOk())
        .andReturn();

    assertThat(result.getResponse().getContentType()).contains("application/pdf");
    assertThat(result.getResponse().getContentAsByteArray().length).isGreaterThan(100);
  }

  @Test
  void returnsDocxBytes() throws Exception {
    String payload = "{\"content\":\"Hello world\"}";

    MvcResult result = mockMvc.perform(
        post("/api/export/docx")
            .contentType(MediaType.APPLICATION_JSON)
            .content(payload)
    )
        .andExpect(status().isOk())
        .andReturn();

    assertThat(result.getResponse().getContentType()).contains("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
    assertThat(result.getResponse().getContentAsByteArray().length).isGreaterThan(100);
  }
}
