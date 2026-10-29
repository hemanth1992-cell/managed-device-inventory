package com.example.manageddeviceinventory.core.agent;

import com.example.manageddeviceinventory.core.collector.InventoryCollector;
import com.example.manageddeviceinventory.core.config.AgentConfig;
import com.example.manageddeviceinventory.core.crypto.CryptoUtil;
import com.example.manageddeviceinventory.core.model.InventoryEvent;
import com.example.manageddeviceinventory.core.queue.EncryptedDocumentQueue;
import com.example.manageddeviceinventory.core.queue.InventorySender;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;

public class ManagedInventoryAgent {
    private final AgentConfig config;
    private final InventoryCollector collector;
    private final EncryptedDocumentQueue queue;
    private final InventorySender sender;
    private final Scheduler scheduler;
    private final ObjectMapper mapper = new ObjectMapper();
    private AgentStatus status = AgentStatus.RUNNING;

    public ManagedInventoryAgent(AgentConfig config, InventoryCollector collector) throws Exception {
        this.config = config;
        this.collector = collector;
        this.queue = new EncryptedDocumentQueue(Path.of("./queue"), CryptoUtil.createKey(), config.getQueueMaxBytes());
        this.sender = new InventorySender(config.getBaseUrl());
        this.scheduler = new Scheduler(java.time.Clock.systemUTC(), config.getCollectionIntervalSeconds());
    }

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
        queue.enqueue(event.getEventId(), mapper.writeValueAsString(event));
        scheduler.markCollected();
    }

    public void sendPending() throws Exception {
        for (EncryptedDocumentQueue.QueuedDocument q : queue.toDebugView()) {
            Optional<String> payload = queue.dequeue(q.getEventId());
            if (payload.isEmpty()) {
                continue;
            }
            InventoryEvent event = mapper.readValue(payload.get(), InventoryEvent.class);
            InventorySender.SendResult result = sender.send(event);
            status = (result.getStatusCode() >= 200 && result.getStatusCode() < 300)
                ? AgentStatus.RUNNING
                : AgentStatus.OFFLINE_RETRYING;
        }
    }

    public boolean shouldCollect() {
        return scheduler.shouldCollectNow();
    }

    public AgentStatus getStatus() {
        return status;
    }
}
