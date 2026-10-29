package com.example.manageddeviceinventory.core;

import com.example.manageddeviceinventory.core.config.AgentConfig;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AgentConfigTest {
    @Test
    void validConfigurationIsAccepted() {
        AgentConfig config = AgentConfig.fromProperties(Map.of(
            "server.base_url", "https://inventory.example.com",
            "server.connect_timeout_seconds", "10",
            "server.request_timeout_seconds", "30",
            "transport.encoding", "json",
            "auth.mode", "mtls",
            "auth.client_certificate_alias", "managed-device-inventory",
            "tls.minimum_version", "1.2",
            "collection.interval_seconds", "3600",
            "collection.location.enabled", "false",
            "queue.max_bytes", "268435456",
            "queue.retry_initial_seconds", "60",
            "queue.retry_max_seconds", "3600",
            "queue.reset_acknowledged_on_start", "false",
            "ui.pause_minutes", "60",
            "logging.level", "INFO",
            "logging.retention_days", "14"
        ));

        assertEquals("https://inventory.example.com", config.getBaseUrl());
        assertEquals(3600, config.getCollectionIntervalSeconds());
    }

    @Test
    void invalidBaseUrlIsRejected() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> AgentConfig.fromProperties(Map.of(
            "server.base_url", "http://inventory.example.com",
            "server.connect_timeout_seconds", "10",
            "server.request_timeout_seconds", "30",
            "transport.encoding", "json",
            "auth.mode", "mtls",
            "auth.client_certificate_alias", "managed-device-inventory",
            "tls.minimum_version", "1.2",
            "collection.interval_seconds", "3600",
            "collection.location.enabled", "false",
            "queue.max_bytes", "268435456",
            "queue.retry_initial_seconds", "60",
            "queue.retry_max_seconds", "3600",
            "queue.reset_acknowledged_on_start", "false",
            "ui.pause_minutes", "60",
            "logging.level", "INFO",
            "logging.retention_days", "14"
        )));
        assertTrue(ex.getMessage().contains("Invalid server.base_url"));
    }
}
