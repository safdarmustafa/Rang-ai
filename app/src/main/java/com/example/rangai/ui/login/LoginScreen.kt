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
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.rangai.ui.components.AuthFormCard
import com.example.rangai.ui.components.AuthHeader
import com.example.rangai.ui.components.GradientButton
import com.example.rangai.ui.components.PremiumBackground
import com.example.rangai.ui.components.PremiumTextField

@Composable
fun LoginScreen(
    onSendOtp: (String) -> Unit
) {
    var phoneNumber by remember {
        mutableStateOf("")
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
                    title = "Welcome Back",
                    subtitle = "Sign in with your phone number to continue"
                )
            }

            Spacer(modifier = Modifier.height(36.dp))

            AuthFormCard {
                PremiumTextField(
                    value = phoneNumber,
                    onValueChange = { phoneNumber = it },
                    label = "Phone Number",
                    placeholder = "+1 234 567 8900",
                    leadingIcon = Icons.Outlined.Phone,
                    keyboardType = KeyboardType.Phone
                )

                Spacer(modifier = Modifier.height(24.dp))

                GradientButton(
                    text = "Send OTP",
                    onClick = {
                        onSendOtp(phoneNumber)
                    },
                    icon = Icons.AutoMirrored.Outlined.Send
                )
            }
        }
    }
}
