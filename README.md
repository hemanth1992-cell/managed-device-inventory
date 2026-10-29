# Managed Device Inventory

This repository contains a Java 21 multi-module implementation of the managed device inventory system described in `device_inventory_spec.md`.

## Included modules

- `agent-core` — queueing, scheduling, HTTPS submission, config, validation, and model classes
- `agent-windows` — Windows collector package
- `agent-macos` — macOS collector package
- `agent-linux` — Linux collector package
- `desktop-companion` — Java Swing UI
- `receiver` — minimal HTTPS receiver for inventory ingestion
- `database` — MySQL migration SQL
- `tests` — JUnit acceptance tests

## Build

```bash
./gradlew clean test
```

## Notes

This is a specification-aligned implementation skeleton designed for Java 21 and packageable for platform-specific deployment. Production deployment still requires signing, PKI, OS service registration, and authorized MDM/Intune configuration.
