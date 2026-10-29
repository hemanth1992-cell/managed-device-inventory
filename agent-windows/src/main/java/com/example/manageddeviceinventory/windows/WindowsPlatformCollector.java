package com.example.manageddeviceinventory.receiver;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class InventoryReceiverServer {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        InventoryReceiver receiver = new InventoryReceiver();

        server.createContext("/api/v1/inventory", exchange -> {
            try {
                String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                InventoryReceiver.EventProcessingResult result = receiver.accept(body);
                byte[] responseBody = ("{\"event_id\":\"" + (result.getEventId() == null ? "" : result.getEventId()) + "\",\"status\":\"accepted\"}").getBytes(StandardCharsets.UTF_8);
                if (result.getStatus() == 400) {
                    exchange.sendResponseHeaders(400, responseBody.length);
                    exchange.getResponseBody().write(responseBody);
                    exchange.getResponseBody().close();
                    return;
                }
                exchange.sendResponseHeaders(result.getStatus(), responseBody.length);
                exchange.getResponseBody().write(responseBody);
                exchange.getResponseBody().close();
            } catch (Exception e) {
                byte[] payload = "{\"code\":\"invalid_payload\",\"message\":\"invalid payload\"}".getBytes(StandardCharsets.UTF_8);
                exchange.sendResponseHeaders(400, payload.length);
                exchange.getResponseBody().write(payload);
                exchange.getResponseBody().close();
            }
        });

        server.createContext("/health/live", exchange -> respond(exchange, 200, "{\"status\":\"ok\"}"));
        server.createContext("/health/ready", exchange -> respond(exchange, 200, "{\"status\":\"ready\"}"));

        server.setExecutor(null);
        server.start();
        System.out.println("Inventory receiver listening on http://localhost:8080");
    }

    private static void respond(HttpExchange exchange, int status, String body) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(status, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}
