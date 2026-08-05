@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.omnifence.detector.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.omnifence.detector.data.DemoEnvironmentRepository
import com.omnifence.detector.domain.GetLatestReportUseCase
import com.omnifence.detector.model.DetectionStatus
import com.omnifence.detector.model.EnvironmentReport
import com.omnifence.detector.model.SignalObservation
import com.omnifence.detector.model.TargetArea
import com.omnifence.detector.ui.theme.OmniFenceTheme
import com.omnifence.detector.ui.theme.statusColors
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun OmniFenceApp() {
    val viewModel: DetectionViewModel = viewModel(
        key = "detection",
        factory = viewModelFactory {
            initializer {
                DetectionViewModel(GetLatestReportUseCase(DemoEnvironmentRepository()))
            }
        },
    )
    val report by viewModel.report.collectAsStateWithLifecycle()

    report?.let {
        DetectionScreen(
            report = it,
            onRefresh = viewModel::refresh,
        )
    } ?: Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("正在准备检测报告…")
    }
}

@Composable
private fun DetectionScreen(
    report: EnvironmentReport,
    onRefresh: (TargetArea) -> Unit,
) {
    var showPages by remember { mutableStateOf(false) }
    var showTargetPicker by remember { mutableStateOf(false) }
    var selectedObservation by remember { mutableStateOf<SignalObservation?>(null) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .clickable { showPages = true }
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text("检测", fontWeight = FontWeight.SemiBold)
                        Icon(Icons.Default.KeyboardArrowDown, contentDescription = "切换页面")
                    }
                    DropdownMenu(expanded = showPages, onDismissRequest = { showPages = false }) {
                        DropdownMenuItem(text = { Text("检测") }, onClick = { showPages = false })
                        DropdownMenuItem(text = { Text("指导（即将推出）") }, onClick = { showPages = false })
                        DropdownMenuItem(text = { Text("常见场景（即将推出）") }, onClick = { showPages = false })
                        DropdownMenuItem(text = { Text("历史记录（即将推出）") }, onClick = { showPages = false })
                    }
                },
                actions = {
                    IconButton(onClick = { onRefresh(report.targetArea) }) {
                        Icon(Icons.Default.Refresh, contentDescription = "重新检测")
                    }
                },
            )
        },
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(contentPadding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp),
        ) {
            Spacer(Modifier.height(2.dp))
            SectionTitle("目标地区")
            TargetAreaCard(targetArea = report.targetArea, onClick = { showTargetPicker = true })
            OverviewCard(report)
            SectionTitle("检测结果")
            ResultCard(
                observations = report.observations,
                onDetailClick = { selectedObservation = it },
            )
            Button(
                onClick = { onRefresh(report.targetArea) },
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 14.dp),
            ) {
                Icon(Icons.Default.Refresh, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("重新检测")
            }
            Text(
                text = "检测结果仅保存在此设备。每项结论均受权限、设备能力与数据时效影响。",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 28.dp),
            )
        }
    }

    if (showTargetPicker) {
        TargetAreaDialog(
            current = report.targetArea,
            onDismiss = { showTargetPicker = false },
            onSelect = {
                onRefresh(it)
                showTargetPicker = false
            },
        )
    }

    selectedObservation?.let { observation ->
        ObservationDetailSheet(observation = observation, targetArea = report.targetArea) {
            selectedObservation = null
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
    )
}

@Composable
private fun TargetAreaCard(targetArea: TargetArea, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(role = Role.Button, onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(18.dp),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 17.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Surface(
                modifier = Modifier.size(40.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer,
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                }
            }
            Spacer(Modifier.width(14.dp))
            Column(Modifier.weight(1f)) {
                Text(targetArea.displayName, style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(3.dp))
                Text(
                    "国家/地区${if (targetArea.subdivisionCode != null) " · 州/省" else ""}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            TextButton(onClick = onClick) { Text("编辑") }
        }
    }
}

@Composable
private fun OverviewCard(report: EnvironmentReport) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(18.dp),
    ) {
        Column(Modifier.padding(18.dp)) {
            Text("检测概览", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))
            Text(
                "当前报告生成于：${formatTime(report.generatedAt)}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                SummaryPill("一致 ${report.summary[DetectionStatus.MATCHED] ?: 0} 项", DetectionStatus.MATCHED)
                SummaryPill("待关注 ${report.summary[DetectionStatus.ATTENTION] ?: 0} 项", DetectionStatus.ATTENTION)
            }
            Spacer(Modifier.height(8.dp))
            SummaryPill("数据不足 ${report.summary[DetectionStatus.INSUFFICIENT] ?: 0} 项", DetectionStatus.INSUFFICIENT)
        }
    }
}

@Composable
private fun SummaryPill(text: String, status: DetectionStatus) {
    val color = statusColor(status)
    Surface(
        color = color.copy(alpha = 0.12f),
        contentColor = color,
        shape = RoundedCornerShape(8.dp),
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp),
        )
    }
}

