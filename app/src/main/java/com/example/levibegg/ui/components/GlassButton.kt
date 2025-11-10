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
 * Primary glassmorphism pill button.
 * Used on the Landing screen for "Get started" / "How it works".
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

    // Subtle press animation
    val scale = animateFloatAsState(
        targetValue = if (pressed) 0.96f else 1f,
        label = "glass_btn_scale"
    ).value

    val borderColor =
        if (isPrimary) Color(0xFF38BDF8).copy(alpha = 0.9f)
        else Color.White.copy(alpha = 0.40f)

    val backgroundBrush =
        if (isPrimary) {
            // Slight glow / accent for main action
            Brush.linearGradient(
                colors = listOf(
                    Color(0xFF38BDF8).copy(alpha = 0.30f),
                    Color(0xFF22C55E).copy(alpha = 0.10f)
                )
            )
        } else {
            // Neutral frosted glass
            Brush.linearGradient(
                colors = listOf(
                    Color.White.copy(alpha = 0.10f),
                    Color.White.copy(alpha = 0.03f)
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
            }
            .background(backgroundBrush)
            .border(
                BorderStroke(1.dp, borderColor),
                shape = CircleShape
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null, // custom visual feedback only
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

/**
 * Glass role selection card used on the RoleSelectScreen.
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
            Color.White.copy(alpha = 0.05f),
            Color.White.copy(alpha = 0.015f)
        )
    )

    val borderColor = Color.White.copy(alpha = 0.14f)

    Box(
        modifier = modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                shape = RoundedCornerShape(18.dp)
                clip = true
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
                fontSize = 16.sp,
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
