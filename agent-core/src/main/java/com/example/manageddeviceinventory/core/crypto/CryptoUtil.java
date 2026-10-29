package com.example.manageddeviceinventory.core.config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class AgentConfig {
    public static final String DEFAULT_BASE_URL = "https://inventory.example.com";
    private String baseUrl = DEFAULT_BASE_URL;
    private int connectTimeoutSeconds = 10;
    private int requestTimeoutSeconds = 30;
    private String transportEncoding = "json";
    private String authMode = "mtls";
    private String clientCertificateAlias = "managed-device-inventory";
    private int minimumTlsVersion = 1_2;
    private int collectionIntervalSeconds = 3600;
    private boolean locationEnabled = false;
    private long queueMaxBytes = 256L * 1024 * 1024;
    private int retryInitialSeconds = 60;
    private int retryMaxSeconds = 3600;
    private boolean resetAcknowledgedOnStart = false;
    private int pauseMinutes = 60;
    private String loggingLevel = "INFO";
    private int loggingRetentionDays = 14;

    public static AgentConfig load(Path file) throws IOException {
        AgentConfig config = new AgentConfig();
        Properties p = new Properties();
        try (InputStream in = Files.newInputStream(file)) {
            p.load(in);
        }

        config.baseUrl = requireString(p, "server.base_url", true);
        config.connectTimeoutSeconds = Integer.parseInt(requireString(p, "server.connect_timeout_seconds", true));
        config.requestTimeoutSeconds = Integer.parseInt(requireString(p, "server.request_timeout_seconds", true));
        config.transportEncoding = requireString(p, "transport.encoding", true);
        config.authMode = requireString(p, "auth.mode", true);
        config.clientCertificateAlias = requireString(p, "auth.client_certificate_alias", true);
        config.minimumTlsVersion = Integer.parseInt(requireString(p, "tls.minimum_version", true));
        config.collectionIntervalSeconds = Integer.parseInt(requireString(p, "collection.interval_seconds", true));
        config.locationEnabled = Boolean.parseBoolean(requireString(p, "collection.location.enabled", false));
        config.queueMaxBytes = Long.parseLong(requireString(p, "queue.max_bytes", false));
        config.retryInitialSeconds = Integer.parseInt(requireString(p, "queue.retry_initial_seconds", false));
        config.retryMaxSeconds = Integer.parseInt(requireString(p, "queue.retry_max_seconds", false));
        config.resetAcknowledgedOnStart = Boolean.parseBoolean(requireString(p, "queue.reset_acknowledged_on_start", false));
        config.pauseMinutes = Integer.parseInt(requireString(p, "ui.pause_minutes", false));
        config.loggingLevel = requireString(p, "logging.level", false);
        config.loggingRetentionDays = Integer.parseInt(requireString(p, "logging.retention_days", false));

        config.validate();
        return config;
    }

    public static AgentConfig fromProperties(Map<String, String> map) {
        AgentConfig config = new AgentConfig();
        config.baseUrl = map.getOrDefault("server.base_url", DEFAULT_BASE_URL);
        config.connectTimeoutSeconds = Integer.parseInt(map.getOrDefault("server.connect_timeout_seconds", "10"));
        config.requestTimeoutSeconds = Integer.parseInt(map.getOrDefault("server.request_timeout_seconds", "30"));
        config.transportEncoding = map.getOrDefault("transport.encoding", "json");
        config.authMode = map.getOrDefault("auth.mode", "mtls");
        config.clientCertificateAlias = map.getOrDefault("auth.client_certificate_alias", "managed-device-inventory");
        config.minimumTlsVersion = Integer.parseInt(map.getOrDefault("tls.minimum_version", "1.2").replace(".", ""));
        config.collectionIntervalSeconds = Integer.parseInt(map.getOrDefault("collection.interval_seconds", "3600"));
        config.locationEnabled = Boolean.parseBoolean(map.getOrDefault("collection.location.enabled", "false"));
        config.queueMaxBytes = Long.parseLong(map.getOrDefault("queue.max_bytes", String.valueOf(256L * 1024 * 1024)));
        config.retryInitialSeconds = Integer.parseInt(map.getOrDefault("queue.retry_initial_seconds", "60"));
        config.retryMaxSeconds = Integer.parseInt(map.getOrDefault("queue.retry_max_seconds", "3600"));
        config.resetAcknowledgedOnStart = Boolean.parseBoolean(map.getOrDefault("queue.reset_acknowledged_on_start", "false"));
        config.pauseMinutes = Integer.parseInt(map.getOrDefault("ui.pause_minutes", "60"));
        config.loggingLevel = map.getOrDefault("logging.level", "INFO");
        config.loggingRetentionDays = Integer.parseInt(map.getOrDefault("logging.retention_days", "14"));
        config.validate();
        return config;
    }

    public void validate() {
        if (baseUrl == null || !baseUrl.startsWith("https://") || baseUrl.contains("/")) {
            throw new IllegalArgumentException("Invalid server.base_url value");
        }
        if (connectTimeoutSeconds < 1 || connectTimeoutSeconds > 300) {
            throw new IllegalArgumentException("server.connect_timeout_seconds must be 1-300");
        }
        if (requestTimeoutSeconds < 1 || requestTimeoutSeconds > 300) {
            throw new IllegalArgumentException("server.request_timeout_seconds must be 1-300");
        }
        if (!"json".equals(transportEncoding) && !"base64_json".equals(transportEncoding)) {
            throw new IllegalArgumentException("transport.encoding must be json or base64_json");
        }
        if (!"mtls".equals(authMode)) {
            throw new IllegalArgumentException("auth.mode must be mtls in this release");
        }
        if (minimumTlsVersion != 1_2 && minimumTlsVersion != 1_3) {
            throw new IllegalArgumentException("tls.minimum_version must be 1.2 or 1.3");
        }
        if (collectionIntervalSeconds < 900 || collectionIntervalSeconds > 86400) {
            throw new IllegalArgumentException("collection.interval_seconds must be 900-86400");
        }
        if (queueMaxBytes < 1024 * 1024 || queueMaxBytes > 4L * 1024 * 1024 * 1024) {
            throw new IllegalArgumentException("queue.max_bytes must be 1 MiB to 4 GiB");
        }
        if (retryInitialSeconds <= 0 || retryMaxSeconds <= 0 || retryMaxSeconds < retryInitialSeconds) {
            throw new IllegalArgumentException("retry settings invalid");
        }
        if (pauseMinutes != 60) {
            throw new IllegalArgumentException("ui.pause_minutes must be 60");
        }
        if (!"INFO".equals(loggingLevel) && !"WARN".equals(loggingLevel)) {
            throw new IllegalArgumentException("logging.level must be INFO or WARN");
        }
        if (loggingRetentionDays < 1 || loggingRetentionDays > 90) {
            throw new IllegalArgumentException("logging.retention_days must be 1-90");
        }
    }

    public static String requireString(Properties properties, String key, boolean required) {
        String value = properties.getProperty(key);
        if (required && (value == null || value.isBlank())) {
            throw new IllegalArgumentException("Missing required property: " + key);
        }
        if (value != null && value.isBlank()) {
            return null;
        }
        return value;
    }

    public String getBaseUrl() { return baseUrl; }
    public void setBaseUrl(String baseUrl) { this.baseUrl = baseUrl; }
    public int getConnectTimeoutSeconds() { return connectTimeoutSeconds; }
    public void setConnectTimeoutSeconds(int connectTimeoutSeconds) { this.connectTimeoutSeconds = connectTimeoutSeconds; }
    public int getRequestTimeoutSeconds() { return requestTimeoutSeconds; }
    public void setRequestTimeoutSeconds(int requestTimeoutSeconds) { this.requestTimeoutSeconds = requestTimeoutSeconds; }
    public String getTransportEncoding() { return transportEncoding; }
    public void setTransportEncoding(String transportEncoding) { this.transportEncoding = transportEncoding; }
    public String getAuthMode() { return authMode; }
    public void setAuthMode(String authMode) { this.authMode = authMode; }
    public String getClientCertificateAlias() { return clientCertificateAlias; }
    public void setClientCertificateAlias(String clientCertificateAlias) { this.clientCertificateAlias = clientCertificateAlias; }
    public int getMinimumTlsVersion() { return minimumTlsVersion; }
    public void setMinimumTlsVersion(int minimumTlsVersion) { this.minimumTlsVersion = minimumTlsVersion; }
    public int getCollectionIntervalSeconds() { return collectionIntervalSeconds; }
    public void setCollectionIntervalSeconds(int collectionIntervalSeconds) { this.collectionIntervalSeconds = collectionIntervalSeconds; }
    public boolean isLocationEnabled() { return locationEnabled; }
    public void setLocationEnabled(boolean locationEnabled) { this.locationEnabled = locationEnabled; }
    public long getQueueMaxBytes() { return queueMaxBytes; }
    public void setQueueMaxBytes(long queueMaxBytes) { this.queueMaxBytes = queueMaxBytes; }
    public int getRetryInitialSeconds() { return retryInitialSeconds; }
    public void setRetryInitialSeconds(int retryInitialSeconds) { this.retryInitialSeconds = retryInitialSeconds; }
    public int getRetryMaxSeconds() { return retryMaxSeconds; }
    public void setRetryMaxSeconds(int retryMaxSeconds) { this.retryMaxSeconds = retryMaxSeconds; }
    public boolean isResetAcknowledgedOnStart() { return resetAcknowledgedOnStart; }
    public void setResetAcknowledgedOnStart(boolean resetAcknowledgedOnStart) { this.resetAcknowledgedOnStart = resetAcknowledgedOnStart; }
    public int getPauseMinutes() { return pauseMinutes; }
    public void setPauseMinutes(int pauseMinutes) { this.pauseMinutes = pauseMinutes; }
    public String getLoggingLevel() { return loggingLevel; }
    public void setLoggingLevel(String loggingLevel) { this.loggingLevel = loggingLevel; }
    public int getLoggingRetentionDays() { return loggingRetentionDays; }
    public void setLoggingRetentionDays(int loggingRetentionDays) { this.loggingRetentionDays = loggingRetentionDays; }
}
