package com.guardian.networkscanner.feature.scan

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alert
import androidx.compose.material.icons.filled.NewReleases
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.guardian.networkscanner.data.ArpAlert
import com.guardian.networkscanner.data.DeviceType
import com.guardian.networkscanner.data.GuardianRepository
import com.guardian.networkscanner.data.NetworkDevice
import com.guardian.networkscanner.data.TrustLevel

@Composable
fun ScanScreen(repository: GuardianRepository, modifier: Modifier = Modifier) {
    val devices by repository.observeNetworkDevices().collectAsState(initial = emptyList())
    val alerts by repository.observeArpAlerts().collectAsState(initial = emptyList())

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Wi-Fi Device Discovery",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Button(onClick = { /* TODO: trigger real scan */ }) {
            Text(text = "Scan Now")
        }
        AlertList(alerts = alerts)
        DeviceList(devices = devices, onTrustChanged = repository::updateDeviceTrust)
    }
}

@Composable
private fun AlertList(alerts: List<ArpAlert>, modifier: Modifier = Modifier) {
    if (alerts.isEmpty()) return
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        alerts.sortedByDescending { it.severity.priority }.forEach { alert ->
            Surface(color = MaterialTheme.colorScheme.errorContainer) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Alert, contentDescription = null)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(text = alert.message, style = MaterialTheme.typography.titleMedium)
                        Text(text = "Timestamp: ${alert.timestamp}", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}

@Composable
private fun DeviceList(
    devices: List<NetworkDevice>,
    onTrustChanged: (String, TrustLevel) -> Unit,
    modifier: Modifier = Modifier
) {
    if (devices.isEmpty()) {
        Text(text = "No devices detected yet.", style = MaterialTheme.typography.bodyMedium)
        return
    }
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(devices) { device ->
            DeviceCard(device = device, onTrustChanged = onTrustChanged)
        }
    }
}

@Composable
private fun DeviceCard(device: NetworkDevice, onTrustChanged: (String, TrustLevel) -> Unit) {
    val containerColor = when {
        device.isSuspicious -> MaterialTheme.colorScheme.errorContainer
        device.isNew -> MaterialTheme.colorScheme.secondaryContainer
        else -> MaterialTheme.colorScheme.surfaceVariant
    }
    Card(
        colors = CardDefaults.cardColors(containerColor = containerColor),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (device.isNew) {
                    Icon(imageVector = Icons.Default.NewReleases, contentDescription = null)
                }
                if (device.isTrusted) {
                    Icon(imageVector = Icons.Default.Verified, contentDescription = null)
                }
                Text(text = device.ipAddress, style = MaterialTheme.typography.titleMedium)
            }
            Text(text = "MAC: ${device.macAddress}", style = MaterialTheme.typography.bodySmall)
            Text(
                text = "Vendor: ${device.vendor ?: "Unknown"} • Type: ${device.type.prettyName()}",
                style = MaterialTheme.typography.bodySmall
            )
            if (device.openPorts.isNotEmpty()) {
                Text(text = "Open ports: ${device.openPorts.joinToString()}")
            }
            if (device.latencyMs != null) {
                Text(text = "Latency: ${device.latencyMs} ms")
            }
            if (device.services.isNotEmpty()) {
                Text(text = "Services: ${device.services.joinToString()}")
            }
            Divider()
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { onTrustChanged(device.macAddress, TrustLevel.TRUSTED) }) {
                    Text(text = "Mark Trusted")
                }
                Button(onClick = { onTrustChanged(device.macAddress, TrustLevel.SUSPICIOUS) }) {
                    Text(text = "Flag Suspicious")
                }
            }
        }
    }
}

private fun DeviceType.prettyName(): String = name.lowercase().replaceFirstChar { it.titlecase() }
