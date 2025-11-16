package com.example.levibegg.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.levibegg.ui.screens.ArtistDashboardScreen
import com.example.levibegg.ui.screens.ArtistsScreen
import com.example.levibegg.ui.screens.ExploreEventsScreen
import com.example.levibegg.ui.screens.LandingScreen
import com.example.levibegg.ui.screens.OrganizerDashboardScreen
import com.example.levibegg.ui.screens.RoleSelectScreen

// All routes for the app
sealed class Screen(val route: String) {
    data object Landing : Screen("landing")
    data object RoleSelect : Screen("role_select")

    // User explore screen (Figma-style)
    data object ExploreEvents : Screen("explore_events")

    data object Artists : Screen("artists")
    data object ArtistDashboard : Screen("artist_dashboard")
    data object OrganizerDashboard : Screen("organizer_dashboard")
}

/**
 * Main navigation graph for LeVibe.
 * Call LeVibeNav() from MainActivity.setContent { ... }.
 */
@Composable
fun LeVibeNav(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Landing.route,
        modifier = Modifier.fillMaxSize()
    ) {
        // Landing / intro
        composable(Screen.Landing.route) {
            LandingScreen(
                onGetStarted = {
                    navController.navigate(Screen.RoleSelect.route)
                },
                onHowItWorks = {
                    navController.navigate(Screen.RoleSelect.route)
                }
            )
        }

        // Role gate: "Who are you?"
        composable(Screen.RoleSelect.route) {
            RoleSelectScreen(
                // "I'm looking for events" → explore screen
                onSelectSeeker = {
                    navController.navigate(Screen.ExploreEvents.route)
                },
                // "I'm an Artist" → artist dashboard
                onSelectArtist = {
                    navController.navigate(Screen.ArtistDashboard.route)
                },
                // "I'm an Event Organizer" → organizer dashboard
                onSelectOrganizer = {
                    navController.navigate(Screen.OrganizerDashboard.route)
                }
            )
        }

        // 🎟 User explore page (Figma-style)
        composable(Screen.ExploreEvents.route) {
            ExploreEventsScreen(
                onProfileClick = {
                    // later: navigate to profile screen if you add one
                    // navController.navigate("profile")
                }
            )
        }

        // Artists directory
        composable(Screen.Artists.route) {
            ArtistsScreen(
                onBack = { navController.popBackStack() },
                onArtistSelected = { artistId ->
                    // later: navController.navigate("artist/$artistId")
                }
            )
        }

        // Artist dashboard (for creators)
        composable(Screen.ArtistDashboard.route) {
            ArtistDashboardScreen(
                onBack = { navController.popBackStack() }
            )
        }

        // Organizer dashboard
        composable(Screen.OrganizerDashboard.route) {
            OrganizerDashboardScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}
