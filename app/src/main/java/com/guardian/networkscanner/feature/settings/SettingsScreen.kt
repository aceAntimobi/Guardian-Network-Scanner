package com.guardian.networkscanner.feature.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.guardian.networkscanner.data.GuardianRepository
import com.guardian.networkscanner.data.NotificationPreferences

@Composable
fun SettingsScreen(repository: GuardianRepository, modifier: Modifier = Modifier) {
    val preferences by repository.observeNotificationPreferences().collectAsState(
        initial = NotificationPreferences(true, true, true, true)
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(text = "Notification preferences", style = MaterialTheme.typography.headlineSmall)
        PreferenceItem(
            title = "New device alerts",
            checked = preferences.alertNewDevice,
            onCheckedChange = { repository.updateNotificationPreferences(preferences.copy(alertNewDevice = it)) }
        )
        PreferenceItem(
            title = "ARP spoofing alerts",
            checked = preferences.alertArp,
            onCheckedChange = { repository.updateNotificationPreferences(preferences.copy(alertArp = it)) }
        )
        PreferenceItem(
            title = "Public Wi-Fi warnings",
            checked = preferences.alertPublicWifi,
            onCheckedChange = { repository.updateNotificationPreferences(preferences.copy(alertPublicWifi = it)) }
        )
        PreferenceItem(
            title = "Bluetooth tracker warnings",
            checked = preferences.alertBluetoothTracker,
            onCheckedChange = { repository.updateNotificationPreferences(preferences.copy(alertBluetoothTracker = it)) }
        )
    }
}

@Composable
private fun PreferenceItem(title: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = title)
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(checkedThumbColor = MaterialTheme.colorScheme.primary)
        )
    }
}
