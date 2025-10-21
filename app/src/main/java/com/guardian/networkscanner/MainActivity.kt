package com.guardian.networkscanner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import com.guardian.networkscanner.data.GuardianRepository
import com.guardian.networkscanner.ui.navigation.GuardianApp
import com.guardian.networkscanner.ui.theme.GuardianTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val repository = remember { GuardianRepository() }
            GuardianTheme {
                GuardianApp(repository = repository)
            }
        }
    }
}
