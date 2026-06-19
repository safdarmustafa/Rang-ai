package com.example.rangai.ui.compare

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.rangai.ui.components.BeforeAfterSlider
import com.example.rangai.ui.components.GlassCard
import com.example.rangai.ui.components.PremiumBackground
import com.example.rangai.ui.theme.TextSecondary
import com.example.rangai.ui.theme.TextTertiary

@Composable
fun CompareScreen(
    originalImageUri: String,
    enhancedImageUrl: String,
    navController: NavController
) {
    PremiumBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .padding(top = 16.dp, bottom = 32.dp)
        ) {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }

            AnimatedVisibility(
                visible = true,
                enter = fadeIn() + slideInVertically { it / 4 }
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Before & After",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Drag the slider to compare",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                BeforeAfterSlider(
                    originalImage = originalImageUri,
                    enhancedImage = enhancedImageUrl,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Slide left or right to reveal the original vs enhanced image",
                style = MaterialTheme.typography.bodySmall,
                color = TextTertiary,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}
