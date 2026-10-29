package com.example.manageddeviceinventory.core.agent;

import com.example.manageddeviceinventory.core.collector.InventoryCollector;
import com.example.manageddeviceinventory.core.config.AgentConfig;
import com.example.manageddeviceinventory.core.crypto.CryptoUtil;
import com.example.manageddeviceinventory.core.model.InventoryEvent;
import com.example.manageddeviceinventory.core.queue.EncryptedDocumentQueue;
import com.example.manageddeviceinventory.core.queue.InventorySender;

import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;

public class ManagedInventoryAgent {
    private final AgentConfig config;
    private final InventoryCollector collector;
    private final EncryptedDocumentQueue queue;
    private final InventorySender sender;
    private final Scheduler scheduler;
    private AgentStatus status = AgentStatus.RUNNING;

    public ManagedInventoryAgent(AgentConfig config, InventoryCollector collector) throws Exception {
        this.config = config;
        this.collector = collector;
        this.queue = new EncryptedDocumentQueue(Path.of("./queue"), CryptoUtil.createKey(), config.getQueueMaxBytes());
        this.sender = new InventorySender(config);
        this.scheduler = new Scheduler(java.time.Clock.systemUTC(), config.getCollectionIntervalSeconds());
    }

    public AgentStatus getStatus() { return status; }

    public Optional<InventoryEvent> collectOnce() {
        InventoryEvent event = collector.collect();
        if (event == null) {
            status = AgentStatus.ATTENTION_REQUIRED;
            return Optional.empty();
        }
        event.setEventId(UUID.randomUUID().toString());
        event.setInstallationId(UUID.randomUUID().toString());
        return Optional.of(event);
    }

    public void queueEvent(InventoryEvent event) throws Exception {
        queue.enqueue(event.getEventId(), com.fasterxml.jackson.databind.ObjectMapperHolder.MAPPER.writeValueAsString(event));
        scheduler.markCollected();
    }

    public void sendPending() throws Exception {
        if (queue.size() == 0) {
            return;
        }
        for (var doc : queue.toDebugView()) {
            String eventJson = queue.dequeue(doc.getEventId()).orElseThrow();
            InventoryEvent event = com.fasterxml.jackson.databind.ObjectMapperHolder.MAPPER.readValue(eventJson, InventoryEvent.class);
            InventorySender.SendResult result = sender.send(event);
            if (result.isAccepted()) {
                status = AgentStatus.RUNNING;
            } else {
                status = AgentStatus.OFFLINE_RETRYING;
            }
        }
    }

    public boolean shouldCollect() {
        return scheduler.shouldCollectNow();
    }
}
