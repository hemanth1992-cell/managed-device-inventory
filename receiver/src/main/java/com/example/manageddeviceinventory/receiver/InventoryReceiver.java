package com.example.manageddeviceinventory.receiver;

import com.example.manageddeviceinventory.core.model.InventoryEvent;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InventoryReceiver {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private final Map<String, InventoryEvent> accepted = new ConcurrentHashMap<>();

    public EventProcessingResult accept(String jsonBody) {
        try {
            InventoryEvent event = MAPPER.readValue(jsonBody, InventoryEvent.class);
            if (event.getEventId() == null) {
                return EventProcessingResult.rejected(400, "invalid_event_id");
            }
            if (accepted.containsKey(event.getEventId())) {
                return EventProcessingResult.accepted(event.getEventId(), 200, "already_received");
            }
            accepted.put(event.getEventId(), event);
            return EventProcessingResult.accepted(event.getEventId(), 201, "accepted");
        } catch (Exception ex) {
            return EventProcessingResult.rejected(400, "invalid_payload");
        }
    }

    public static class EventProcessingResult {
        private final int status;
        private final String eventId;
        private final String code;

        private EventProcessingResult(int status, String eventId, String code) {
            this.status = status;
            this.eventId = eventId;
            this.code = code;
        }

        public static EventProcessingResult accepted(String eventId, int status, String code) {
            return new EventProcessingResult(status, eventId, code);
        }

        public static EventProcessingResult rejected(int status, String code) {
            return new EventProcessingResult(status, null, code);
        }

        public int getStatus() { return status; }
        public String getEventId() { return eventId; }
        public String getCode() { return code; }
    }
}
