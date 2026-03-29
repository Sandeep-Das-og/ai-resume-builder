package com.airesume.builder.email;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
class EmailControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private BrevoClient brevoClient;

  @Test
  void returnsQueuedStatus() throws Exception {
    when(brevoClient.send(any())).thenReturn(new BrevoSendResult("msg-999"));

    String payload = """
        {
          "toEmail": "user@example.com",
          "toName": "User",
          "subject": "Your resume",
          "textContent": "Hello"
        }
        """;

    MvcResult result = mockMvc.perform(
        post("/api/email/send")
            .contentType(MediaType.APPLICATION_JSON)
            .content(payload)
    )
        .andExpect(status().isAccepted())
        .andReturn();

    String body = result.getResponse().getContentAsString();
    assertThat(body).contains("queued");
    assertThat(body).contains("msg-999");
  }
}
