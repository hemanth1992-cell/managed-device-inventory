package com.example.manageddeviceinventory.core.queue;

import com.example.manageddeviceinventory.core.crypto.CryptoUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class EncryptedDocumentQueue {
    private final Path queueDirectory;
    private final ObjectMapper mapper;
    private final byte[] key;
    private final Map<String, QueuedDocument> documents = new ConcurrentHashMap<>();
    private final AtomicLong maxBytes;

    public EncryptedDocumentQueue(Path queueDirectory, byte[] key, long maxBytes) throws IOException {
        this.queueDirectory = queueDirectory;
        this.key = key;
        this.maxBytes = new AtomicLong(maxBytes);
        Files.createDirectories(queueDirectory);
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JavaTimeModule());
    }

    public void enqueue(String eventId, String jsonContent) throws IOException {
        byte[] plain = jsonContent.getBytes(StandardCharsets.UTF_8);
        byte[] associatedData = eventId.getBytes(StandardCharsets.UTF_8);
        CryptoUtil.EncryptedPayload payload = CryptoUtil.encrypt(key, plain, associatedData);
        String encoded = CryptoUtil.toBase64(payload.getNonce()) + ":" + CryptoUtil.toBase64(payload.getCipherText());
        String docName = UUID.randomUUID() + ".enc";
        Path target = queueDirectory.resolve(docName);
        Files.writeString(target, encoded, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
        documents.put(eventId, new QueuedDocument(eventId, target.toString(), encoded.length()));
    }

    public Optional<String> dequeue(String eventId) throws IOException {
        QueuedDocument doc = documents.get(eventId);
        if (doc == null) { return Optional.empty(); }
        String content = Files.readString(Path.of(doc.path), StandardCharsets.UTF_8);
        String[] parts = content.split(":", 2);
        if (parts.length != 2) { throw new IllegalStateException("Encrypted queue data malformed"); }
        byte[] nonce = CryptoUtil.fromBase64(parts[0]);
        byte[] ciphertext = CryptoUtil.fromBase64(parts[1]);
        byte[] decrypted = CryptoUtil.decrypt(key, nonce, ciphertext, eventId.getBytes(StandardCharsets.UTF_8));
        documents.remove(eventId);
        Files.deleteIfExists(Path.of(doc.path));
        return Optional.of(new String(decrypted, StandardCharsets.UTF_8));
    }

    public int size() { return documents.size(); }
    public long getMaxBytes() { return maxBytes.get(); }
    public void setMaxBytes(long value) { maxBytes.set(value); }

    public static class QueuedDocument {
        private final String eventId;
        private final String path;
        private final long approxBytes;

        public QueuedDocument(String eventId, String path, long approxBytes) {
            this.eventId = eventId;
            this.path = path;
            this.approxBytes = approxBytes;
        }

        public String getEventId() { return eventId; }
        public String getPath() { return path; }
        public long getApproxBytes() { return approxBytes; }
    }
}
