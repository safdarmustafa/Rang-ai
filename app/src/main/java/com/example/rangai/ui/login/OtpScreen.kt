package com.example.rangai.ui.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun OtpScreen(
    onVerify: () -> Unit
) {

    var otp by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Verify OTP"
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = otp,
            onValueChange = {
                otp = it
            },
            label = {
                Text("Enter OTP")
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onVerify
        ) {
            Text("Verify")
        }
    }
}