package com.example.levibegg.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.levibegg.R
import com.example.levibegg.ui.theme.AmsterdamOne   // ✅ use the font from Type.kt

/**
 * Role gate screen – asks "Who are you?" with 3 roles.
 * Uses rolegate.jpg as a full-screen background image.
 */
@Composable
fun RoleSelectScreen(
    onSelectSeeker: () -> Unit,
    onSelectArtist: () -> Unit,
    onSelectOrganizer: () -> Unit
) {
    // Local visual selection just to drive the glass highlight
    var selectedRole by remember { mutableStateOf<String?>(null) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Black
    ) {
        Box(modifier = Modifier.fillMaxSize()) {

            // 🎆 Background image from drawable (rolegate.jpg)
            Image(
                painter = painterResource(id = R.drawable.rolegate),
                contentDescription = "Party crowd background",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Dark gradient overlay so text/cards pop
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0x99000000),   // top dim
                                Color(0xFF020014)    // deep bottom
                            )
                        )
                    )
            )

            // Foreground content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 40.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {

                // "Who are you?" in AmsterdamOne font
                Text(
                    text = "Who are you?",
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = AmsterdamOne,        // ✅ custom font here
                    modifier = Modifier.padding(bottom = 28.dp)
                )

                RoleCard(
                    title = "I'm looking for events",
                    subtitle = "Browse posters, maps, ride estimates & safety tips.",
                    isSelected = selectedRole == "seeker",
                    onClick = {
                        selectedRole = "seeker"
                        onSelectSeeker()
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                RoleCard(
                    title = "I'm an Artist",
                    subtitle = "Post gigs, grow your following & manage invites.",
                    isSelected = selectedRole == "artist",
                    onClick = {
                        selectedRole = "artist"
                        onSelectArtist()
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                RoleCard(
                    title = "I'm an Event Organizer",
                    subtitle = "Publish events, monitor performance & manage collabs.",
                    isSelected = selectedRole == "organizer",
                    onClick = {
                        selectedRole = "organizer"
                        onSelectOrganizer()
                    }
                )
            }
        }
    }
}

@Composable
private fun RoleCard(
    title: String,
    subtitle: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    // Glass background – stronger white gradient when selected
    val glassBrush = if (isSelected) {
        Brush.verticalGradient(
            colors = listOf(
                Color(0x55FFFFFF),   // brighter top
                Color(0x22FFFFFF)    // soft bottom
            )
        )
    } else {
        Brush.verticalGradient(
            colors = listOf(
                Color(0x26FFFFFF),
                Color(0x10FFFFFF)
            )
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(glassBrush)                           // gradient glass
            .border(
                width = 1.dp,
                color = if (isSelected) Color(0x66FFFFFF) else Color(0x40FFFFFF),
                shape = RoundedCornerShape(24.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 18.dp, vertical = 16.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = title,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = subtitle,
                color = Color(0xFFCBD5E1),
                fontSize = 12.sp
            )
        }
    }
}
