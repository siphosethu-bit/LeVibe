package com.example.levibegg.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.levibegg.data.model.Artist
import com.example.levibegg.data.model.ArtistData
import androidx.compose.material.icons.automirrored.filled.ArrowBack

/**
 * Artists directory screen (like the web “Artists” page).
 *
 * @param onBack           called when back button pressed.
 * @param onArtistSelected called with artist.id when "View profile" tapped.
 */
@Composable
fun ArtistsScreen(
    onBack: () -> Unit = {},
    onArtistSelected: (String) -> Unit = {}
) {
    val allArtists: List<Artist> = ArtistData.artists
    var query by remember { mutableStateOf("") }

    val filtered = remember(query, allArtists) {
        if (query.isBlank()) {
            allArtists
        } else {
            val q = query.trim().lowercase()
            allArtists.filter { artist ->
                artist.name.lowercase().contains(q) ||
                        artist.genre.any { it.lowercase().contains(q) }
            }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF02030A)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // Top bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
                Text(
                    text = "Artists",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(Modifier.height(8.dp))

            // Search bar
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        text = "Search South African artists...",
                        color = Color(0xFF6B7280),
                        fontSize = 13.sp
                    )
                },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF38BDF8),
                    unfocusedBorderColor = Color(0xFF111827),
                    cursorColor = Color.White,
                    focusedContainerColor = Color(0xFF020814),
                    unfocusedContainerColor = Color(0xFF020814),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                textStyle = MaterialTheme.typography.bodyMedium
            )

            Spacer(Modifier.height(12.dp))

            // Artists list
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filtered, key = { it.id }) { artist ->
                    ArtistCard(
                        artist = artist,
                        onViewProfile = { onArtistSelected(artist.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun ArtistCard(
    artist: Artist,
    onViewProfile: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onViewProfile() },
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF020814)
        ),
        shape = RoundedCornerShape(18.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar
            AsyncImage(
                model = artist.avatar,
                contentDescription = artist.name,
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(999.dp))
                    .background(Color(0xFF111827)),
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.width(10.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = artist.name,
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (artist.verified == true) {
                        Spacer(Modifier.width(4.dp))
                        // simple verified dot – you can swap for an icon later
                        Text(
                            text = "●",
                            color = Color(0xFF38BDF8),
                            fontSize = 10.sp
                        )
                    }
                }

                Text(
                    text = artist.genre.joinToString(" • "),
                    color = Color(0xFF9CA3AF),
                    fontSize = 11.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = artist.bio,
                    color = Color(0xFF6B7280),
                    fontSize = 10.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(Modifier.width(10.dp))

            Button(
                onClick = onViewProfile,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color(0xFF111827)
                ),
                contentPadding = ButtonDefaults.ContentPadding,
                shape = RoundedCornerShape(999.dp)
            ) {
                Text(
                    text = "View profile",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
