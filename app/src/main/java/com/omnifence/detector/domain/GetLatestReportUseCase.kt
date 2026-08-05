package com.omnifence.detector.domain

import com.omnifence.detector.data.EnvironmentRepository
import com.omnifence.detector.model.EnvironmentReport
import com.omnifence.detector.model.TargetArea
import kotlinx.coroutines.flow.Flow

class GetLatestReportUseCase(private val repository: EnvironmentRepository) {
    operator fun invoke(): Flow<EnvironmentReport> = repository.observeLatestReport()

    suspend fun refresh(targetArea: TargetArea) = repository.refresh(targetArea)
}
