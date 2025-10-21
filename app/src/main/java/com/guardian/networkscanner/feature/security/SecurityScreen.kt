package com.guardian.networkscanner.feature.security

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WifiLock
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.guardian.networkscanner.data.GuardianRepository
import com.guardian.networkscanner.data.NetworkSecurityReport

@Composable
fun SecurityScreen(repository: GuardianRepository, modifier: Modifier = Modifier) {
    val report by repository.observeSecurityReport().collectAsState(initial = null)

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Network Security Assessment",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        report?.let { SecurityReportContent(it) } ?: Text("Awaiting security report...")
    }
}

@Composable
private fun SecurityReportContent(report: NetworkSecurityReport) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Icon(imageVector = Icons.Default.WifiLock, contentDescription = null)
            Text(text = "Encryption: ${report.encryption}")
            Text(text = "Security score: ${"★".repeat(report.wifiScore)}${"☆".repeat(5 - report.wifiScore)}")
            if (report.isPublicNetwork) {
                Text(text = "Public network detected", color = MaterialTheme.colorScheme.error)
            }
            if (report.phishingRisk) {
                Text(text = "Potential phishing hotspot", color = MaterialTheme.colorScheme.error)
            }
            Section(title = "Anomalies", items = report.anomalies)
            Section(title = "Recommendations", items = report.recommendations)
        }
    }
}

@Composable
private fun Section(title: String, items: List<String>) {
    if (items.isEmpty()) return
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(text = title, style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(2.dp))
        items.forEach { item ->
            Text(text = "• $item")
        }
    }
}
