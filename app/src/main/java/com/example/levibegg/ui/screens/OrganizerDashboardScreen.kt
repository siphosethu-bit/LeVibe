package com.example.levibegg.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.levibegg.R
import com.example.levibegg.ui.components.GlassButton

private enum class OrganizerTab {
    MyEvents, PostNew, Analytics, Collaborate
}

@Composable
fun OrganizerDashboardScreen(
    onBack: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(OrganizerTab.MyEvents) }
    val scrollState = rememberScrollState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF02030A)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {

            // Background image + dark overlay
            Image(
                painter = painterResource(id = R.drawable.organizer_dashboard_bg),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xE602030A),
                                Color(0xF002030A)
                            )
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 18.dp, vertical = 14.dp)
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {

                // Top row: Back + title
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    GlassButton(
                        text = "Back",
                        isPrimary = false
                    ) { onBack() }

                    // (Optional) future actions: Refresh / Close
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "Organizer Dashboard",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Manage your gigs, collaborate, and track performance.",
                        color = Color(0xFF9CA3AF),
                        fontSize = 12.sp
                    )
                }

                // Tabs: My events / Post new / Analytics / Collaborate
                OrganizerTabsRow(
                    selectedTab = selectedTab,
                    onTabSelected = { selectedTab = it }
                )

                // Content for each tab
                when (selectedTab) {
                    OrganizerTab.MyEvents -> MyEventsSection()
                    OrganizerTab.PostNew -> PostNewSection()
                    OrganizerTab.Analytics -> AnalyticsSection()
                    OrganizerTab.Collaborate -> CollaborateSection()
                }

                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

@Composable
private fun OrganizerTabsRow(
    selectedTab: OrganizerTab,
    onTabSelected: (OrganizerTab) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OrganizerTabChip(
            label = "My events",
            tab = OrganizerTab.MyEvents,
            selectedTab = selectedTab,
            onTabSelected = onTabSelected
        )
        OrganizerTabChip(
            label = "+ Post new",
            tab = OrganizerTab.PostNew,
            selectedTab = selectedTab,
            onTabSelected = onTabSelected
        )
        OrganizerTabChip(
            label = "Analytics",
            tab = OrganizerTab.Analytics,
            selectedTab = selectedTab,
            onTabSelected = onTabSelected
        )
        OrganizerTabChip(
            label = "Collaborate",
            tab = OrganizerTab.Collaborate,
            selectedTab = selectedTab,
            onTabSelected = onTabSelected
        )
    }
}

@Composable
private fun OrganizerTabChip(
    label: String,
    tab: OrganizerTab,
    selectedTab: OrganizerTab,
    onTabSelected: (OrganizerTab) -> Unit
) {
    val isSelected = selectedTab == tab

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .background(
                if (isSelected)
                    Color(0x33FBBF24)
                else
                    Color(0x33111827)
            )
            .border(
                BorderStroke(
                    width = 1.dp,
                    color = if (isSelected) Color(0xFFFBBF24) else Color(0x33FFFFFF)
                ),
                shape = RoundedCornerShape(999.dp)
            )
            .clickable { onTabSelected(tab) }
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Text(
            text = label,
            color = if (isSelected) Color(0xFFFBBF24) else Color.White,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium
        )
    }
}

/* ---------- TAB CONTENTS ---------- */

@Composable
private fun MyEventsSection() {
    // Single hero event card (like RAMFEST screenshot)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0x80111827)
        ),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.12f))
    ) {
        Column {
            // Poster / hero image placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(170.dp)
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFF0EA5E9),
                                Color(0xFF6366F1),
                                Color(0xFFEC4899)
                            )
                        )
                    )
            ) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(10.dp)
                        .clip(RoundedCornerShape(999.dp))
                        .background(Color(0xFF16A34A))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Live",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "RAMFEST x HALLOWEEN 2025 | Pretoria",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Heartfelt Arena • Pretoria",
                    color = Color(0xFFCBD5F5),
                    fontSize = 12.sp
                )
                Text(
                    text = "11/1/2025, 2:00 PM — 11/1/2025, 11:59 PM",
                    color = Color(0xFF9CA3AF),
                    fontSize = 11.sp
                )

                // Genres row
                Row(
                    modifier = Modifier.padding(top = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    GenrePill("Rock")
                    GenrePill("Alt")
                    GenrePill("Electronic")
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Buttons row: Edit details / View analytics / Share poster
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    GlassButton(
                        text = "Edit details",
                        isPrimary = false,
                        modifier = Modifier.weight(1f)
                    ) {
                        // TODO: open edit screen
                    }

                    GlassButton(
                        text = "View analytics",
                        isPrimary = false,
                        modifier = Modifier.weight(1f)
                    ) {
                        // TODO: open analytics
                    }

                    GlassButton(
                        text = "Share poster",
                        isPrimary = true,
                        modifier = Modifier.weight(1f)
                    ) {
                        // TODO: share poster
                    }
                }
            }
        }
    }
}

@Composable
private fun GenrePill(label: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .background(Color(0x331F2937))
            .border(
                BorderStroke(1.dp, Color(0x33FFFFFF)),
                RoundedCornerShape(999.dp)
            )
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = label,
            color = Color.White,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun PostNewSection() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0x80111827)
        ),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.12f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "Post your event",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "Give party people everything they need: time, place, vibe, and price. " +
                        "This is a placeholder for your full event creation form.",
                color = Color(0xFF9CA3AF),
                fontSize = 12.sp
            )

            GlassButton(
                text = "Start new event",
                isPrimary = true,
                modifier = Modifier.padding(top = 6.dp)
            ) {
                // TODO: navigate to full form
            }
        }
    }
}

@Composable
private fun AnalyticsSection() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0x80111827)
        ),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.12f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "Analytics",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "View poster performance, CTR, hourly sales, SharePlay clicks, and more.",
                color = Color(0xFF9CA3AF),
                fontSize = 12.sp
            )

            GlassButton(
                text = "Open poster analytics",
                isPrimary = true,
                modifier = Modifier.padding(top = 6.dp)
            ) {
                // TODO: analytics page
            }
        }
    }
}

@Composable
private fun CollaborateSection() {
    Column(
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = "Featured organizers to collaborate with",
            color = Color(0xFF9CA3AF),
            fontSize = 12.sp
        )

        listOf(
            "Aurora Nights" to "Cape Town • 4.7",
            "Vibe Foundry" to "Johannesburg • 4.5",
            "Lucid Collective" to "Durban • 4.6"
        ).forEach { (name, meta) ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0x80111827)
                ),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = name,
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = meta,
                        color = Color(0xFFCBD5F5),
                        fontSize = 12.sp
                    )
                    Text(
                        text = "Genres • Amapiano • House • Afro\nRecent events • High-energy city nights.",
                        color = Color(0xFF9CA3AF),
                        fontSize = 11.sp
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        GlassButton(
                            text = "View profile",
                            isPrimary = false,
                            modifier = Modifier.weight(1f)
                        ) {}

                        GlassButton(
                            text = "Collaborate",
                            isPrimary = true,
                            modifier = Modifier.weight(1f)
                        ) {}
                    }
                }
            }
        }
    }
}
