# OmniFence Detector Product Purpose

## Product Positioning

OmniFence Detector is a mobile multi-source location-environment verification tool.

After a user enters or selects a target region, the application collects the device's current environmental signals within the permissions granted by the user. It compares each actual result with the target region and produces a clear, explainable environment report.

The product does not change a device environment. Its purpose is to help users understand which current signals match the target region, which do not, and how missing data or permission limits affect the conclusion.

## Core Experience

The top of the page provides:

- **Target region:** The country or region the user expects the device environment to match.
- **Overall result:** Environment consistency, risk level, and a concise conclusion.
- **Detection time:** The report generation time and data freshness.

The lower area presents each detection dimension in a card or list row. Each item includes:

- The detection dimension, such as IP, GPS, SIM, cell network, or system time zone.
- The actual value detected by the system, such as the current IP address and its parsed region.
- The reference value for the target region.
- The consistency status.
- The reason for the status and data-quality notes.

Example:

| Detection dimension | System result | Target region | Status |
| --- | --- | --- | --- |
| IP | `104.xxx.xxx.xxx`, United States | United States | Matched |
| GPS | China, 10 m accuracy | United States | Needs attention |
| SIM | China Mobile, CN | United States | Needs attention |
| Time zone | `America/Los_Angeles` | United States | Matched |
| Wi-Fi | Permission missing or unavailable | United States | Insufficient data |

## Status Definitions

- **Matched:** The current result is consistent with the target region and meets the minimum data-quality requirement.
- **Needs attention:** The result conflicts with the target region, contains a clear inconsistency, or requires further verification.
- **Insufficient data:** Permission is not granted, the platform does not support the signal, collection failed, or data precision is inadequate for a conclusion.
- **Detection error:** Data format is invalid, data is stale, or there is an unexplained conflict. The user should run detection again or review the result manually.

Needs attention helps users understand and verify a mismatch. For example, users may confirm the target region, check location permissions, or verify the expected network-egress region. The product does not provide instructions to spoof a device environment or bypass geographic restrictions.

## Problems Addressed

A GPS result or IP address alone cannot reliably represent a complete geographical environment. OmniFence Detector uses multiple independent sources to identify inconsistencies, including:

- A mismatch between IP-derived region and GPS location.
- A mismatch between SIM, carrier, or cell-network country and the target region.
- Conflicts between system time zone, region, language settings, and other environmental signals.
- Geographical jumps that are implausible over a short period.
- Critical data that cannot be used reliably because of permissions, platform capabilities, or low precision.

The product produces an environment-consistency assessment and does not make an absolute claim about a device's physical location.

## Version 1 Scope

- Allow a user to set a target region.
- Collect environment data on Android and iOS within the permissions granted by the user.
- Display IP, GPS, network type, SIM or cell information, system region, time zone, and available Wi-Fi status.
- Show the actual result, target reference, status, and reason for every detection dimension.
- Produce an overall consistency assessment and risk level.
- Save detection history within the user's authorization scope.

## Explicit Boundaries

OmniFence Detector is for detection, analysis, and reporting only. It does not implement unauthorized continuous tracking or sensitive-data collection.
