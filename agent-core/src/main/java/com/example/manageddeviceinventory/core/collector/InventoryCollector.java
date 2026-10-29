package com.example.manageddeviceinventory.core.agent;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;

public class Scheduler {
    private final Clock clock;
    private final long intervalSeconds;
    private Instant lastCollectionAt;

    public Scheduler(Clock clock, long intervalSeconds) {
        this.clock = clock;
        this.intervalSeconds = intervalSeconds;
    }

    public boolean shouldCollectNow() {
        Instant now = clock.instant();
        if (lastCollectionAt == null) {
            lastCollectionAt = now;
            return true;
        }
        return Duration.between(lastCollectionAt, now).getSeconds() >= intervalSeconds;
    }

    public void markCollected() {
        lastCollectionAt = clock.instant();
    }
}
