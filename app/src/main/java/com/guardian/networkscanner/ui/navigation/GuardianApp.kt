package com.guardian.networkscanner.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BluetoothSearching
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.NetworkWifi
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.guardian.networkscanner.data.GuardianRepository
import com.guardian.networkscanner.feature.bluetooth.BluetoothScreen
import com.guardian.networkscanner.feature.privacy.PrivacyScreen
import com.guardian.networkscanner.feature.scan.ScanScreen
import com.guardian.networkscanner.feature.security.SecurityScreen
import com.guardian.networkscanner.feature.settings.SettingsScreen
import com.guardian.networkscanner.feature.tools.ToolsScreen

@Composable
fun GuardianApp(repository: GuardianRepository, modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    Scaffold(
        modifier = modifier,
        bottomBar = { GuardianBottomBar(navController = navController) }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = GuardianDestination.Scan.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(GuardianDestination.Scan.route) {
                ScanScreen(repository = repository)
            }
            composable(GuardianDestination.Security.route) {
                SecurityScreen(repository = repository)
            }
            composable(GuardianDestination.Privacy.route) {
                PrivacyScreen(repository = repository)
            }
            composable(GuardianDestination.Bluetooth.route) {
                BluetoothScreen(repository = repository)
            }
            composable(GuardianDestination.Tools.route) {
                ToolsScreen(repository = repository)
            }
            composable(GuardianDestination.Settings.route) {
                SettingsScreen(repository = repository)
            }
        }
    }
}

@Composable
private fun GuardianBottomBar(navController: NavHostController) {
    val destinations = listOf(
        GuardianDestination.Scan,
        GuardianDestination.Security,
        GuardianDestination.Privacy,
        GuardianDestination.Bluetooth,
        GuardianDestination.Tools,
        GuardianDestination.Settings
    )
    val icons = mapOf(
        GuardianDestination.Scan to Icons.Default.NetworkWifi,
        GuardianDestination.Security to Icons.Default.Shield,
        GuardianDestination.Privacy to Icons.Default.PrivacyTip,
        GuardianDestination.Bluetooth to Icons.Default.BluetoothSearching,
        GuardianDestination.Tools to Icons.Default.Tune,
        GuardianDestination.Settings to Icons.Default.Settings
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    NavigationBar {
        destinations.forEach { destination ->
            NavigationBarItem(
                selected = currentRoute == destination.route,
                onClick = { navController.navigate(destination.route) {
                    launchSingleTop = true
                    restoreState = true
                    popUpTo(navController.graph.startDestinationId) {
                        saveState = true
                    }
                } },
                icon = {
                    Icon(
                        imageVector = icons[destination] ?: Icons.Default.BugReport,
                        contentDescription = destination.name
                    )
                },
                label = null,
                alwaysShowLabel = false
            )
        }
    }
}
