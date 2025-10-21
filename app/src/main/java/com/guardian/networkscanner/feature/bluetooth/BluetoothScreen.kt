package com.guardian.networkscanner.feature.bluetooth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.guardian.networkscanner.data.BluetoothDeviceInfo
import com.guardian.networkscanner.data.GuardianRepository

@Composable
fun BluetoothScreen(repository: GuardianRepository, modifier: Modifier = Modifier) {
    val devices by repository.observeBluetoothDevices().collectAsState(initial = emptyList())
    val backgroundScan = remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = "Bluetooth scanner", style = MaterialTheme.typography.headlineSmall)
        ScanControls(backgroundScan)
        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(devices) { device ->
                BluetoothDeviceCard(device)
            }
        }
    }
}

@Composable
private fun ScanControls(backgroundScan: MutableState<Boolean>) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(text = "Background scanning")
                Text(text = "Scan every 5 minutes for trackers", style = MaterialTheme.typography.bodySmall)
            }
            Switch(
                checked = backgroundScan.value,
                onCheckedChange = { backgroundScan.value = it },
                colors = SwitchDefaults.colors(checkedThumbColor = MaterialTheme.colorScheme.primary)
            )
        }
    }
}

@Composable
private fun BluetoothDeviceCard(device: BluetoothDeviceInfo) {
    val containerColor = if (device.isTrackerLike) {
        MaterialTheme.colorScheme.errorContainer
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }
    Card(colors = CardDefaults.cardColors(containerColor = containerColor), modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(imageVector = Icons.Default.Bluetooth, contentDescription = null)
                Text(text = device.name ?: "Unknown device", style = MaterialTheme.typography.titleMedium)
                if (device.isTrackerLike) {
                    Icon(imageVector = Icons.Default.Warning, contentDescription = null)
                }
            }
            Text(text = "Identifier: ${device.identifier}")
            Text(text = "RSSI: ${device.rssi} dBm")
            device.distanceMeters?.let { Text(text = "Estimated distance: ${"%.1f".format(it)} m") }
            Text(text = if (device.isPaired) "Paired" else "Unpaired")
            if (device.serviceUuids.isNotEmpty()) {
                Text(text = "Services: ${device.serviceUuids.joinToString()}" )
            }
        }
    }
}
