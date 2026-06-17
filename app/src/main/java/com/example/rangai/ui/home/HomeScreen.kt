package com.example.rangai.ui.home
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import coil.compose.AsyncImage
import androidx.compose.runtime.*
import androidx.activity.result.PickVisualMediaRequest
import androidx.compose.ui.layout.ContentScale
import com.example.rangai.viewmodel.HomeViewModel
import com.example.rangai.data.EnhancementType
import android.util.Log
import android.net.Uri
import com.example.rangai.data.repository.StorageRepository
import androidx.compose.ui.platform.LocalContext
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream


@Composable
fun HomeScreen() {

    val homeViewModel: HomeViewModel = viewModel()
    val context = LocalContext.current
    val isLoading by homeViewModel.isLoading.collectAsState()

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
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "Rang AI",
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "AI Powered Photo Enhancement"
        )

        Spacer(modifier = Modifier.height(40.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp)
        ) {

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {

                if (selectedImageUri != null) {

                    AsyncImage(
                        model = selectedImageUri,
                        contentDescription = "Selected Image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )

                } else {

                    Text("No Image Selected")
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            FilterChip(
                selected = selectedEnhancement == EnhancementType.HD,
                onClick = {
                    selectedEnhancement = EnhancementType.HD
                },
                label = {
                    Text("HD")
                }
            )

            FilterChip(
                selected = selectedEnhancement == EnhancementType.ULTRA_HD,
                onClick = {
                    selectedEnhancement = EnhancementType.ULTRA_HD
                },
                label = {
                    Text("Ultra HD")
                }
            )
        }

        Button(
            onClick = {
                launcher.launch(
                    PickVisualMediaRequest(
                        ActivityResultContracts.PickVisualMedia.ImageOnly
                    )
                )
            }
        )
        {
            Text("Select Image")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {

                android.util.Log.d(
                    "RANG_AI",
                    "Enhance button clicked"
                )

                selectedImageUri?.let { uri ->

                    android.util.Log.d(
                        "RANG_AI",
                        "Uri found: $uri"
                    )

                    val originalBytes = context.contentResolver
                        .openInputStream(uri)
                        ?.readBytes()

                    if (originalBytes != null) {

                        val bitmap = BitmapFactory.decodeByteArray(
                            originalBytes,
                            0,
                            originalBytes.size
                        )

                        val resizedBitmap = Bitmap.createScaledBitmap(
                            bitmap,
                            1024,
                            1024,
                            true
                        )

                        val outputStream = ByteArrayOutputStream()

                        resizedBitmap.compress(
                            Bitmap.CompressFormat.JPEG,
                            90,
                            outputStream
                        )

                        val resizedBytes = outputStream.toByteArray()

                        android.util.Log.d(
                            "RANG_AI",
                            "Original Bytes = ${originalBytes.size}"
                        )

                        android.util.Log.d(
                            "RANG_AI",
                            "Resized Bytes = ${resizedBytes.size}"
                        )

                        homeViewModel.startEnhancing(
                            imageBytes = resizedBytes
                        )
                    }
                }
            },
            enabled = selectedImageUri != null && !isLoading
        ) {
            Text(
                if (isLoading)
                    "Enhancing..."
                else
                    "Enhance Image"
            )
        }
    }
}