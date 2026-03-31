package com.airesume.builder.email;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class EmailService {
  private final BrevoClient brevoClient;
  private final EmailProperties properties;
  private final com.airesume.builder.security.ValidationGuard validationGuard;

  public EmailService(BrevoClient brevoClient, EmailProperties properties,
      com.airesume.builder.security.ValidationGuard validationGuard) {
    this.brevoClient = brevoClient;
    this.properties = properties;
    this.validationGuard = validationGuard;
  }

  public EmailSendResult send(EmailSendRequest request) {
    String htmlContent = safe(request.htmlContent());
    String textContent = safe(request.textContent());

    if (isBlank(htmlContent) && isBlank(textContent)) {
      throw new IllegalArgumentException("Email content is required.");
    }

    validationGuard.requireMaxLength(htmlContent, properties.maxContentChars(), "htmlContent");
    validationGuard.requireMaxLength(textContent, properties.maxContentChars(), "textContent");

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
