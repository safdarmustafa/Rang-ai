package com.example.rangai.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import com.example.rangai.ui.theme.DarkBackgroundEnd
import com.example.rangai.ui.theme.DarkBackgroundStart
import com.example.rangai.ui.theme.GradientBurgundyEnd
import com.example.rangai.ui.theme.MaroonPrimary

@Composable
fun PremiumBackground(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        DarkBackgroundStart,
                        DarkBackgroundEnd,
                        GradientBurgundyEnd.copy(alpha = 0.15f)
                    )
                )
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            MaroonPrimary.copy(alpha = 0.12f),
                            DarkBackgroundStart.copy(alpha = 0f)
                        ),
                        radius = 800f
                    )
                )
        )
        content()
    }
}
