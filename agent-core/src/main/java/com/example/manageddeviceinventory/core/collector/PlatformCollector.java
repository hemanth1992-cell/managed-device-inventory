package com.example.manageddeviceinventory.core.collector;

import com.example.manageddeviceinventory.core.model.InventoryEvent;

public interface InventoryCollector {
    InventoryEvent collect();
}
