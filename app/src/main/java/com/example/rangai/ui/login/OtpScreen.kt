package com.example.rangai.ui.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Verified
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.rangai.auth.OtpSmsConsentEffect
import com.example.rangai.ui.components.AuthFormCard
import com.example.rangai.ui.components.AuthHeader
import com.example.rangai.ui.components.GradientButton
import com.example.rangai.ui.components.PremiumBackground
import com.example.rangai.ui.components.PremiumTextField
import com.example.rangai.ui.theme.ErrorRed
import com.example.rangai.ui.theme.TextSecondary

@Composable
fun OtpScreen(
    isLoading: Boolean = false,
    errorMessage: String? = null,
    onVerify: (String) -> Unit
) {
    var otp by remember { mutableStateOf("") }
    var autoSubmitted by remember { mutableStateOf(false) }

    OtpSmsConsentEffect { detectedOtp ->
        if (!isLoading && !autoSubmitted) {
            otp = detectedOtp
            autoSubmitted = true
            onVerify(detectedOtp)
        }
    }

    LaunchedEffect(otp) {
        if (!isLoading && !autoSubmitted && otp.length == 6 && otp.all { it.isDigit() }) {
            autoSubmitted = true
            onVerify(otp)
        }
    }

    PremiumBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
                .padding(top = 72.dp, bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedVisibility(
                visible = true,
                enter = fadeIn() + slideInVertically { it / 4 }
            ) {
                AuthHeader(
                    title = "Verify OTP",
                    subtitle = "Enter the 6-digit code sent to your phone. It will be detected automatically when the SMS arrives."
                )
            }

            Spacer(modifier = Modifier.height(36.dp))

            AuthFormCard {
                PremiumTextField(
                    value = otp,
                    onValueChange = { input ->
                        if (input.length <= 6 && input.all { it.isDigit() }) {
                            autoSubmitted = false
                            otp = input
                        }
                    },
                    label = "One-Time Password",
                    placeholder = "• • • • • •",
                    leadingIcon = Icons.Outlined.Lock,
                    keyboardType = KeyboardType.Number
                )

                if (isLoading) {
                    Spacer(modifier = Modifier.height(20.dp))
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary,
                            strokeWidth = 2.dp,
                            modifier = Modifier.padding(8.dp)
                        )
                        Text(
                            text = "Verifying…",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary
                        )
                    }
                }

                if (errorMessage != null) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = errorMessage,
                        color = ErrorRed,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                GradientButton(
                    text = "Verify & Continue",
                    onClick = { onVerify(otp) },
                    enabled = !isLoading && otp.length == 6,
                    isLoading = isLoading,
                    icon = Icons.Outlined.Verified
                )
            }
        }
    }
}
