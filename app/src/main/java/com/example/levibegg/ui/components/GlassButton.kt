package com.example.levibegg.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * UNIVERSAL GLASS BUTTON
 * Used across Events, Landing, Tickets, Rides, etc.
 *
 * - Frosted glass background
 * - Subtle neon border
 * - Press animation (squish)
 * - Perfect for dark UI themes
 */
@Composable
fun GlassButton(
    text: String,
    modifier: Modifier = Modifier,
    isPrimary: Boolean = false,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed = interactionSource.collectIsPressedAsState().value

    // Press animation effect
    val scale = animateFloatAsState(
        targetValue = if (pressed) 0.96f else 1f,
        label = "glass_btn_scale"
    ).value

    // Border color: blue glow for primary, white glow for neutral
    val borderColor =
        if (isPrimary) Color(0xFF38BDF8).copy(alpha = 0.70f)
        else Color.White.copy(alpha = 0.28f)

    // Background gradient: soft glass with tint
    val backgroundBrush =
        if (isPrimary) {
            Brush.linearGradient(
                colors = listOf(
                    Color(0xFF38BDF8).copy(alpha = 0.22f),
                    Color(0xFF22C55E).copy(alpha = 0.12f)
                )
            )
        } else {
            Brush.linearGradient(
                colors = listOf(
                    Color.White.copy(alpha = 0.12f),
                    Color.White.copy(alpha = 0.04f)
                )
            )
        }

    Box(
        modifier = modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                shape = CircleShape
                clip = true
                shadowElevation = 18f
                ambientShadowColor = Color.Black.copy(alpha = 0.45f)
                spotShadowColor = Color.Black.copy(alpha = 0.60f)
            }
            .background(backgroundBrush)
            .border(
                BorderStroke(1.3.dp, borderColor),
                shape = CircleShape
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = 22.dp, vertical = 11.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = if (isPrimary) FontWeight.SemiBold else FontWeight.Medium,
            style = MaterialTheme.typography.labelLarge
        )
    }
}

/**
 * GLASS CARD (Used in RoleSelectScreen)
 * Enhances visual consistency across the whole app.
 */
@Composable
fun GlassRoleCard(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed = interactionSource.collectIsPressedAsState().value

    val scale = animateFloatAsState(
        targetValue = if (pressed) 0.97f else 1f,
        label = "glass_role_scale"
    ).value

    val backgroundBrush = Brush.linearGradient(
        colors = listOf(
            Color.White.copy(alpha = 0.08f),
            Color.White.copy(alpha = 0.03f)
        )
    )

    val borderColor = Color.White.copy(alpha = 0.16f)

    Box(
        modifier = modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                shape = RoundedCornerShape(18.dp)
                clip = true
                shadowElevation = 14f
            }
            .background(backgroundBrush)
            .border(
                BorderStroke(1.dp, borderColor),
                shape = RoundedCornerShape(18.dp)
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = 18.dp, vertical = 14.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Column {
            Text(
                text = title,
                color = Color.White,
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.padding(top = 2.dp))
            Text(
                text = subtitle,
                color = Color(0xFF9CA3AF),
                fontSize = 12.sp
            )
        }
    }
}
