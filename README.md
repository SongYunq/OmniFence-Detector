# OmniFence Detector

OmniFence Detector is a mobile tool for evaluating whether a device environment is consistent with a user-selected target region. It compares multiple authorized signals, presents an explainable report, and does not change or spoof device settings.

## Repository Layout

```text
.
├── android/    Android application (Kotlin, Jetpack Compose, Material 3)
├── ios/        Reserved for the future iOS application
└── docs/       Product and platform design documentation
```

## Current Status

The Android frontend is the current implementation. It uses demonstration data only and does not request sensitive permissions or upload detection data.

See [Android setup instructions](android/README.md) and the [product purpose](docs/PROJECT_PURPOSE.md).
