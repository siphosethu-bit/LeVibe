package com.example.levibegg.ui.components

import android.graphics.RenderEffect
import android.graphics.Shader
import android.net.Uri
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.example.levibegg.R

/**
 * Full-screen looping video background for the role gate,
 * with blur + dim overlay so glass buttons are readable.
 */
@Composable
fun RoleGateVideoBackground(
    modifier: Modifier = Modifier,
    blurRadius: Float = 15f,        // overall blur intensity
    dimOverlayAlpha: Float = 0.55f  // darkness over the video
) {
    val context = LocalContext.current

    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            val uri = Uri.parse("android.resource://${context.packageName}/${R.raw.role_gate_background}")
            val mediaItem = MediaItem.fromUri(uri)
            setMediaItem(mediaItem)
            repeatMode = ExoPlayer.REPEAT_MODE_ALL
            playWhenReady = true
            volume = 0f
            prepare()
        }
    }

    DisposableEffect(Unit) {
        onDispose { exoPlayer.release() }
    }

    Box(modifier = modifier.fillMaxSize()) {

        // 1️⃣ Video itself (blurred)
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { ctx ->
                PlayerView(ctx).apply {
                    useController = false
                    player = exoPlayer
                    resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        setRenderEffect(
                            RenderEffect.createBlurEffect(
                                blurRadius,
                                blurRadius,
                                Shader.TileMode.CLAMP
                            )
                        )
                    } else {
                        // For older Android we keep it sharp here;
                        // dimming happens via the overlay below.
                        alpha = 1f
                    }
                }
            },
            update = { view ->
                // Re-apply blur if values change
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    view.setRenderEffect(
                        RenderEffect.createBlurEffect(
                            blurRadius,
                            blurRadius,
                            Shader.TileMode.CLAMP
                        )
                    )
                } else {
                    view.alpha = 1f
                }
            }
        )

        // 2️⃣ Dark overlay above the video to improve contrast for text/cards
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = dimOverlayAlpha))
        )
    }
}
