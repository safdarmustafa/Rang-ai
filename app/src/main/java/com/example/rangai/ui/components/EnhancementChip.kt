package com.example.rangai.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.HighQuality
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.rangai.ui.theme.BurgundySecondary
import com.example.rangai.ui.theme.ChipShape
import com.example.rangai.ui.theme.DarkSurfaceVariant
import com.example.rangai.ui.theme.GradientMaroonEnd
import com.example.rangai.ui.theme.GradientMaroonStart
import com.example.rangai.ui.theme.GlassBorder
import com.example.rangai.ui.theme.SoftRoseAccent
import com.example.rangai.ui.theme.TextPrimary
import com.example.rangai.ui.theme.TextSecondary
import com.example.rangai.ui.theme.TextTertiary
import com.example.rangai.ui.theme.WarmRedAccent

@Composable
fun EnhancementChip(
    label: String,
    subtitle: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (selected) BurgundySecondary.copy(alpha = 0.55f)
        else DarkSurfaceVariant.copy(alpha = 0.9f),
        animationSpec = tween(250),
        label = "chipBg"
    )
    val borderColor by animateColorAsState(
        targetValue = if (selected) WarmRedAccent else GlassBorder,
        animationSpec = tween(250),
        label = "chipBorder"
    )

    Column(
        modifier = modifier
            .clip(ChipShape)
            .background(backgroundColor)
            .border(
                width = if (selected) 1.5.dp else 1.dp,
                brush = if (selected) {
                    Brush.horizontalGradient(
                        colors = listOf(GradientMaroonStart, GradientMaroonEnd)
                    )
                } else {
                    Brush.horizontalGradient(
                        colors = listOf(borderColor, borderColor)
                    )
                },
                shape = ChipShape
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (selected) WarmRedAccent else SoftRoseAccent.copy(alpha = 0.9f),
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = label,
            color = TextPrimary,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.SemiBold,
            style = androidx.compose.material3.MaterialTheme.typography.titleSmall
        )
        Text(
            text = subtitle,
            color = if (selected) TextSecondary else TextTertiary,
            style = androidx.compose.material3.MaterialTheme.typography.labelSmall
        )
    }
}

@Composable
fun EnhancementChipRow(
    hdSelected: Boolean,
    onHdClick: () -> Unit,
    onUltraHdClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        EnhancementChip(
            label = "HD",
            subtitle = "2× Upscale",
            icon = Icons.Outlined.HighQuality,
            selected = hdSelected,
            onClick = onHdClick,
            modifier = Modifier.weight(1f)
        )
        EnhancementChip(
            label = "Ultra HD",
            subtitle = "4× Upscale",
            icon = Icons.Outlined.AutoAwesome,
            selected = !hdSelected,
            onClick = onUltraHdClick,
            modifier = Modifier.weight(1f)
        )
    }
}
