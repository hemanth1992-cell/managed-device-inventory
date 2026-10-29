package com.example.manageddeviceinventory.core.queue;

import com.example.manageddeviceinventory.core.crypto.CryptoUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class EncryptedDocumentQueue {
    private final Path queueDirectory;
    private final byte[] key;
    private final long maxBytes;
    private final Map<String, QueuedDocument> documents = new ConcurrentHashMap<>();

    public EncryptedDocumentQueue(Path queueDirectory, byte[] key, long maxBytes) throws IOException {
        this.queueDirectory = queueDirectory;
        this.key = key;
        this.maxBytes = maxBytes;
        Files.createDirectories(queueDirectory);
    }

    public void enqueue(String eventId, String jsonContent) throws IOException {
        byte[] plain = jsonContent.getBytes(StandardCharsets.UTF_8);
        CryptoUtil.EncryptedPayload encrypted = CryptoUtil.encrypt(key, plain, eventId.getBytes(StandardCharsets.UTF_8));
        String blob = CryptoUtil.toBase64(encrypted.getNonce()) + ":" + CryptoUtil.toBase64(encrypted.getCipherText());
        Path target = queueDirectory.resolve(eventId + ".enc");
        Files.writeString(target, blob, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
        documents.put(eventId, new QueuedDocument(eventId, target.toString(), blob.length()));
    }

    public Optional<String> dequeue(String eventId) throws IOException {
        QueuedDocument doc = documents.remove(eventId);
        if (doc == null) {
            return Optional.empty();
        }
        Path target = Path.of(doc.getPath());
        String blob = Files.readString(target, StandardCharsets.UTF_8);
        Files.deleteIfExists(target);

        String[] parts = blob.split(":", 2);
        if (parts.length != 2) {
            throw new IllegalStateException("Encrypted queue data malformed");
        }
        byte[] nonce = CryptoUtil.fromBase64(parts[0]);
        byte[] ciphertext = CryptoUtil.fromBase64(parts[1]);
        byte[] decrypted = CryptoUtil.decrypt(key, nonce, ciphertext, eventId.getBytes(StandardCharsets.UTF_8));
        return Optional.of(new String(decrypted, StandardCharsets.UTF_8));
    }

    public int size() {
        return documents.size();
    }

    public List<QueuedDocument> toDebugView() {
        return new ArrayList<>(documents.values());
    }

    public long getMaxBytes() {
        return maxBytes;
    }

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
