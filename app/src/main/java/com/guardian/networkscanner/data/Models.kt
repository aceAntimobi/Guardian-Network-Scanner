package com.guardian.networkscanner.data

data class NetworkDevice(
    val ipAddress: String,
    val macAddress: String,
    val vendor: String?,
    val type: DeviceType,
    val isNew: Boolean = false,
    val isTrusted: Boolean = false,
    val isSuspicious: Boolean = false,
    val openPorts: List<Int> = emptyList(),
    val latencyMs: Int? = null,
    val services: List<String> = emptyList()
)

enum class DeviceType {
    ROUTER, PHONE, TABLET, COMPUTER, CAMERA, IOT, UNKNOWN
}

data class ArpAlert(
    val message: String,
    val severity: AlertSeverity,
    val timestamp: Long
)

data class NetworkSecurityReport(
    val encryption: String,
    val wifiScore: Int,
    val isPublicNetwork: Boolean,
    val phishingRisk: Boolean,
    val anomalies: List<String>,
    val recommendations: List<String>
)

data class BluetoothDeviceInfo(
    val name: String?,
    val identifier: String,
    val rssi: Int,
    val distanceMeters: Double?,
    val isPaired: Boolean,
    val isTrackerLike: Boolean,
    val serviceUuids: List<String> = emptyList()
)

data class ToolExecutionResult(
    val title: String,
    val output: List<String>,
    val timestamp: Long = System.currentTimeMillis()
)

data class PrivacyScanResult(
    val potentialNetworkCameras: List<NetworkDevice>,
    val magneticAnomalies: Int,
    val infraredDetections: Int,
    val audioAlerts: Int,
    val notes: String
)

data class UserDeviceNote(
    val macAddress: String,
    val label: String,
    val remark: String?,
    val trustLevel: TrustLevel
)

enum class TrustLevel {
    TRUSTED, UNKNOWN, SUSPICIOUS
}

data class NotificationPreferences(
    val alertNewDevice: Boolean,
    val alertArp: Boolean,
    val alertPublicWifi: Boolean,
    val alertBluetoothTracker: Boolean
)

sealed interface AlertSeverity {
    val priority: Int

    data object Info : AlertSeverity { override val priority = 0 }
    data object Warning : AlertSeverity { override val priority = 1 }
    data object Critical : AlertSeverity { override val priority = 2 }
}
