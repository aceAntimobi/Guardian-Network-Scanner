package com.guardian.networkscanner.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update

/**
 * Repository façade that bridges real network, bluetooth and sensor scanners.
 * All methods currently provide mocked data so that the UI can be exercised
 * without privileged permissions inside the development environment.
 */
class GuardianRepository {
    private val devices = MutableStateFlow<List<NetworkDevice>>(sampleDevices())
    private val arpAlerts = MutableStateFlow<List<ArpAlert>>(sampleArpAlerts())
    private val bluetoothDevices = MutableStateFlow(sampleBluetooth())
    private val toolResults = MutableStateFlow<List<ToolExecutionResult>>(emptyList())
    private val notes = MutableStateFlow<List<UserDeviceNote>>(emptyList())
    private val notificationPreferences = MutableStateFlow(
        NotificationPreferences(
            alertNewDevice = true,
            alertArp = true,
            alertPublicWifi = true,
            alertBluetoothTracker = true
        )
    )

    fun observeNetworkDevices(): Flow<List<NetworkDevice>> = devices.asStateFlow()

    fun observeArpAlerts(): Flow<List<ArpAlert>> = arpAlerts.asStateFlow()

    fun observeBluetoothDevices(): Flow<List<BluetoothDeviceInfo>> = bluetoothDevices.asStateFlow()

    fun observeToolResults(): Flow<List<ToolExecutionResult>> = toolResults.asStateFlow()

    fun observePrivacyScan(): Flow<PrivacyScanResult> = flow {
        emit(
            PrivacyScanResult(
                potentialNetworkCameras = devices.value.filter { it.type == DeviceType.CAMERA },
                magneticAnomalies = 1,
                infraredDetections = 0,
                audioAlerts = 0,
                notes = "Use physical inspection to confirm potential surveillance devices."
            )
        )
    }

    fun observeSecurityReport(): Flow<NetworkSecurityReport> = flow {
        emit(
            NetworkSecurityReport(
                encryption = "WPA3-Personal",
                wifiScore = 4,
                isPublicNetwork = false,
                phishingRisk = false,
                anomalies = listOf("Gateway firmware outdated"),
                recommendations = listOf(
                    "Change default router password",
                    "Enable automatic firmware updates",
                    "Use VPN when traveling"
                )
            )
        )
    }

    fun updateDeviceTrust(mac: String, trustLevel: TrustLevel) {
        val existing = notes.value.find { it.macAddress == mac }
        notes.update { current ->
            val filtered = current.filterNot { it.macAddress == mac }
            filtered + UserDeviceNote(mac, existing?.label ?: mac, existing?.remark, trustLevel)
        }
        devices.update { list ->
            list.map {
                if (it.macAddress == mac) {
                    it.copy(
                        isTrusted = trustLevel == TrustLevel.TRUSTED,
                        isSuspicious = trustLevel == TrustLevel.SUSPICIOUS
                    )
                } else it
            }
        }
    }

    fun addToolResult(result: ToolExecutionResult) {
        toolResults.update { resultList -> (resultList + result).takeLast(20) }
    }

    fun updateNotificationPreferences(preferences: NotificationPreferences) {
        notificationPreferences.value = preferences
    }

    fun observeNotificationPreferences(): Flow<NotificationPreferences> =
        notificationPreferences.asStateFlow()

    companion object {
        private fun sampleDevices(): List<NetworkDevice> = listOf(
            NetworkDevice(
                ipAddress = "192.168.0.1",
                macAddress = "AA:BB:CC:DD:EE:FF",
                vendor = "TP-Link",
                type = DeviceType.ROUTER,
                openPorts = listOf(80, 443),
                latencyMs = 2,
                services = listOf("HTTPS", "DNS")
            ),
            NetworkDevice(
                ipAddress = "192.168.0.10",
                macAddress = "11:22:33:44:55:66",
                vendor = "Apple",
                type = DeviceType.PHONE,
                isNew = true,
                latencyMs = 23,
                services = listOf("AirPlay", "mDNS")
            ),
            NetworkDevice(
                ipAddress = "192.168.0.42",
                macAddress = "22:33:44:55:66:77",
                vendor = "Hikvision",
                type = DeviceType.CAMERA,
                isSuspicious = true,
                openPorts = listOf(80, 554),
                services = listOf("RTSP", "HTTP")
            )
        )

        private fun sampleBluetooth(): List<BluetoothDeviceInfo> = listOf(
            BluetoothDeviceInfo(
                name = "John's AirPods",
                identifier = "AA:11:BB:22:CC:33",
                rssi = -45,
                distanceMeters = 1.5,
                isPaired = true,
                isTrackerLike = false,
                serviceUuids = listOf("180A")
            ),
            BluetoothDeviceInfo(
                name = "Unknown Tracker",
                identifier = "Random-1234",
                rssi = -55,
                distanceMeters = 2.0,
                isPaired = false,
                isTrackerLike = true,
                serviceUuids = listOf("FE2C")
            )
        )

        private fun sampleArpAlerts(): List<ArpAlert> = listOf(
            ArpAlert(
                message = "Gateway MAC address changed unexpectedly",
                severity = AlertSeverity.Critical,
                timestamp = System.currentTimeMillis() - 120_000
            )
        )
    }
}
