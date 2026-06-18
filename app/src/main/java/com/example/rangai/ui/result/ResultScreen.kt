package com.example.rangai.ui.result

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding

import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

import androidx.core.content.FileProvider

import coil.compose.SubcomposeAsyncImage

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

import java.io.File
import java.io.FileOutputStream
import java.net.URL
@Composable
fun ResultScreen(
    imageUrl: String
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        Text(
            text = "Enhanced Result",
            style = MaterialTheme.typography.headlineMedium
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(450.dp)
                .padding(top = 24.dp)
        ) {

            SubcomposeAsyncImage(
                model = imageUrl,
                contentDescription = "Enhanced Image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit,
                loading = {

                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {

                        CircularProgressIndicator()
                    }
                }
            )
        }

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Button(
            onClick = {

                downloadImage(
                    context = context,
                    imageUrl = imageUrl
                )

            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Download Image")
        }

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Button(
            onClick = {

                shareImage(
                    context = context,
                    imageUrl = imageUrl
                )

            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Share Image")
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

            val inputStream =
                url.openStream()

            val fileName =
                "RangAI_${System.currentTimeMillis()}.png"

            val values = ContentValues().apply {

                put(
                    MediaStore.Images.Media.DISPLAY_NAME,
                    fileName
                )

                put(
                    MediaStore.Images.Media.MIME_TYPE,
                    "image/png"
                )

                put(
                    MediaStore.Images.Media.RELATIVE_PATH,
                    Environment.DIRECTORY_PICTURES +
                            "/RangAI"
                )
            }

            val uri =
                context.contentResolver.insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    values
                )

            uri?.let {

                val outputStream =
                    context.contentResolver.openOutputStream(it)

                inputStream.copyTo(outputStream!!)

                outputStream.close()
                inputStream.close()

                android.os.Handler(
                    android.os.Looper.getMainLooper()
                ).post {

                    Toast.makeText(
                        context,
                        "Image Saved Successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        } catch (e: Exception) {

            android.os.Handler(
                android.os.Looper.getMainLooper()
            ).post {

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

            val inputStream =
                url.openStream()

            val file =
                File(
                    context.cacheDir,
                    "shared_image.png"
                )

            val outputStream =
                FileOutputStream(file)

            inputStream.copyTo(outputStream)

            outputStream.close()
            inputStream.close()

            val uri =
                FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.provider",
                    file
                )

            val shareIntent =
                Intent(Intent.ACTION_SEND).apply {

                    type = "image/*"

                    putExtra(
                        Intent.EXTRA_STREAM,
                        uri
                    )

                    addFlags(
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                }

            context.startActivity(
                Intent.createChooser(
                    shareIntent,
                    "Share Image"
                )
            )

        } catch (e: Exception) {

            e.printStackTrace()
        }
    }
}
