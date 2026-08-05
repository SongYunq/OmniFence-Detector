# OmniFence Detector Android

This directory is the Android project root and can be opened directly in Android Studio.

## Current Contents

- Kotlin, Jetpack Compose, and Material 3 frontend for the Detection screen.
- Light and dark themes with consistent semantic status colors.
- Target-region selection supporting countries, regions, states, and provinces.
- Bottom Sheet details for each detection item.
- Baseline `UI -> ViewModel -> Use Case -> Repository` layering.
- Demo data only; the app does not request sensitive permissions or upload detection data.

## Open and Run

1. In Android Studio, choose **Open** and select this `android` directory.
2. After Gradle sync completes, choose an emulator or device and run `app`.

The first sync downloads Gradle and dependencies. JDK 17 or later is recommended.

## Future Integration Points

`data/EnvironmentRepository.kt` currently contains `DemoEnvironmentRepository` for demonstration data. It will later be replaced by local Room storage, on-demand permission management, GPS/cellular/Wi-Fi signal sources, and a server-side network-egress API. The UI must not access sensitive system APIs directly.
