# Architecture

The solution is split into modules with explicit responsibilities:

- `agent-core` handles the inventory event contract, queue, scheduling, HTTP submission, settings, and crypto orchestration.
- `agent-windows`, `agent-macos`, and `agent-linux` provide platform-specific collector implementations and OS integration hooks.
- `desktop-companion` creates a Swing UI that contacts the service control endpoint and shows status/pause information.
- `receiver` validates, authenticates, persists, and idempotently stores inventory event payloads.
- `database` holds versioned SQL migrations that align with the MySQL model described in the specification.

## Lifecycle

1. On install, the OS-managed service is registered and the companion is installed for the user session.
2. The device loads configuration and secure key material.
3. A first collection occurs immediately after startup.
4. Periodic inventory snapshots are queued and sent by an HTTPS sender worker.
5. The receiver validates the event and writes the latest state for the tenant/device combination.
6. The sender removes only the accepted event by matching the event ID.
7. A pause/resume UI prevents collection and sending during temporary maintenance.

## Design decisions

- The package uses Java 21 and the standard Java HTTP client rather than hard-coded native access.
- Local queue records are encrypted with AES-256-GCM before being written to disk.
- The receiver uses a strict validation layer to reject malformed inventory payloads.
- The project deliberately avoids exposing secrets in configuration or logs.
