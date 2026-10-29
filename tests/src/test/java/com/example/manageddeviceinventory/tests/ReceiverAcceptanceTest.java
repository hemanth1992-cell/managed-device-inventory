package com.example.manageddeviceinventory.core;

import com.example.manageddeviceinventory.core.model.InventoryEvent;
import com.example.manageddeviceinventory.core.validator.InventoryValidator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InventoryValidatorTest {
    @Test
    void validSampleEventPassesBasicChecks() {
        InventoryEvent event = InventoryEvent.createSample();
        assertTrue(InventoryValidator.validate(event).isEmpty());
    }
}
