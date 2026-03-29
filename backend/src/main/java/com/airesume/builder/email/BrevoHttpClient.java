package com.airesume.builder.email;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.stereotype.Component;

@Component
public class BrevoHttpClient implements BrevoClient {
  private static final String BREVO_URL = "https://api.brevo.com/v3/smtp/email";

  private final HttpClient httpClient;
  private final ObjectMapper objectMapper;
  private final EmailProperties properties;

  public BrevoHttpClient(ObjectMapper objectMapper, EmailProperties properties) {
    this.httpClient = HttpClient.newHttpClient();
    this.objectMapper = objectMapper;
    this.properties = properties;
  }

  @Override
  public BrevoSendResult send(BrevoEmailPayload payload) {
    String apiKey = properties.apiKey();
    if (apiKey == null || apiKey.isBlank()) {
      throw new IllegalStateException("Email API key is not configured.");
    }
    try {
      String body = objectMapper.writeValueAsString(payload);
      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(BREVO_URL))
          .header("api-key", apiKey)
          .header("Content-Type", "application/json")
          .POST(HttpRequest.BodyPublishers.ofString(body))
          .build();

      HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
      if (response.statusCode() < 200 || response.statusCode() >= 300) {
        throw new IllegalStateException("Email provider returned status " + response.statusCode());
      }
      JsonNode node = objectMapper.readTree(response.body());
      JsonNode messageId = node.get("messageId");
      return new BrevoSendResult(messageId != null ? messageId.asText() : null);
    } catch (IOException | InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new IllegalStateException("Failed to send email.", e);
    }
  }
}
