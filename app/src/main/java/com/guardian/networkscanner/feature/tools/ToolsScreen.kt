package com.guardian.networkscanner.feature.tools

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.guardian.networkscanner.data.GuardianRepository
import com.guardian.networkscanner.data.ToolExecutionResult

@Composable
fun ToolsScreen(repository: GuardianRepository, modifier: Modifier = Modifier) {
    val results by repository.observeToolResults().collectAsState(initial = emptyList())

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = "Network toolbox", style = MaterialTheme.typography.headlineSmall)
        ToolActions(onExecute = repository::addToolResult)
        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(results) { result ->
                ToolResultCard(result)
            }
        }
    }
}

@Composable
private fun ToolActions(onExecute: (ToolExecutionResult) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
        Button(onClick = {
            onExecute(
                ToolExecutionResult(
                    title = "Ping guardian.app",
                    output = listOf("64 bytes from 192.0.2.1: icmp_seq=1 ttl=57 time=23.4 ms")
                )
            )
        }) { Text(text = "Run Ping") }
        Button(onClick = {
            onExecute(
                ToolExecutionResult(
                    title = "Traceroute example.com",
                    output = listOf("1 192.168.0.1", "2 10.0.0.1", "3 93.184.216.34")
                )
            )
        }) { Text(text = "Run Traceroute") }
    }
}

@Composable
private fun ToolResultCard(result: ToolExecutionResult) {
    Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(text = result.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            result.output.forEach { line -> Text(text = line, style = MaterialTheme.typography.bodySmall) }
            Text(text = "Timestamp: ${result.timestamp}", style = MaterialTheme.typography.bodySmall)
        }
    }
}
