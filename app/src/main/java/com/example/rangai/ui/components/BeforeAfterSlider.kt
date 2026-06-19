package com.example.rangai.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.CompareArrows
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.rangai.ui.theme.ImagePreviewShape
import com.example.rangai.ui.theme.MaroonPrimary
import com.example.rangai.ui.theme.TextPrimary
import com.example.rangai.ui.theme.WarmRedAccent
import kotlin.math.roundToInt

@Composable
fun BeforeAfterSlider(
    originalImage: Any?,
    enhancedImage: Any?,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modifier.clip(ImagePreviewShape)
    ) {
        val maxWidthPx = constraints.maxWidth.toFloat()
        val maxHeightPx = constraints.maxHeight.toFloat()
        var sliderPosition by remember { mutableFloatStateOf(0.5f) }
        val sliderOffsetPx = sliderPosition * maxWidthPx
        val density = LocalDensity.current
        val handleHalfPx = with(density) { 20.dp.toPx() }

        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = enhancedImage,
                contentDescription = "Enhanced Image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(with(density) { sliderOffsetPx.toDp() })
                    .clipToBounds()
            ) {
                AsyncImage(
                    model = originalImage,
                    contentDescription = "Original Image",
                    modifier = Modifier
                        .fillMaxSize()
                        .width(with(density) { maxWidthPx.toDp() }),
                    contentScale = ContentScale.Crop
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(3.dp)
                    .offset { IntOffset(sliderOffsetPx.roundToInt() - 1, 0) }
                    .background(WarmRedAccent)
            )

            Box(
                modifier = Modifier
                    .offset {
                        IntOffset(
                            (sliderOffsetPx - handleHalfPx).roundToInt(),
                            (maxHeightPx / 2f - handleHalfPx).roundToInt()
                        )
                    }
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaroonPrimary)
                    .pointerInput(maxWidthPx) {
                        detectHorizontalDragGestures { change, dragAmount ->
                            change.consume()
                            val newPosition = (sliderPosition + dragAmount / maxWidthPx)
                                .coerceIn(0.05f, 0.95f)
                            sliderPosition = newPosition
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.CompareArrows,
                    contentDescription = "Drag to compare",
                    tint = TextPrimary,
                    modifier = Modifier.size(22.dp)
                )
            }

            Text(
                text = "Original",
                color = TextPrimary,
                fontWeight = FontWeight.SemiBold,
                style = androidx.compose.material3.MaterialTheme.typography.labelMedium,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(12.dp)
                    .background(
                        color = Color.Black.copy(alpha = 0.55f),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            )

            Text(
                text = "Enhanced",
                color = TextPrimary,
                fontWeight = FontWeight.SemiBold,
                style = androidx.compose.material3.MaterialTheme.typography.labelMedium,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp)
                    .background(
                        color = Color.Black.copy(alpha = 0.55f),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            )
        }
    }
}

@Composable
fun ComparisonLabel(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        color = TextPrimary,
        fontWeight = FontWeight.SemiBold,
        style = androidx.compose.material3.MaterialTheme.typography.labelLarge,
        modifier = modifier
            .background(
                color = MaroonPrimary.copy(alpha = 0.85f),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 12.dp, vertical = 6.dp)
    )
}
