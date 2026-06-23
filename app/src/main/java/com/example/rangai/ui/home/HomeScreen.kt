package com.example.rangai.ui.home

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddPhotoAlternate
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.rangai.data.EnhancementType
import com.example.rangai.navigation.Screen
import com.example.rangai.ui.components.EnhancementChipRow
import com.example.rangai.ui.components.GlassCard
import com.example.rangai.ui.components.GradientButton
import com.example.rangai.ui.components.PremiumBackground
import com.example.rangai.ui.components.PremiumLoadingOverlay
import com.example.rangai.ui.components.PremiumTopBar
import com.example.rangai.ui.theme.ErrorRed
import com.example.rangai.ui.theme.ImagePreviewShape
import com.example.rangai.ui.theme.SoftRoseAccent
import com.example.rangai.ui.theme.TextPrimary
import com.example.rangai.ui.theme.TextSecondary
import com.example.rangai.ui.theme.TextTertiary
import com.example.rangai.ui.theme.WarmRedAccent
import com.example.rangai.viewmodel.HomeViewModel
import java.io.ByteArrayOutputStream

@Composable
fun HomeScreen(
    navController: NavController
) {
    val homeViewModel: HomeViewModel = viewModel()
    val context = LocalContext.current
    val isLoading by homeViewModel.isLoading.collectAsState()
    val errorMessage by homeViewModel.errorMessage.collectAsState()

    val enhancedImageUrl by homeViewModel
        .enhancedImageUrl
        .collectAsState()

    var selectedImageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    var selectedEnhancement by remember {
        mutableStateOf(EnhancementType.HD)
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        selectedImageUri = uri
        homeViewModel.clearError()
    }

    LaunchedEffect(enhancedImageUrl) {
        enhancedImageUrl?.let { imageUrl ->
            val originalUri = selectedImageUri?.toString() ?: ""
            navController.navigate(
                Screen.Result.createRoute(imageUrl, originalUri)
            ) {
                launchSingleTop = true
            }
            homeViewModel.clearEnhancedImage()
        }
    }

    PremiumBackground {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                PremiumTopBar(
                    title = "Rang AI",
                    subtitle = "Premium AI Photo Enhancement",
                    onProfileClick = {
                        navController.navigate(Screen.UserProfile.route)
                    }
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 20.dp)
                        .padding(top = 8.dp, bottom = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    HomeWelcomeSection()

                    Spacer(modifier = Modifier.height(20.dp))

                    ImagePreviewCard(selectedImageUri = selectedImageUri)

                    Spacer(modifier = Modifier.height(24.dp))

                    SectionLabel(text = "Enhancement Quality")

                    Spacer(modifier = Modifier.height(12.dp))

                    EnhancementChipRow(
                        hdSelected = selectedEnhancement == EnhancementType.HD,
                        onHdClick = { selectedEnhancement = EnhancementType.HD },
                        onUltraHdClick = { selectedEnhancement = EnhancementType.ULTRA_HD }
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    GradientButton(
                        text = "Select Image",
                        onClick = {
                            launcher.launch(
                                PickVisualMediaRequest(
                                    ActivityResultContracts.PickVisualMedia.ImageOnly
                                )
                            )
                        },
                        icon = Icons.Outlined.AddPhotoAlternate
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    if (errorMessage != null) {
                        Text(
                            text = errorMessage!!,
                            style = MaterialTheme.typography.bodyMedium,
                            color = ErrorRed,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp)
                        )
                    }

                    GradientButton(
                        text = if (isLoading) "Enhancing..." else "Enhance Image",
                        onClick = {
                            Log.d("RANG_AI", "Enhance button clicked")

                            selectedImageUri?.let { uri ->
                                Log.d("RANG_AI", "Uri found: $uri")

                                val originalBytes = context.contentResolver
                                    .openInputStream(uri)
                                    ?.readBytes()

                                if (originalBytes != null) {
                                    val bitmap = BitmapFactory.decodeByteArray(
                                        originalBytes,
                                        0,
                                        originalBytes.size
                                    )

                                    if (bitmap == null) {
                                        Log.e("RANG_AI", "Could not decode selected image")
                                        homeViewModel.reportError(
                                            "Could not read this image. Try a JPG or PNG from your gallery."
                                        )
                                        return@let
                                    }

                                    val originalWidth = bitmap.width
                                    val originalHeight = bitmap.height
                                    val maxDimension = maxOf(originalWidth, originalHeight)
                                    val scaleFactor = 1024f / maxDimension
                                    val newWidth = (originalWidth * scaleFactor).toInt()
                                    val newHeight = (originalHeight * scaleFactor).toInt()

                                    val resizedBitmap = Bitmap.createScaledBitmap(
                                        bitmap,
                                        newWidth,
                                        newHeight,
                                        true
                                    )

                                    val outputStream = ByteArrayOutputStream()
                                    resizedBitmap.compress(
                                        Bitmap.CompressFormat.JPEG,
                                        90,
                                        outputStream
                                    )

                                    val resizedBytes = outputStream.toByteArray()
                                    val scale = when (selectedEnhancement) {
                                        EnhancementType.HD -> 2
                                        EnhancementType.ULTRA_HD -> 4
                                    }

                                    homeViewModel.startEnhancing(
                                        imageBytes = resizedBytes,
                                        scale = scale
                                    )
                                } else {
                                    Log.e("RANG_AI", "Could not read image bytes from URI")
                                    homeViewModel.reportError(
                                        "Could not open the selected image. Please try another photo."
                                    )
                                }
                            }
                        },
                        enabled = selectedImageUri != null && !isLoading,
                        isLoading = isLoading,
                        icon = Icons.Outlined.AutoAwesome
                    )
                }

                if (isLoading) {
                    PremiumLoadingOverlay()
                }
            }
        }
    }
}

@Composable
private fun HomeWelcomeSection() {
    AnimatedVisibility(
        visible = true,
        enter = fadeIn() + slideInVertically { it / 4 }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Transform Your Photos",
                style = MaterialTheme.typography.headlineSmall,
                color = TextPrimary,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Select an image and enhance it with AI-powered upscaling",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        color = TextPrimary,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun ImagePreviewCard(
    selectedImageUri: Uri?
) {
    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(320.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (selectedImageUri != null) {
                AsyncImage(
                    model = selectedImageUri,
                    contentDescription = "Selected Image",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(ImagePreviewShape),
                    contentScale = ContentScale.Fit
                )
            } else {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.padding(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Image,
                        contentDescription = null,
                        tint = SoftRoseAccent,
                        modifier = Modifier.size(52.dp)
                    )
                    Text(
                        text = "No Image Selected",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Tap below to choose a photo from your gallery",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
