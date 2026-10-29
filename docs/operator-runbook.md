# Operator runbook

## 1. Pre-deployment

- Validate legal and privacy notice requirements.
- Confirm tenant registration, certificate lifecycle, and Intune or MDM deployment flow.
- Verify required OS versions and architecture support.
- Validate location permission policy before enabling geolocation.

## 2. Configuration

Copy the example file and replace placeholder values:

```bash
cp config/agent.properties.example /etc/managed-device-inventory/agent.properties
```

The service must use a real HTTPS origin and a machine certificate alias backed by the OS trust store and service account.

## 3. Installation

- Install the service under the normal OS service manager.
- Install the companion for the signed-in user session.
- Validate service startup, queue directory permissions, and secure key storage access.

## 4. Health checks

- Confirm queue health and pending count.
- Verify the local service status indicates healthy or attention required when a collection or delivery issue exists.
- Audit logs and review operational retention settings.

## 5. Recovery

- Preserve pending encrypted queue records during upgrade and uninstall.
- Never delete unsent events without explicit policy confirmation.
- Use a controlled, audited purge process for acknowledged remnants or decommissioned devices.
