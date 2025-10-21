package com.guardian.networkscanner.feature.privacy

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Healing
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.guardian.networkscanner.data.GuardianRepository
import com.guardian.networkscanner.data.PrivacyScanResult

@Composable
fun PrivacyScreen(repository: GuardianRepository, modifier: Modifier = Modifier) {
    val result by repository.observePrivacyScan().collectAsState(initial = null)

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Privacy Threat Detection",
            style = MaterialTheme.typography.headlineSmall
        )
        result?.let { PrivacyContent(it) } ?: Text("Preparing privacy diagnostics...")
    }
}

@Composable
private fun PrivacyContent(result: PrivacyScanResult) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(text = "Network camera analysis", style = MaterialTheme.typography.titleMedium)
            if (result.potentialNetworkCameras.isEmpty()) {
                Text(text = "No known surveillance devices detected over the network.")
            } else {
                result.potentialNetworkCameras.forEach { device ->
                    Text(text = "• ${device.ipAddress} (${device.vendor ?: "Unknown"})")
                }
            }
            SensorSummary(result)
            Text(text = result.notes, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
private fun SensorSummary(result: PrivacyScanResult) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        SummaryItem(icon = Icons.Default.CameraAlt, label = "IR hits", value = result.infraredDetections)
        VerticalDivider()
        SummaryItem(icon = Icons.Default.Sensors, label = "Magnetic spikes", value = result.magneticAnomalies)
        VerticalDivider()
        SummaryItem(icon = Icons.Default.Healing, label = "Audio alerts", value = result.audioAlerts)
    }
}

@Composable
private fun SummaryItem(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Icon(imageVector = icon, contentDescription = null)
        Text(text = label, style = MaterialTheme.typography.bodySmall)
        Text(text = value.toString(), style = MaterialTheme.typography.titleLarge)
    }
}
