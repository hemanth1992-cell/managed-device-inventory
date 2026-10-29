# Platform support

This project explicitly identifies the supported release targets described in the spec:

- Windows 11 x64: supported as the primary target
- Ubuntu Desktop 24.04 / 26.04 x64: supported as a deployment platform
- macOS Apple Silicon / Intel: supported only after native packaging and test qualification

## Current implementation status

This repository is structured for those targets and includes module stubs for OS-specific collectors. A full device management deployment for real enterprise environments will still require:

- approved signing certificates
- real Intune or other MDM configuration
- actual device certificate issuance and binding
- OS-specific runtime permissions and tests

## Collection capabilities

- Windows: registry, WMI-like runtime data, OS metadata, and session identity support are represented by portable Java interfaces.
- macOS: native permission-aware retrieval is modeled in the platform module.
- Linux: `/proc`, `/sys`, and OS metadata access are represented in the platform module.

The key requirement is that unsupported or denied data is represented as null and accompanied by a field issue rather than being invented.
