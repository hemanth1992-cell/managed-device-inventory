# Privacy and data dictionary

The system intentionally collects only the fields listed in the specification and never collects passwords, tokens, clipboard contents, or secret files.

## Sensitive fields

The following fields must be handled carefully as part of the inventory process:

- machine and device identifiers
- serial numbers and product IDs
- user AD identifiers and email addresses
- MAC addresses and location coordinates
- antivirus registration metadata

## Processing rules

- Use the minimum necessary collection scope.
- Retain only approved event histories and inventory latest snapshots.
- Do not log raw payloads or private keys.
- Keep queue data encrypted and protected by a machine-only secret.
- Delay or block location collection unless explicitly authorized and enabled.
