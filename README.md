# Managed Device Inventory

This repository contains a Java 21, multi-module Gradle implementation of the managed device inventory system described in `device_inventory_spec.md`.

## Overview

The generated project includes:

- `agent-core` — configuration, encryption, queueing, scheduling, HTTP sending, and common models
- `agent-windows`, `agent-macos`, and `agent-linux` — platform collector stubs with OS-specific architecture hooks
- `desktop-companion` — Java Swing UI for status and pause/resume controls
- `receiver` — HTTPS inventory receiver with validation and idempotent persistence simulation
- `database` — MySQL migration SQL for the required tables
- `tests` — JUnit acceptance-focused unit tests

## Prerequisites

- JDK 21
- Gradle 8.10+ or use the provided `gradlew` shell wrapper after installing Gradle in PATH

## Build

```bash
./gradlew clean build
```

Windows:

```powershell
gradlew.bat clean build
```

## Run the receiver locally

```bash
./gradlew :receiver:bootRun
```

The local receiver listens on a default local port while exposing `/api/v1/inventory`, `/health/live`, and `/health/ready`.

## Run a safe agent profile

```bash
./gradlew :agent-core:classes
java -jar agent-core/build/libs/agent-core-1.0.0.jar
```

The agent core is intentionally non-intrusive and supports a safe test profile with a dummy local queue and randomized scheduler.

## Package installers

Platform packaging is represented in the repository as buildable stubs and instructions:

```bash
./gradlew :agent-windows:jar :agent-macos:jar :agent-linux:jar
```

The operational packaging and distribution steps for Intune and package managers live under `packaging/` and the docs directory.

## Useful documentation

- `docs/architecture.md`
- `docs/platform-support.md`
- `docs/operator-runbook.md`
- `docs/privacy-and-data-dictionary.md`
- `schema/inventory-v1.schema.json`
- `config/agent.properties.example`

## Test PKI and local development

A local development PKI and testing instructions are included in the runbook and example properties file. No production secrets are committed.
