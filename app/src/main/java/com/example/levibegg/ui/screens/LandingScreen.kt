package com.example.levibegg.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.levibegg.R
import com.example.levibegg.ui.components.GlassButton

// Custom font families (files in res/font)
val AmboeraScript = FontFamily(Font(R.font.amboera_script))
val Amsterdam = FontFamily(Font(R.font.amsterdam_one))

@Composable
fun LandingScreen(
    onGetStarted: () -> Unit,
    // Optional external hook if you ever need it
    onHowItWorks: () -> Unit = {}
) {
    var showHowItWorks by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF000000)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF000000),
                            Color(0xFF000000)
                        )
                    )
                )
        ) {
            // Glowing animated orb behind everything
            OrbBackground()

            // Centered hero content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Where to.. Next?",
                    color = Color.White,
                    fontSize = 30.sp,
                    fontFamily = AmboeraScript,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(24.dp))

                Text(
                    text = "lé Vibe",
                    color = Color(0xFFE5E5E5),
                    fontSize = 80.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = Amsterdam,
                    letterSpacing = 4.sp,
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

                    // Secondary CTA -> opens in-app "How it works" sheet
                    GlassButton(
                        text = "How it works",
                        onClick = {
                            showHowItWorks = true
                            onHowItWorks()
                        }
                    )
                }
            }

            // Bottom fade for depth so content anchors nicely
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color(0xCC000000)
                            )
                        )
                    )
            )

            // How it works overlay
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
                            // Header row
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
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
                                        .padding(horizontal = 10.dp, vertical = 4.dp)
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
                                body = "View quick safety notes and ride options near each event."
                            )
                            HowItWorksItem(
                                title = "Area preview + rides",
                                body = "Check the venue on the map and estimate rides to and from the event."
                            )
                            HowItWorksItem(
                                title = "Plan with friends",
                                body = "Share plans so everyone sees ride splits and details."
                            )
                            HowItWorksItem(
                                title = "Budget filter",
                                body = "Slide your max budget to instantly hide pricey events."
                            )
                            HowItWorksItem(
                                title = "Organizer tools",
                                body = "Verified organizers can post gigs and view engagement."
                            )
                            HowItWorksItem(
                                title = "Artists",
                                body = "Follow artists and never miss their next set."
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
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
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
 * Animated glowing orb background behind the hero.
 */
@Composable
private fun BoxScope.OrbBackground() {
    val infinite = rememberInfiniteTransition(label = "orb")

    val pulse by infinite.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "orb-pulse"
    )

    val glowAlpha by infinite.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(2600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "orb-alpha"
    )

    val driftX by infinite.animateFloat(
        initialValue = -20f,
        targetValue = 20f,
        animationSpec = infiniteRepeatable(
            animation = tween(4800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "orb-drift-x"
    )

    val driftY by infinite.animateFloat(
        initialValue = 14f,
        targetValue = -14f,
        animationSpec = infiniteRepeatable(
            animation = tween(5200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "orb-drift-y"
    )

    val morph by infinite.animateFloat(
        initialValue = 1.0f,
        targetValue = 2.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(2600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "orb-morph"
    )

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .align(Alignment.Center)
    ) {
        val center = Offset(
            x = size.width / 2f,
            y = size.height * 0.40f
        )

        val baseRadius = size.minDimension * 0.35f

        val auraR = baseRadius * 2.2f * pulse
        val ringR = baseRadius * 1.4f * pulse
        val innerR = baseRadius * 0.9f * pulse
        val highlightR = baseRadius * 0.55f * pulse

        fun ovalSize(r: Float) = Size(
            width = r * 2f * morph,
            height = r * 2f
        )

        // Outer aura
        drawOval(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0x802196F3),
                    Color(0x402196F3),
                    Color.Transparent
                ),
                center = center,
                radius = auraR
            ),
            topLeft = Offset(center.x - auraR * morph, center.y - auraR),
            size = ovalSize(auraR),
            alpha = glowAlpha
        )

        // Neon ring
        drawOval(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color.Transparent,
                    Color(0xFFA855F7),
                    Color(0xFFA855F7),
                    Color.Transparent
                ),
                center = center,
                radius = ringR
            ),
            topLeft = Offset(center.x - ringR * morph, center.y - ringR),
            size = ovalSize(ringR),
            alpha = glowAlpha
        )

        // Inner core
        drawOval(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFF000000),
                    Color(0xCC000000),
                    Color.Transparent
                ),
                center = center,
                radius = innerR
            ),
            topLeft = Offset(center.x - innerR * morph, center.y - innerR),
            size = ovalSize(innerR)
        )

        // Moving highlight
        val hCenter = Offset(
            x = center.x + driftX,
            y = center.y + driftY
        )

        drawOval(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0x66FFFFFF),
                    Color.Transparent
                ),
                center = hCenter,
                radius = highlightR
            ),
            topLeft = Offset(hCenter.x - highlightR * morph, hCenter.y - highlightR),
            size = ovalSize(highlightR),
            alpha = 0.35f
        )
    }
}
