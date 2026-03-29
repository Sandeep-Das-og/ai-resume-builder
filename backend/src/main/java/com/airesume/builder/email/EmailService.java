package com.airesume.builder.email;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class EmailService {
  private final BrevoClient brevoClient;
  private final EmailProperties properties;

  public EmailService(BrevoClient brevoClient, EmailProperties properties) {
    this.brevoClient = brevoClient;
    this.properties = properties;
  }

  public EmailSendResult send(EmailSendRequest request) {
    String htmlContent = safe(request.htmlContent());
    String textContent = safe(request.textContent());

    if (isBlank(htmlContent) && isBlank(textContent)) {
      throw new IllegalArgumentException("Email content is required.");
    }

    if (htmlContent.length() > properties.maxContentChars()
        || textContent.length() > properties.maxContentChars()) {
      throw new IllegalArgumentException("Email content exceeds max size.");
    }

    BrevoEmailPayload payload = new BrevoEmailPayload(
        new BrevoSender(properties.senderName(), properties.senderEmail()),
        List.of(new BrevoRecipient(request.toEmail(), request.toName())),
        request.subject(),
        htmlContent.isBlank() ? null : htmlContent,
        textContent.isBlank() ? null : textContent
    );

    BrevoSendResult sendResult = brevoClient.send(payload);
    return new EmailSendResult("queued", sendResult.messageId());
  }

  private boolean isBlank(String value) {
    return value == null || value.isBlank();
  }

  private String safe(String value) {
    return value == null ? "" : value.trim();
  }
}
