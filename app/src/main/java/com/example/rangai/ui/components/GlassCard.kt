package com.example.rangai.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.rangai.ui.theme.CardShape
import com.example.rangai.ui.theme.DarkSurface
import com.example.rangai.ui.theme.GlassBorder
import com.example.rangai.ui.theme.GlassWhite

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    animate: Boolean = true,
    elevation: Dp = 0.dp,
    content: @Composable BoxScope.() -> Unit
) {
    val alpha by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = if (animate) 500 else 0),
        label = "cardAlpha"
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .alpha(alpha),
        shape = CardShape,
        color = DarkSurface.copy(alpha = 0.88f),
        shadowElevation = elevation,
        border = BorderStroke(1.dp, GlassBorder),
        tonalElevation = 2.dp
    ) {
        Box(
            modifier = Modifier.background(GlassWhite),
            content = content
        )
    }
}
