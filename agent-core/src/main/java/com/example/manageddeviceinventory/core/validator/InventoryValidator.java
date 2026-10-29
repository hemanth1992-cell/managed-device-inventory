package com.example.manageddeviceinventory.core.validator;

import com.example.manageddeviceinventory.core.model.InventoryEvent;

import java.util.ArrayList;
import java.util.List;

public class InventoryValidator {
    public static List<String> validate(InventoryEvent event) {
        List<String> findings = new ArrayList<>();
        if (event == null) {
            findings.add("event is null");
            return findings;
        }
        if (event.getSchemaVersion() == null || !"1.0".equals(event.getSchemaVersion())) {
            findings.add("schema_version must be 1.0");
        }
        if (event.getEventId() == null || !event.getEventId().matches("^[0-9a-fA-F-]{36}$")) {
            findings.add("event_id must be a UUID");
        }
        if (event.getCollectedAt() == null || !event.getCollectedAt().endsWith("Z")) {
            findings.add("collected_at must be UTC with Z suffix");
        }
        if (event.getInstallationId() == null || !event.getInstallationId().matches("^[0-9a-fA-F-]{36}$")) {
            findings.add("installation_id must be a UUID");
        }
        if (event.getDevice() == null) {
            findings.add("device payload is required");
        }
        return findings;
    }
}
