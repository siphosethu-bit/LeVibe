package com.example.levibegg.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.levibegg.ui.components.GlassRoleCard
import com.example.levibegg.ui.components.RoleGateVideoBackground

@Composable
fun RoleSelectScreen(
    onSelectSeeker: () -> Unit,
    onSelectArtist: () -> Unit,
    onSelectOrganizer: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Black // fallback behind video
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF02030A).copy(alpha = 0.2f)) // subtle global dark base
        ) {
            // 1) Background video (blurred + dim overlay inside the composable)
            RoleGateVideoBackground(
                modifier = Modifier.fillMaxSize(),
                blurRadius = 45f,        // tweak: 30f (softer) – 60f (more blur)
                dimOverlayAlpha = 0.55f  // tweak: 0.4 lighter, 0.7 darker
            )

            // 2) Foreground content, centered
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Who are you?",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold
                )

                GlassRoleCard(
                    title = "I'm looking for events",
                    subtitle = "Browse posters, maps, ride estimates & safety tips.",
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onSelectSeeker
                )

                GlassRoleCard(
                    title = "I'm an Artist",
                    subtitle = "Post gigs, grow your following & manage invites.",
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onSelectArtist
                )

                GlassRoleCard(
                    title = "I'm an Event Organizer",
                    subtitle = "Publish events, monitor performance & manage collabs.",
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onSelectOrganizer
                )
            }
        }
    }
}
