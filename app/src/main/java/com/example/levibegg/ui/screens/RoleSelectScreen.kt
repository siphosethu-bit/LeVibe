package com.example.levibegg.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RoleSelectScreen(
    onFan: () -> Unit,
    onArtist: () -> Unit,
    onOrganizer: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF050814))
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Who are you?",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            RoleCard(
                title = "I'm looking for events",
                desc = "Browse posters, maps, ride estimates & safety tips.",
                onClick = onFan
            )
            RoleCard(
                title = "I'm an Artist",
                desc = "Post gigs, track followers & manage invites.",
                onClick = onArtist
            )
            RoleCard(
                title = "I'm an Event Organizer",
                desc = "Publish events, monitor performance & manage collabs.",
                onClick = onOrganizer
            )
        }
    }
}

@Composable
private fun RoleCard(
    title: String,
    desc: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF151826)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(4.dp))
            Text(desc, color = Color(0xFFB0B3C0), fontSize = 13.sp)
        }
    }
}
