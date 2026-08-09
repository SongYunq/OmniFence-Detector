package com.omnifence.detector.data

import com.omnifence.detector.model.Availability
import com.omnifence.detector.model.DetectionStatus
import com.omnifence.detector.model.EnvironmentReport
import com.omnifence.detector.model.SignalObservation
import com.omnifence.detector.model.TargetArea
import java.time.Instant
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Boundary for future local Room storage, Android device signal sources, and network APIs.
 * The UI never talks to GPS, telephony, or network APIs directly.
 */
interface EnvironmentRepository {
    fun observeLatestReport(): Flow<EnvironmentReport>
    suspend fun refresh(targetArea: TargetArea)
}

/** Preview data only. Replace each observation with a permission-aware data source in later work. */
class DemoEnvironmentRepository : EnvironmentRepository {
    private val report = MutableStateFlow(createReport(defaultTarget()))

    override fun observeLatestReport(): Flow<EnvironmentReport> = report.asStateFlow()

    override suspend fun refresh(targetArea: TargetArea) {
        report.value = createReport(targetArea)
    }

    private fun defaultTarget() = TargetArea(
        countryCode = "US",
        countryName = "美国",
        subdivisionCode = "US-CA",
        subdivisionName = "加利福尼亚州",
    )

    private fun createReport(targetArea: TargetArea): EnvironmentReport {
        val now = Instant.now()
        return EnvironmentReport(
            targetArea = targetArea,
            generatedAt = now,
            observations = listOf(
                SignalObservation(
                    id = "ip",
                    title = "IP 网络出口",
                    result = "美国",
                    status = DetectionStatus.MATCHED,
                    reason = "网络出口的国家/地区与目标地区一致。",
                    rawValue = "104.xxx.xxx.xxx · United States",
                    observedAt = now,
                ),
                SignalObservation(
                    id = "gps",
                    title = "GPS 定位",
                    result = "中国 · 上海市",
                    status = DetectionStatus.ATTENTION,
                    reason = "GPS 解析地区与目标地区不同，建议确认目标地区设置及定位权限。",
                    rawValue = "31.2304, 121.4737 · 精度 12 米",
                    observedAt = now,
                ),
                SignalObservation(
                    id = "cell",
                    title = "基站 / 蜂窝网络",
                    result = "中国",
                    status = DetectionStatus.ATTENTION,
                    reason = "蜂窝网络国家与目标地区不同。",
                    rawValue = "MCC 460 · 4G",
                    observedAt = now,
                ),
                SignalObservation(
                    id = "sim",
                    title = "SIM 信息",
                    result = "中国移动 · CN",
                    status = DetectionStatus.ATTENTION,
                    reason = "SIM 归属国家与目标地区不同。",
                    rawValue = "运营商：中国移动 · 国家代码：CN",
                    observedAt = now,
                ),
                SignalObservation(
                    id = "timezone",
                    title = "系统时区",
                    result = "美国 · 太平洋时间",
                    status = DetectionStatus.MATCHED,
                    reason = "系统时区与目标地区的常见时区相符。",
                    rawValue = "America/Los_Angeles",
                    observedAt = now,
                ),
                SignalObservation(
                    id = "wifi",
                    title = "Wi‑Fi 状态",
                    result = "未获授权",
                    status = DetectionStatus.INSUFFICIENT,
                    reason = "未获得所需权限，暂无法判断 Wi‑Fi 环境。",
                    rawValue = "无可用数据",
                    availability = Availability.PERMISSION_DENIED,
                    observedAt = now,
                ),
            ),
        )
    }
}
