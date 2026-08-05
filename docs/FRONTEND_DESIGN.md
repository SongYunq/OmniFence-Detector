# OmniFence Detector Android Frontend Design

## 1. Purpose

This document defines the visual direction, information architecture, and detection-screen components for the first Android release of OmniFence Detector. It is the shared reference for UI design, component implementation, and review.

The product evaluates the consistency between the device environment and the user's selected target region. The interface must explain results and their evidence without making absolute claims about a device's real location.

## 2. Design Principles

- **Easy to scan:** Users should immediately see what matches, what needs attention, and what lacks enough data.
- **Explainable:** Every result can reveal raw values, parsed data, comparison logic, and data-quality context.
- **Calm and trustworthy:** Avoid aggressive security-software, hacker, or radar-scan visuals. Warnings must be clear without creating unnecessary anxiety.
- **Native Android:** Follow Material patterns for hierarchy, feedback, and interaction.
- **Accessible:** Never communicate a status through color alone; combine text, color, and an icon.

## 3. Theme Strategy

The app supports light and dark themes and follows the system setting by default. A future settings screen may provide Follow system, Always light, and Always dark options.

### 3.1 Light Theme

- Use white or a very light cool-gray page background.
- Use white for content surfaces.
- Use near-black primary text rather than pure black.
- Use low-contrast light-gray dividers.
- Keep the result list light, clean, and comfortable for dense information.

### 3.2 Dark Theme

- Use deep charcoal rather than large areas of pure black.
- Use surfaces slightly lighter than the page background.
- Use near-white primary text and low-luminance gray dividers.
- Provide dark-theme variants for every status color with sufficient contrast.

### 3.3 Semantic Color Tokens

Colors must be defined by semantic role rather than embedded as fixed values in screens.

| Token | Use |
| --- | --- |
| `background` | Page background |
| `surface` | Cards, list rows, and sheets |
| `textPrimary` | Primary text |
| `textSecondary` | Supporting text |
| `divider` | Row separators |
| `statusMatched` | Matched status |
| `statusAttention` | Needs-attention status |
| `statusInsufficient` | Insufficient-data status |
| `statusError` | Detection-error status |

### 3.4 Status Semantics

| Status | Color direction | Icon | Meaning |
| --- | --- | --- | --- |
| Matched | Green | Check | The current result is consistent with the target region. |
| Needs attention | Red | Exclamation mark | The result differs from the target region or requires user verification. |
| Insufficient data | Blue-gray | Minus or information icon | Permissions, platform capability, precision, or collection results do not support a decision. |
| Detection error | Deep red or dark orange | Warning icon | Data is invalid, stale, or contains an unexplained conflict. |

Every status chip must include text and an icon. Red signals an item requiring attention or an error; it is not a direct risk verdict.

## 4. Android Visual Direction

- Use a clear, relatively flat Material 3 style.
- Build hierarchy through surfaces, color, spacing, corner radius, and touch feedback.
- Use Material Symbols and standard Android patterns such as Bottom Sheets.
- Optimize result rows for fast scanning. Avoid excessive transparency, blur, or decorative shadows.
- Keep the information architecture and status semantics compatible with a future iOS version, while preserving native Android interaction patterns.

## 5. Navigation

The top-left area provides the page switcher. This is a top navigation control, not a conventional bottom tab bar.

Recommended first-release pages:

1. **Detection:** Select a target region and inspect the current environment report.
2. **Guidance:** Explain detection dimensions, permissions, status meanings, and verification suggestions.
3. **Common scenarios:** Explain which signals matter in scenarios such as travel, cross-region work, network changes, and missing permissions.
4. **History:** A later addition for previous reports and environment changes.

On phones, use the current page name with a dropdown menu. On wider screens, use horizontal segmented navigation when space permits.

## 6. Detection Screen

### 6.1 Screen Structure

```text
Top navigation: [Detection v]

Target region
[ United States                                      Edit ]

Report overview
Generated: Just now
3 matched · 2 need attention · 1 insufficient data

Detection results
[ IP egress           United States       (i)   Matched ]
[ GPS location        China               (i)   Needs attention ]
[ Cell network        China               (i)   Needs attention ]
[ SIM information     China               (i)   Needs attention ]
[ System time zone    United States       (i)   Matched ]
[ Wi-Fi status        Permission missing  (i)   Insufficient data ]
```

### 6.2 Target Region

- Place the Target region control at the top of the page.
- Support typing, searching, and choosing from a country or region list.
- Display a standardized region name. A flag or region code may support recognition but must not be the sole identifier.
- Prompt the user to run detection again after the target region changes.

### 6.3 Report Overview

The overview displays the report generation time and counts for each result status. A later version may add an overall consistency assessment, but must avoid absolute language such as real location or safe/unsafe.

### 6.4 Detection Result Rows

Each detection dimension occupies one consistent horizontal row:

- **Left:** Dimension name, such as IP, GPS, cell signal, SIM, or time zone.
- **Center:** Parsed region or current availability state.
- **Right:** A details entry point and a status chip.
- **Interaction:** The full row is tappable; the details icon provides a clear, direct expansion affordance.

Reading priority:

`Detection dimension -> Current result -> Consistency status -> Supporting evidence`

## 7. Detection Details

Use a circled `i` icon for the details entry point. Reserve an exclamation mark for statuses that require attention or represent an error.

Open details in a Material Bottom Sheet or an inline expanded section. Include, where available:

- Raw values, such as IP address, GPS coordinates, or location accuracy.
- Parsed country or region.
- Comparison with the target region.
- Data collection timestamp.
- Permission, platform-capability, or data-quality notes.
- A link to verification guidance when relevant.

Keep details collapsed by default so the main result list remains quick to scan.

## 8. Copy and Safety Boundaries

- Use neutral wording such as Matched with target region, Needs verification, and Unable to determine.
- Explain missing permissions directly, for example: Location permission is not granted, so GPS consistency cannot be determined.
- Describe mismatches as evidence, for example: The GPS-derived region differs from the target region. Confirm the target-region setting and location permission.
- Do not provide instructions to alter, spoof, or bypass geographic restrictions.
- Each conclusion shown to the user must be traceable to its data source and quality.

## 9. Next Design Steps

1. Confirm the final top-navigation interaction.
2. Define concrete light and dark tokens for color, typography, spacing, corner radius, and status components.
3. Create a low-fidelity Detection screen prototype.
4. Implement reusable Material 3 components using mock data.
5. Replace mock sources after the data, permissions, and application architecture are finalized.
