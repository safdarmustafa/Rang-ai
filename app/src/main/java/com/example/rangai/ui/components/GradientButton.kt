package com.example.rangai.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.rangai.ui.theme.ButtonShape
import com.example.rangai.ui.theme.GradientMaroonEnd
import com.example.rangai.ui.theme.GradientMaroonStart
import com.example.rangai.ui.theme.TextPrimary
import com.example.rangai.ui.theme.TextSecondary
import com.example.rangai.ui.theme.WarmRedAccent

@Composable
fun GradientButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    icon: ImageVector? = null
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed && enabled) 0.97f else 1f,
        animationSpec = tween(100),
        label = "buttonScale"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(54.dp)
            .scale(scale)
            .clip(ButtonShape)
            .background(
                brush = if (enabled) {
                    Brush.horizontalGradient(
                        colors = listOf(GradientMaroonStart, GradientMaroonEnd)
                    )
                } else {
                    Brush.horizontalGradient(
                        colors = listOf(
                            TextSecondary.copy(alpha = 0.3f),
                            TextSecondary.copy(alpha = 0.2f)
                        )
                    )
                }
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled && !isLoading,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = TextPrimary,
                strokeWidth = 2.dp
            )
        } else {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                icon?.let {
                    Icon(
                        imageVector = it,
                        contentDescription = null,
                        tint = TextPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Text(
                    text = text,
                    color = TextPrimary,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
fun OutlinedGradientButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        animationSpec = tween(100),
        label = "outlinedButtonScale"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(54.dp)
            .scale(scale)
            .clip(ButtonShape)
            .background(WarmRedAccent.copy(alpha = 0.12f))
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            icon?.let {
                Icon(
                    imageVector = it,
                    contentDescription = null,
                    tint = WarmRedAccent,
                    modifier = Modifier.size(20.dp)
                )
            }
            Text(
                text = text,
                color = WarmRedAccent,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
