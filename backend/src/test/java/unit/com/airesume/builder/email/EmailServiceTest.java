package com.airesume.builder.email;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class EmailServiceTest {

  @Test
  void buildsPayloadAndReturnsMessageId() {
    FakeBrevoClient client = new FakeBrevoClient();
    EmailProperties properties = new EmailProperties("test-key", "noreply@example.com", "AI Resume", 10000);
    EmailService service = new EmailService(client, properties);

    EmailSendRequest request = new EmailSendRequest(
        "user@example.com",
        "User",
        "Your resume",
        null,
        "Hello there"
    );

    EmailSendResult result = service.send(request);

    assertThat(result.status()).isEqualTo("queued");
    assertThat(result.messageId()).isEqualTo("msg-123");
    assertThat(client.lastPayload().subject()).isEqualTo("Your resume");
    assertThat(client.lastPayload().to().get(0).email()).isEqualTo("user@example.com");
  }

  @Test
  void rejectsMissingContent() {
    FakeBrevoClient client = new FakeBrevoClient();
    EmailProperties properties = new EmailProperties("test-key", "noreply@example.com", "AI Resume", 10000);
    EmailService service = new EmailService(client, properties);

    EmailSendRequest request = new EmailSendRequest(
        "user@example.com",
        null,
        "Your resume",
        null,
        null
    );

    assertThatThrownBy(() -> service.send(request))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("content");
  }

  static class FakeBrevoClient implements BrevoClient {
    private BrevoEmailPayload payload;

    @Override
    public BrevoSendResult send(BrevoEmailPayload payload) {
      this.payload = payload;
      return new BrevoSendResult("msg-123");
    }

    public BrevoEmailPayload lastPayload() {
      return payload;
    }
  }
}
