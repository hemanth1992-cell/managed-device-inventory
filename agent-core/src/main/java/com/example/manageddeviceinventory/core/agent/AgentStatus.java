package com.example.manageddeviceinventory.core.queue;

import com.example.manageddeviceinventory.core.config.AgentConfig;
import com.example.manageddeviceinventory.core.model.InventoryEvent;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class InventorySender {
    private final AgentConfig config;
    private final HttpClient client;

    public InventorySender(AgentConfig config) {
        this.config = config;
        this.client = HttpClient.newBuilder().build();
    }

    public SendResult send(InventoryEvent event) throws Exception {
        String body = com.fasterxml.jackson.databind.ObjectMapperHolder.MAPPER.writeValueAsString(event);
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(config.getBaseUrl() + "/api/v1/inventory"))
            .header("Content-Type", "application/json; charset=utf-8")
            .POST(HttpRequest.BodyPublishers.ofString(body, StandardCharsets.UTF_8))
            .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        boolean accepted = response.statusCode() == 200 || response.statusCode() == 201;
        return new SendResult(accepted, response.statusCode(), response.body());
    }

    public static class SendResult {
        private final boolean accepted;
        private final int httpStatus;
        private final String responseBody;

        public SendResult(boolean accepted, int httpStatus, String responseBody) {
            this.accepted = accepted;
            this.httpStatus = httpStatus;
            this.responseBody = responseBody;
        }

        public boolean isAccepted() { return accepted; }
        public int getHttpStatus() { return httpStatus; }
        public String getResponseBody() { return responseBody; }
    }
}

class ObjectMapperHolder {
    static final com.fasterxml.jackson.databind.ObjectMapper MAPPER = new com.fasterxml.jackson.databind.ObjectMapper();
}
