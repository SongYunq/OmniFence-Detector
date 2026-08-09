package com.omnifence.detector.model

import java.time.Instant

/** A user-selected country, optionally narrowed to a first-level administrative area. */
data class TargetArea(
    val countryCode: String,
    val countryName: String,
    val subdivisionCode: String? = null,
    val subdivisionName: String? = null,
) {
    val displayName: String = listOfNotNull(countryName, subdivisionName).joinToString(" · ")
}

enum class DetectionStatus {
    MATCHED,
    ATTENTION,
    INSUFFICIENT,
    ERROR,
}

enum class Availability {
    AVAILABLE,
    PERMISSION_DENIED,
    UNSUPPORTED,
    UNAVAILABLE,
}

data class SignalObservation(
    val id: String,
    val title: String,
    val result: String,
    val status: DetectionStatus,
    val reason: String,
    val rawValue: String,
    val availability: Availability = Availability.AVAILABLE,
    val observedAt: Instant,
)

data class EnvironmentReport(
    val targetArea: TargetArea,
    val generatedAt: Instant,
    val observations: List<SignalObservation>,
) {
    val summary: Map<DetectionStatus, Int> = DetectionStatus.entries.associateWith { status ->
        observations.count { it.status == status }
    }
}
