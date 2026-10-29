package com.example.manageddeviceinventory.tests;

import com.example.manageddeviceinventory.core.model.InventoryEvent;
import com.example.manageddeviceinventory.receiver.InventoryReceiver;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReceiverAcceptanceTest {
    @Test
    void receiverAcceptsValidEventAndRejectsInvalidPayload() {
        InventoryEvent event = InventoryEvent.createSample();
        String json = "{\"schema_version\":\"1.0\",\"event_id\":\"" + event.getEventId() + "\",\"collected_at\":\"2026-10-29T10:30:00Z\",\"installation_id\":\"" + event.getInstallationId() + "\",\"agent\":{\"version\":\"1.0.0\",\"platform\":\"windows\"},\"device\":{\"device_name\":\"OFFICE-LAPTOP-01\",\"hostname\":\"OFFICE-LAPTOP-01\",\"processor\":{\"model\":\"Example 8-core processor\",\"architecture\":\"x86_64\",\"logical_processors\":8},\"memory\":{\"installed_bytes\":17179869184},\"storage\":[{\"name\":\"System\",\"total_bytes\":512110190592,\"free_bytes\":256000000000}],\"identifiers\":{\"device_id\":null,\"device_id_source\":null,\"product_id\":\"EXAMPLE-PRODUCT-ID\",\"serial_number\":\"EXAMPLE-SERIAL\"},\"network_interfaces\":[{\"name\":\"Ethernet\",\"mac\":\"02:00:00:00:00:01\",\"kind\":\"physical\"}],\"system_type\":\"64-bit operating system, x64-based processor\",\"os\":{\"name\":\"Windows\",\"edition\":\"Enterprise\",\"version\":\"11\",\"installed_at\":null,\"build\":\"example-build\"},\"manufacturer\":\"Example Manufacturer\",\"antivirus\":[{\"name\":\"Example Antivirus\",\"status\":\"enabled\"}],\"battery\":{\"present\":true,\"percent\":78,\"charging\":false},\"users\":[{\"account\":\"example.user\",\"domain\":\"EXAMPLE\",\"ad_id\":\"EXAMPLE\\\\example.user\",\"ad_id_type\":\"domain_account\",\"email\":null}],\"location\":null},\"field_issues\":[{\"field\":\"device.identifiers.device_id\",\"reason\":\"unavailable\"}]}";

        InventoryReceiver receiver = new InventoryReceiver();
        InventoryReceiver.EventProcessingResult result = receiver.accept(json);
        assertEquals(201, result.getStatus());
        assertNotNull(result.getEventId());

        InventoryReceiver.EventProcessingResult invalid = receiver.accept("{bad json}");
        assertEquals(400, invalid.getStatus());
    }
}
