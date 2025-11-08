package com.example.levibegg.ui.navigation

sealed class Screen(val route: String) {
    data object Landing : Screen("landing")
    data object RoleSelect : Screen("role_select")
    data object EventsMap : Screen("events_map")
    data object ArtistDashboard : Screen("artist_dashboard")
    data object OrganizerDashboard : Screen("organizer_dashboard")
}
