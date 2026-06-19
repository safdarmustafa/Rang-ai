package com.example.rangai.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.rangai.ui.theme.DarkBackgroundStart
import com.example.rangai.ui.theme.GradientMaroonEnd
import com.example.rangai.ui.theme.GradientMaroonStart
import com.example.rangai.ui.theme.TextPrimary
import com.example.rangai.ui.theme.TextSecondary

@Composable
fun PremiumLoadingOverlay(
    message: String = "Enhancing your photo..."
) {
    val infiniteTransition = rememberInfiniteTransition(label = "loadingPulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackgroundStart.copy(alpha = 0.85f)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(64.dp)
                        .alpha(pulseAlpha),
                    color = GradientMaroonEnd,
                    strokeWidth = 3.dp,
                    trackColor = GradientMaroonStart.copy(alpha = 0.2f)
                )
                CircularProgressIndicator(
                    modifier = Modifier.size(48.dp),
                    color = GradientMaroonStart,
                    strokeWidth = 2.dp,
                    trackColor = GradientMaroonEnd.copy(alpha = 0.1f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = message,
                color = TextPrimary,
                fontWeight = FontWeight.Medium,
                style = androidx.compose.material3.MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "AI is working its magic",
                color = TextSecondary,
                style = androidx.compose.material3.MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun PremiumImageLoading(
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "shimmer")
    val shimmerOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmerOffset"
    )

    Box(
        modifier = modifier
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        GradientMaroonStart.copy(alpha = 0.1f),
                        GradientMaroonEnd.copy(alpha = 0.2f),
                        GradientMaroonStart.copy(alpha = 0.1f)
                    ),
                    start = androidx.compose.ui.geometry.Offset(
                        shimmerOffset * 1000f,
                        0f
                    ),
                    end = androidx.compose.ui.geometry.Offset(
                        shimmerOffset * 1000f + 400f,
                        400f
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = GradientMaroonEnd,
            strokeWidth = 2.dp,
            modifier = Modifier.size(36.dp)
        )
    }
}
