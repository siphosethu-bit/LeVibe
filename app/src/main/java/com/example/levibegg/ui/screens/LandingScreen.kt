package com.example.levibegg.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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

@Composable
fun LandingScreen(
    onGetStarted: () -> Unit,
    onHowItWorks: () -> Unit
) {
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
            // Glowing animated orb in the back
            OrbBackground()

            // Foreground content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // "Where to.. Next?"
                Text(
                    text = "Where to.. Next?",
                    color = Color(0xFF38BDF8),
                    fontSize = 30.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 2.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(24.dp))

                // "lé Vibe" title
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

                // Tagline
                Text(
                    text = "Browse events by genre and budget, preview the area, set one-tap reminders, and split costs with your crew all in one place.",
                    color = Color(0xFFE5E5E5),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Light,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(0.9f)
                )

                Spacer(Modifier.height(32.dp))

                // Buttons row
                Row(
                    modifier = Modifier.wrapContentWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Get started
                    Button(
                        onClick = onGetStarted,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Color(0xFF111827)
                        ),
                        shape = CircleShape,
                        contentPadding = PaddingValues(
                            horizontal = 24.dp,
                            vertical = 10.dp
                        )
                    ) {
                        Text(
                            text = "Get started",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    // How it works
                    Button(
                        onClick = onHowItWorks,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF111827),
                            contentColor = Color.White
                        ),
                        shape = CircleShape,
                        contentPadding = PaddingValues(
                            horizontal = 24.dp,
                            vertical = 10.dp
                        )
                    ) {
                        Text(
                            text = "How it works",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            // Subtle dark gradient overlay from bottom
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
        }
    }
}

/**
 * Simple animated glowing orb background inspired by the web hero.
 * Declared as BoxScope extension so we can use align().
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
