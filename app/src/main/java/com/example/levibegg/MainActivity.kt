package com.example.levibegg

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import com.example.levibegg.ui.navigation.LeVibeNav
import com.example.levibegg.ui.theme.LeVibeGGTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LeVibeApp()
        }
    }
}

@Composable
fun LeVibeApp() {
    LeVibeGGTheme {
        // Single source of truth for navigation lives in LeVibeNav()
        LeVibeNav()
    }
}
