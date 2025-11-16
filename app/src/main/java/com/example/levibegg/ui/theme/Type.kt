package com.example.levibegg.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.levibegg.R

// ✅ Custom font families (using files in res/font)

// Script font for logo etc.
val AmboeraScript = FontFamily(
    Font(R.font.amboera_script)          // res/font/amboera_script.ttf
)

// Amsterdam display font for headings like "Who are you?"
val AmsterdamOne = FontFamily(
    Font(R.font.amsterdam_one, FontWeight.Bold)   // res/font/amsterdam_one.ttf
)

// ──────────────────────────────────────────────────────────────
// Material3 Typography (you can customise later if you want)
// ──────────────────────────────────────────────────────────────
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    // You can add titleLarge, labelSmall etc. here if needed
)
