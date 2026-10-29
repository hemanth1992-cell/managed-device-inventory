package com.example.manageddeviceinventory.macos;

import com.example.manageddeviceinventory.core.collector.PlatformCollector;
import com.example.manageddeviceinventory.core.model.InventoryEvent;

public class MacOsPlatformCollector implements PlatformCollector {
    @Override
    public InventoryEvent collect() {
        InventoryEvent event = InventoryEvent.createSample();
        event.getAgent().setPlatform("macos");
        return event;
    }

    @Override
    public String getPlatformName() {
        return "macos";
    }
}
