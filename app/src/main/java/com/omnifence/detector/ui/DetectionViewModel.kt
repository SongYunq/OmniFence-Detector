package com.omnifence.detector.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omnifence.detector.domain.GetLatestReportUseCase
import com.omnifence.detector.model.EnvironmentReport
import com.omnifence.detector.model.TargetArea
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DetectionViewModel(
    private val getLatestReport: GetLatestReportUseCase,
) : ViewModel() {
    val report: StateFlow<EnvironmentReport?> = getLatestReport()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    fun refresh(targetArea: TargetArea) {
        viewModelScope.launch { getLatestReport.refresh(targetArea) }
    }
}
