package com.example.levibegg.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.levibegg.ui.components.GlassButton

@Composable
fun LandingScreen(
    onGetStarted: () -> Unit,
    onHowItWorks: () -> Unit = {} // kept for compatibility, we use local sheet
) {
    var showHowItWorks by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF02030A) // deep club-night background
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            // Glowing animated orb behind everything
            OrbBackground()

            // Foreground content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Where to.. Next?",
                    color = Color(0xFF38BDF8),
                    fontSize = 30.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 2.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(24.dp))

                Text(
                    text = "lé Vibe",
                    color = Color(0xFF38BDF8),
                    fontSize = 72.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 4.sp,
                    lineHeight = 66.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(28.dp))

                Text(
                    text = "Browse events by genre and budget, preview the area, set one-tap reminders, and split costs with your crew all in one place.",
                    color = Color(0xFFE5E5E5),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Light,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(0.9f)
                )

                Spacer(Modifier.height(32.dp))

                // Glass buttons row
                Row(
                    modifier = Modifier.wrapContentWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Primary CTA -> Role gate
                    GlassButton(
                        text = "Get started",
                        isPrimary = true,
                        onClick = onGetStarted
                    )

                    // Secondary CTA -> Opens in-app "How it works" sheet
                    GlassButton(
                        text = "How it works",
                        onClick = {
                            showHowItWorks = true
                            onHowItWorks()
                        }
                    )
                }
            }

            // Dim overlay + glass sheet when How it works is visible
            if (showHowItWorks) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xCC000000)),
                    contentAlignment = Alignment.Center
                ) {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .wrapContentHeight(),
                        color = Color(0xFF050814),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Header row with title + X
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = "How it works",
                                        fontSize = 22.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Text(
                                        text = "lé Vibe helps you discover events, plan the night, and arrive safely.",
                                        fontSize = 13.sp,
                                        color = Color(0xFF9CA3AF)
                                    )
                                }

                                Text(
                                    text = "✕",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFFF9FAFB),
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .background(Color(0x331F2937))
                                        .clickable { showHowItWorks = false }
                                        .padding(
                                            horizontal = 10.dp,
                                            vertical = 4.dp
                                        )
                                )
                            }

                            Spacer(Modifier.height(8.dp))

                            HowItWorksItem(
                                title = "Swipe posters",
                                body = "Browse curated gigs by genre, city, budget & popularity. Save favorites for later."
                            )
                            HowItWorksItem(
                                title = "One-tap reminders",
                                body = "Get notified for ticket drops and event start times with one tap."
                            )
                            HowItWorksItem(
                                title = "Safety & transit",
                                body = "View quick safety notes and public transport / ride options near each event."
                            )
                            HowItWorksItem(
                                title = "Area preview + rides",
                                body = "Check the venue on the map and estimate rides to and from the event."
                            )
                            HowItWorksItem(
                                title = "Plan with friends",
                                body = "Share plans with your crew so everyone sees ride splits and details."
                            )
                            HowItWorksItem(
                                title = "Budget filter",
                                body = "Slide your max budget to instantly hide pricey events."
                            )
                            HowItWorksItem(
                                title = "Organizer tools",
                                body = "Verified organizers can post gigs and view basic engagement insights."
                            )
                            HowItWorksItem(
                                title = "Artists",
                                body = "Browse artists, follow them, and find gigs from creators you like."
                            )

                            Spacer(Modifier.height(4.dp))

                            Text(
                                text = "Tip: You can open this again anytime from the welcome screen.",
                                fontSize = 11.sp,
                                color = Color(0xFF6B7280)
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Card-style row for each How it works bullet.
 */
@Composable
private fun HowItWorksItem(
    title: String,
    body: String
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color(0xFF020818),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(
                horizontal = 14.dp,
                vertical = 10.dp
            )
        ) {
            Text(
                text = title,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFFF9FAFB)
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = body,
                fontSize = 12.sp,
                color = Color(0xFF9CA3AF),
                lineHeight = 15.sp
            )
        }
    }
}

/**
 * Animated glowing orb background.
 * Declared as BoxScope extension so we can call align().
 */
@Composable
private fun BoxScope.OrbBackground() {
    val infinite = rememberInfiniteTransition(label = "orb")

    val scale by infinite.animateFloat(
        initialValue = 0.92f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(2600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "orb-scale"
    )

    val alpha by infinite.animateFloat(
        initialValue = 0.32f,
        targetValue = 0.6f,
        animationSpec = infiniteRepeatable(
            animation = tween(2600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "orb-alpha"
    )

    Box(
        modifier = Modifier
            .size(420.dp)
            .align(Alignment.Center)
            .graphicsLayer(
                scaleX = scale,
                scaleY = scale
            )
            .clip(CircleShape)
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF38BDF8),
                        Color(0xFF0F172A),
                        Color(0xFF02030A)
                    )
                )
            )
            .alpha(alpha)
    )
}
