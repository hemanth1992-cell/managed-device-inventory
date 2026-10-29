package com.example.manageddeviceinventory.linux;

import com.example.manageddeviceinventory.core.collector.PlatformCollector;
import com.example.manageddeviceinventory.core.model.InventoryEvent;

public class LinuxPlatformCollector implements PlatformCollector {
    @Override
    public InventoryEvent collect() {
        InventoryEvent event = InventoryEvent.createSample();
        event.getAgent().setPlatform("linux");
        return event;
    }

    @Override
    public String getPlatformName() {
        return "linux";
    }
}
