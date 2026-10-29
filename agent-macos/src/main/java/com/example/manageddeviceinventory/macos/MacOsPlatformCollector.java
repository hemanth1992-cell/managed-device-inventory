package com.example.manageddeviceinventory.windows;

import com.example.manageddeviceinventory.core.collector.PlatformCollector;
import com.example.manageddeviceinventory.core.model.InventoryEvent;

public class WindowsPlatformCollector implements PlatformCollector {
    @Override
    public InventoryEvent collect() {
        InventoryEvent event = InventoryEvent.createSample();
        event.getAgent().setPlatform("windows");
        return event;
    }

    @Override
    public String getPlatformName() {
        return "windows";
    }
}
