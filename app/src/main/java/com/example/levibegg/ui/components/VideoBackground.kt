package com.example.levibegg.ui.components

import android.graphics.RenderEffect
import android.graphics.Shader
import android.net.Uri
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.example.levibegg.R

@Composable
fun RoleGateVideoBackground(
    modifier: Modifier = Modifier,
    blurX: Float = 100f,  // 👈 control horizontal blur intensity
    blurY: Float = 100f,  // 👈 control vertical blur intensity
    dimAlpha: Float = 0.85f // 👈 fallback brightness for older Androids
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

    AndroidView(
        modifier = modifier.fillMaxSize(),
        factory = { ctx ->
            PlayerView(ctx).apply {
                useController = false
                player = exoPlayer
                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    // 👇 Apply customizable blur
                    setRenderEffect(
                        RenderEffect.createBlurEffect(blurX, blurY, Shader.TileMode.CLAMP)
                    )
                } else {
                    // 👇 Darken slightly for readability
                    alpha = dimAlpha
                }
            }
        }
    )
}
