package com.example.levibegg.ui.screens

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.levibegg.R
import com.example.levibegg.ui.components.GlassButton
import kotlinx.coroutines.delay
import android.content.Context
import android.location.LocationManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.layout.offset



// Custom font families (files in res/font)
val AmboeraScript = FontFamily(Font(R.font.amboera_script))
val Amsterdam = FontFamily(Font(R.font.amsterdam_one))

/**
 * Landing flow:
 * 1 -> Splash (orb + Where to.. Next? + lé Vibe) for 0.5s
 * 2 -> Sign in (demo Google email + fixed password 123456)
 * 3 -> Location permission screen, then onGetStarted() -> events
 */

private fun isLocationEnabled(context: Context): Boolean {
    val lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
            lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
}





@Composable
fun LandingScreen(
    onGetStarted: () -> Unit
) {
    var step by remember { mutableStateOf(1) }
    var email by remember { mutableStateOf("") }
    var loginError by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    var locationError by remember { mutableStateOf<String?>(null) }


    // Location permission launcher
    val locationLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (!granted) {
            // User denied the permission
            locationError = "Location permission is required to find nearby events."
            return@rememberLauncherForActivityResult
        }

        // Permission is granted – now check if device location is actually ON
        if (!isLocationEnabled(context)) {
            locationError = "Please turn on Location / GPS in your phone settings."
            return@rememberLauncherForActivityResult
        }

        // ✅ Permission granted and GPS is ON – now we allow them into the app
        locationError = null
        onGetStarted()
    }


    // After 0.5s move from splash -> sign-in
    LaunchedEffect(Unit) {
        delay(500)
        step = 2
    }

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

            when (step) {
                1 -> SplashContent()
                2 -> SignInContent(
                    email = email,
                    onEmailChange = { email = it },
                    loginError = loginError,
                    onContinue = {
                        if (email.isBlank()) {
                            loginError = "Please enter your Google email (demo only)."
                        } else {
                            loginError = null
                            step = 3
                        }
                    }
                )

                3 -> LocationContent(
                    errorMessage = locationError,
                    onAllowLocation = {
                        locationLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                    },
                    onSkip = {
                        // If you want to FORCE location, do nothing here:
                        // locationError = "Location is required to use lé Vibe."
                        // For now, we just don't navigate.
                    }
                )
            }


            // Subtle bottom fade for depth
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
 * STEP 1: 0.5s splash with just orb + tagline + logo.
 */
@Composable
private fun BoxScope.SplashContent() {
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
    }
}

/**
 * STEP 2: Sign in screen (demo).
 * Asks for Google email + shows fixed password 123456.
 */
@Composable
private fun BoxScope.SignInContent(
    email: String,
    onEmailChange: (String) -> Unit,
    loginError: String?,
    onContinue: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.BottomCenter)
            .padding(horizontal = 24.dp, vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "lé Vibe",
            color = Color(0xFFE5E5E5),
            fontSize = 46.sp,
            fontWeight = FontWeight.ExtraBold,
            fontFamily = Amsterdam,
            letterSpacing = 4.sp,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(16.dp))

        Text(
            text = "Sign in to find the vibe near you",
            color = Color(0xFF9CA3AF),
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(24.dp))

        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color(0xCC020617),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Continue with Google (demo)",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = onEmailChange,
                    label = { Text("Google email") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = "123456",
                    onValueChange = {},
                    label = { Text("Password (fixed for demo)") },
                    singleLine = true,
                    enabled = false,
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                if (loginError != null) {
                    Text(
                        text = loginError,
                        color = Color(0xFFFF6B6B),
                        fontSize = 12.sp
                    )
                }

                Spacer(Modifier.height(8.dp))

                GlassButton(
                    text = "Continue",
                    isPrimary = true,
                    onClick = onContinue
                )

                Text(
                    text = "This is a demo sign-in. No real Google account is used yet.",
                    color = Color(0xFF6B7280),
                    fontSize = 11.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

/**
 * STEP 3: Location permission screen.
 * After Allow -> request permission then call onGetStarted().
 */
@Composable
private fun BoxScope.LocationContent(
    errorMessage: String?,
    onAllowLocation: () -> Unit,
    onSkip: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.BottomCenter)
            .padding( top = 60.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


// --- New top title (same as splash) ---
        Text(
            text = "Where to.. Next?",
            color = Color.White,
            fontSize = 20.sp,
            fontFamily = AmboeraScript,
            textAlign = TextAlign.Center,
            modifier = Modifier.offset(y = (-129).dp)
        )


        // --- ADD LOGO HERE (like login screen) ---
        Text(
            text = "lé Vibe",
            color = Color(0xFFE5E5E5),
            fontSize = 52.sp,
            fontWeight = FontWeight.ExtraBold,
            fontFamily = Amsterdam,
            letterSpacing = 4.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.offset(y = (-110).dp)   // ⬅ move logo UP by 40dp
        )


        Spacer(Modifier.height(6.dp))

        // Sub-text (instead of "Almost there ⚡")
        Text(
            text = "Allow location to help us find events near you",
            color = Color(0xFF9CA3AF),
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        // Error message if needed
        if (errorMessage != null) {
            Spacer(Modifier.height(8.dp))
            Text(
                text = errorMessage,
                color = Color(0xFFFF6B6B),
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(Modifier.height(20.dp))

        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color(0xCC020617),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Why we ask",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "• Find events close to you\n" +
                            "• Show safer routes and areas\n" +
                            "• Better suggestions based on your city",
                    color = Color(0xFF9CA3AF),
                    fontSize = 12.sp
                )

                Spacer(Modifier.height(12.dp))

                GlassButton(
                    text = "Allow location",
                    isPrimary = true,
                    onClick = onAllowLocation
                )

                GlassButton(
                    text = "Not now",
                    onClick = onSkip
                )
            }
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
            y = size.height * 0.4f
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