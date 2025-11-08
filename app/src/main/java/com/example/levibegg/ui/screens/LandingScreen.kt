package com.example.levibegg.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LandingScreen(
    onGetStarted: () -> Unit,
    onHowItWorks: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF02030A), Color(0xFF050814))
                )
            )
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "New • SA Gig Guide",
                color = Color(0xFFFFC857),
                fontSize = 14.sp
            )

            Spacer(Modifier.height(16.dp))

            Text(
                buildAnnotatedString {
                    append("Book ")
                    withStyle(SpanStyle(color = Color(0xFFE94EFF), fontWeight = FontWeight.ExtraBold)) {
                        append("the vibe")
                    }
                    append(". ")
                    withStyle(SpanStyle(color = Color(0xFFFFC857), fontWeight = FontWeight.ExtraBold)) {
                        append("Plan")
                    }
                    append(" the night.\n")
                    withStyle(SpanStyle(color = Color(0xFF73F7FF), fontWeight = FontWeight.ExtraBold)) {
                        append("Arrive")
                    }
                    append(" together.")
                },
                color = Color.White,
                fontSize = 30.sp,
                lineHeight = 34.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = "Browse events by genre & budget, preview the area, set reminders and split costs with your crew.",
                color = Color(0xFFEEEEEE),
                fontSize = 14.sp
            )

            Spacer(Modifier.height(28.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(onClick = onGetStarted) {
                    Text("Get started")
                }
                Button(onClick = onHowItWorks) {
                    Text("How it works")
                }
            }
        }
    }
}
