package com.example.levibegg

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.levibegg.ui.navigation.Screen
import com.example.levibegg.ui.screens.ArtistDashboardScreen
import com.example.levibegg.ui.screens.EventsMapScreen
import com.example.levibegg.ui.screens.LandingScreen
import com.example.levibegg.ui.screens.OrganizerDashboardScreen
import com.example.levibegg.ui.screens.RoleSelectScreen
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
    val navController = rememberNavController()

    LeVibeGGTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            NavHost(
                navController = navController,
                startDestination = Screen.Landing.route
            ) {
                composable(Screen.Landing.route) {
                    LandingScreen(
                        onGetStarted = { navController.navigate(Screen.RoleSelect.route) },
                        onHowItWorks = { navController.navigate(Screen.RoleSelect.route) }
                    )
                }

                composable(Screen.RoleSelect.route) {
                    RoleSelectScreen(
                        onFan = { navController.navigate(Screen.EventsMap.route) },
                        onArtist = { navController.navigate(Screen.ArtistDashboard.route) },
                        onOrganizer = { navController.navigate(Screen.OrganizerDashboard.route) }
                    )
                }

                composable(Screen.EventsMap.route) {
                    EventsMapScreen()
                }

                composable(Screen.ArtistDashboard.route) {
                    ArtistDashboardScreen(
                        onBack = { navController.popBackStack() }
                    )
                }

                composable(Screen.OrganizerDashboard.route) {
                    OrganizerDashboardScreen(
                        onBack = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}
