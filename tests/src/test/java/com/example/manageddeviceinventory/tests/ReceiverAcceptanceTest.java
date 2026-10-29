CREATE TABLE IF NOT EXISTS managed_devices (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tenant_id VARCHAR(128) NOT NULL,
    installation_uuid CHAR(36) NOT NULL,
    certificate_subject VARCHAR(255) NOT NULL,
    lifecycle_state VARCHAR(32) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_managed_device (tenant_id, installation_uuid)
);

CREATE TABLE IF NOT EXISTS inventory_events (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tenant_id VARCHAR(128) NOT NULL,
    event_id CHAR(36) NOT NULL,
    managed_device_id BIGINT NOT NULL,
    installation_uuid CHAR(36) NOT NULL,
    collected_at TIMESTAMP NOT NULL,
    received_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    schema_version VARCHAR(16) NOT NULL,
    content_hash CHAR(64) NOT NULL,
    payload JSON NOT NULL,
    UNIQUE KEY uk_event_id (tenant_id, event_id),
    KEY idx_device_collected (managed_device_id, collected_at)
);

CREATE TABLE IF NOT EXISTS inventory_latest (
    tenant_id VARCHAR(128) NOT NULL,
    managed_device_id BIGINT NOT NULL,
    latest_event_id CHAR(36) NOT NULL,
    collected_at TIMESTAMP NOT NULL,
    payload JSON NOT NULL,
    PRIMARY KEY (tenant_id, managed_device_id)
);

CREATE TABLE IF NOT EXISTS audit_events (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    occurred_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    action VARCHAR(128) NOT NULL,
    actor VARCHAR(128) NOT NULL,
    outcome VARCHAR(32) NOT NULL,
    target_reference VARCHAR(255) NOT NULL,
    details JSON
);
