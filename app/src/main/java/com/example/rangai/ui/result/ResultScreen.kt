package com.example.rangai.ui.result

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.CompareArrows
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import com.example.rangai.navigation.Screen
import com.example.rangai.ui.components.GlassCard
import com.example.rangai.ui.components.GradientButton
import com.example.rangai.ui.components.OutlinedGradientButton
import com.example.rangai.ui.components.PremiumBackground
import com.example.rangai.ui.components.PremiumImageLoading
import com.example.rangai.ui.theme.ImagePreviewShape
import com.example.rangai.ui.theme.SuccessGreen
import com.example.rangai.ui.theme.TextSecondary
import com.example.rangai.ui.theme.TextTertiary
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.net.URL

@Composable
fun ResultScreen(
    imageUrl: String,
    originalImageUri: String?,
    navController: NavController
) {
    val context = LocalContext.current

    PremiumBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
                .padding(top = 56.dp, bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedVisibility(
                visible = true,
                enter = fadeIn() + slideInVertically { it / 4 }
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Enhancement Complete",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Your photo has been enhanced with AI",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(420.dp)
            ) {
                SubcomposeAsyncImage(
                    model = imageUrl,
                    contentDescription = "Enhanced Image",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(ImagePreviewShape),
                    contentScale = ContentScale.Fit,
                    loading = {
                        PremiumImageLoading(modifier = Modifier.fillMaxSize())
                    }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "✦ AI Enhanced",
                style = MaterialTheme.typography.labelMedium,
                color = SuccessGreen
            )

            Spacer(modifier = Modifier.height(28.dp))

            GradientButton(
                text = "Download Image",
                onClick = {
                    downloadImage(
                        context = context,
                        imageUrl = imageUrl
                    )
                },
                icon = Icons.Outlined.Download
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedGradientButton(
                text = "Share Image",
                onClick = {
                    shareImage(
                        context = context,
                        imageUrl = imageUrl
                    )
                },
                icon = Icons.Outlined.Share
            )

            if (!originalImageUri.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedGradientButton(
                    text = "Compare Before & After",
                    onClick = {
                        navController.navigate(
                            Screen.Compare.createRoute(
                                originalUri = originalImageUri,
                                enhancedUrl = imageUrl
                            )
                        )
                    },
                    icon = Icons.AutoMirrored.Filled.CompareArrows
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Tip: Use Compare to see the difference",
                style = MaterialTheme.typography.bodySmall,
                color = TextTertiary
            )
        }
    }
}

fun downloadImage(
    context: Context,
    imageUrl: String
) {
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val url = URL(imageUrl)
            val inputStream = url.openStream()
            val fileName = "RangAI_${System.currentTimeMillis()}.png"

            val values = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
                put(MediaStore.Images.Media.MIME_TYPE, "image/png")
                put(
                    MediaStore.Images.Media.RELATIVE_PATH,
                    Environment.DIRECTORY_PICTURES + "/RangAI"
                )
            }

            val uri = context.contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                values
            )

            uri?.let {
                val outputStream = context.contentResolver.openOutputStream(it)
                inputStream.copyTo(outputStream!!)
                outputStream.close()
                inputStream.close()

                android.os.Handler(android.os.Looper.getMainLooper()).post {
                    Toast.makeText(
                        context,
                        "Image Saved Successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } catch (e: Exception) {
            android.os.Handler(android.os.Looper.getMainLooper()).post {
                Toast.makeText(
                    context,
                    "Download Failed",
                    Toast.LENGTH_SHORT
                ).show()
            }
            e.printStackTrace()
        }
    }
}

fun shareImage(
    context: Context,
    imageUrl: String
) {
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val url = URL(imageUrl)
            val inputStream = url.openStream()
            val file = File(context.cacheDir, "shared_image.png")
            val outputStream = FileOutputStream(file)
            inputStream.copyTo(outputStream)
            outputStream.close()
            inputStream.close()

            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.provider",
                file
            )

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "image/*"
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            context.startActivity(
                Intent.createChooser(shareIntent, "Share Image")
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
