package com.airesume.builder.ats;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
class AtsSuggestControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void returnsSuggestionsForValidFile() throws Exception {
    MockMultipartFile file = new MockMultipartFile(
        "file",
        "resume.txt",
        MediaType.TEXT_PLAIN_VALUE,
        "Experience\n- Built APIs\nSkills\nJava".getBytes()
    );

    MvcResult result = mockMvc.perform(
        multipart("/api/ats/suggest")
            .file(file)
            .param("jobDescription", "Java AWS Kubernetes")
    )
        .andExpect(status().isOk())
        .andReturn();

    String body = result.getResponse().getContentAsString();
    assertThat(body).contains("suggestions");
  }
}