@Composable
private fun ResultCard(
    observations: List<SignalObservation>,
    onDetailClick: (SignalObservation) -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(18.dp),
    ) {
        Column {
            observations.forEachIndexed { index, observation ->
                ObservationRow(observation = observation, onClick = { onDetailClick(observation) })
                if (index < observations.lastIndex) {
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.25f),
                        modifier = Modifier.padding(start = 18.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun ObservationRow(observation: SignalObservation, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(role = Role.Button, onClick = onClick)
            .padding(horizontal = 18.dp, vertical = 15.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(Modifier.weight(1f)) {
            Text(observation.title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
            Spacer(Modifier.height(3.dp))
            Text(
                observation.result,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        Spacer(Modifier.width(8.dp))
        StatusChip(observation.status)
        IconButton(onClick = onClick, modifier = Modifier.size(40.dp)) {
            Icon(
                Icons.Default.Info,
                contentDescription = "查看${observation.title}的检测依据",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun StatusChip(status: DetectionStatus) {
    val color = statusColor(status)
    val label = statusLabel(status)
    val icon = statusIcon(status)
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = color.copy(alpha = 0.12f),
        contentColor = color,
    ) {
        Row(
            modifier = Modifier.padding(start = 7.dp, end = 8.dp, top = 5.dp, bottom = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(14.dp))
            Spacer(Modifier.width(4.dp))
            Text(label, style = MaterialTheme.typography.labelMedium)
        }
    }
}

@Composable
private fun ObservationDetailSheet(
    observation: SignalObservation,
    targetArea: TargetArea,
    onDismiss: () -> Unit,
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 36.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(observation.title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.SemiBold)
            StatusChip(observation.status)
            DetailField("系统检测结果", observation.result)
            DetailField("计划所在地", targetArea.displayName)
            DetailField("原始检测值", observation.rawValue)
            DetailField("检测依据", observation.reason)
            DetailField("数据采集时间", formatTime(observation.observedAt))
            Text(
                "此项结论仅反映当前可用数据，不能据此对设备真实位置作绝对判断。",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun DetailField(label: String, value: String) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(label, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
private fun TargetAreaDialog(
    current: TargetArea,
    onDismiss: () -> Unit,
    onSelect: (TargetArea) -> Unit,
) {
    val targets = listOf(
        TargetArea("US", "美国", "US-CA", "加利福尼亚州"),
        TargetArea("US", "美国", "US-NY", "纽约州"),
        TargetArea("US", "美国"),
        TargetArea("CN", "中国", "CN-SH", "上海市"),
        TargetArea("CN", "中国"),
    )
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("选择计划所在地") },
        text = {
            Column {
                Text(
                    "首版支持国家/地区及州、省等一级行政区。",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(Modifier.height(8.dp))
                targets.forEach { target ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .clickable { onSelect(target) }
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(target.displayName, modifier = Modifier.weight(1f))
                        if (target == current) {
                            Icon(Icons.Default.CheckCircle, contentDescription = "当前选择", tint = statusColors.matched)
                        } else {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = "选择${target.displayName}",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                }
            }
        },
        confirmButton = { TextButton(onClick = onDismiss) { Text("取消") } },
    )
}

@Composable
private fun statusColor(status: DetectionStatus): Color = when (status) {
    DetectionStatus.MATCHED -> statusColors.matched
    DetectionStatus.ATTENTION -> statusColors.attention
    DetectionStatus.INSUFFICIENT -> statusColors.insufficient
    DetectionStatus.ERROR -> statusColors.error
}

private fun statusLabel(status: DetectionStatus): String = when (status) {
    DetectionStatus.MATCHED -> "一致"
    DetectionStatus.ATTENTION -> "待关注"
    DetectionStatus.INSUFFICIENT -> "数据不足"
    DetectionStatus.ERROR -> "检测异常"
}

private fun statusIcon(status: DetectionStatus): ImageVector = when (status) {
    DetectionStatus.MATCHED -> Icons.Default.CheckCircle
    DetectionStatus.ATTENTION -> Icons.Default.ErrorOutline
    DetectionStatus.INSUFFICIENT -> Icons.Default.Info
    DetectionStatus.ERROR -> Icons.Default.WarningAmber
}

private fun formatTime(instant: java.time.Instant): String = DateTimeFormatter
    .ofPattern("今天 HH:mm")
    .withZone(ZoneId.systemDefault())
    .format(instant)

@Preview(name = "检测页", showBackground = true, locale = "zh-rCN")
@Composable
private fun DetectionScreenPreview() {
    val now = java.time.Instant.now()
    val report = EnvironmentReport(
        targetArea = TargetArea("US", "美国", "US-CA", "加利福尼亚州"),
        generatedAt = now,
        observations = listOf(
            SignalObservation("ip", "IP 网络出口", "美国", DetectionStatus.MATCHED, "网络出口的国家/地区与目标地区一致。", "104.xxx.xxx.xxx · United States", observedAt = now),
            SignalObservation("gps", "GPS 定位", "中国 · 上海市", DetectionStatus.ATTENTION, "GPS 解析地区与目标地区不同。", "31.2304, 121.4737 · 精度 12 米", observedAt = now),
            SignalObservation("cell", "基站 / 蜂窝网络", "中国", DetectionStatus.ATTENTION, "蜂窝网络国家与目标地区不同。", "MCC 460 · 4G", observedAt = now),
            SignalObservation("sim", "SIM 信息", "中国移动 · CN", DetectionStatus.ATTENTION, "SIM 归属国家与目标地区不同。", "运营商：中国移动 · 国家代码：CN", observedAt = now),
            SignalObservation("timezone", "系统时区", "美国 · 太平洋时间", DetectionStatus.MATCHED, "系统时区与目标地区相符。", "America/Los_Angeles", observedAt = now),
            SignalObservation("wifi", "Wi‑Fi 状态", "未获授权", DetectionStatus.INSUFFICIENT, "未获得所需权限，暂无法判断 Wi‑Fi 环境。", "无可用数据", observedAt = now),
        ),
    )
    OmniFenceTheme {
        DetectionScreen(report = report, onRefresh = {})
    }
}
