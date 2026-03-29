package com.airesume.builder.email;

public interface BrevoClient {
  BrevoSendResult send(BrevoEmailPayload payload);
}
