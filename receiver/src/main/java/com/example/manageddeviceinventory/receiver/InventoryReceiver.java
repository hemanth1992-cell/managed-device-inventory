package com.example.manageddeviceinventory.core;

import com.example.manageddeviceinventory.core.crypto.CryptoUtil;
import com.example.manageddeviceinventory.core.queue.EncryptedDocumentQueue;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class QueueEncryptionTest {
    @Test
    void encryptedQueueStoresAndRecoversPayload() throws Exception {
        Path dir = Files.createTempDirectory("queue-test");
        var key = CryptoUtil.createKey();
        EncryptedDocumentQueue queue = new EncryptedDocumentQueue(dir, key, 1024 * 1024);

        queue.enqueue("event-123", "{\"type\":\"inventory\"}");
        Optional<String> result = queue.dequeue("event-123");

        assertTrue(result.isPresent());
        assertTrue(result.get().contains("inventory"));
        assertEquals(0, queue.size());
    }
}
