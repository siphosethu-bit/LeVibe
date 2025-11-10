package com.example.levibegg.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
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
 * LéVibe glassmorphism button.
 *
 * - Frosted / glass look
 * - Border + subtle gradient
 * - Scale + glow on press for clear feedback
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

    // Press animation
    val scale = animateFloatAsState(
        targetValue = if (pressed) 0.96f else 1f,
        label = "glass_btn_scale"
    ).value

    // Colors tuned to your dark theme
    val borderColor =
        if (isPrimary) Color(0xFF38BDF8).copy(alpha = 0.9f)
        else Color.White.copy(alpha = 0.35f)

    // Slightly stronger when pressed
    val backgroundBrush =
        if (isPrimary) {
            Brush.linearGradient(
                colors = listOf(
                    Color(0xFF38BDF8).copy(alpha = if (pressed) 0.30f else 0.22f),
                    Color(0xFF22C55E).copy(alpha = if (pressed) 0.18f else 0.10f)
                )
            )
        } else {
            Brush.linearGradient(
                colors = listOf(
                    Color.White.copy(alpha = if (pressed) 0.10f else 0.06f),
                    Color.White.copy(alpha = if (pressed) 0.06f else 0.02f)
                )
            )
        }

    Box(
        modifier = modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                shape = RoundedCornerShape(30.dp)
                clip = true
            }
            .background(brush = backgroundBrush)
            .border(
                BorderStroke(1.dp, borderColor),
                shape = RoundedCornerShape(30.dp)
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null, // use our own visual feedback instead of deprecated ripple
                onClick = onClick
            )
            .padding(horizontal = 22.dp, vertical = 10.dp),
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
