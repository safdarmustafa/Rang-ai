package com.example.rangai.ui.profile

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Cake
import androidx.compose.runtime.*
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
fun ProfileSetupScreen(
    onContinue: (String, Int) -> Unit
) {

    var name by remember {
        mutableStateOf("")
    }

    var age by remember {
        mutableStateOf("")
    }

    PremiumBackground {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
                .verticalScroll(
                    rememberScrollState()
                )
                .padding(horizontal = 24.dp)
                .padding(
                    top = 72.dp,
                    bottom = 32.dp
                ),
            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {

            AuthHeader(
                title = "Complete Profile",
                subtitle =
                    "Tell us a little about yourself"
            )

            Spacer(
                modifier = Modifier.height(36.dp)
            )

            AuthFormCard {

                PremiumTextField(
                    value = name,
                    onValueChange = {
                        name = it
                    },
                    label = "Full Name",
                    placeholder = "John Doe",
                    leadingIcon =
                        Icons.Outlined.Person
                )

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                PremiumTextField(
                    value = age,
                    onValueChange = {
                        age = it
                    },
                    label = "Age",
                    placeholder = "21",
                    leadingIcon =
                        Icons.Outlined.Cake,
                    keyboardType =
                        KeyboardType.Number
                )

                Spacer(
                    modifier = Modifier.height(28.dp)
                )

                GradientButton(
                    text = "Continue",
                    onClick = {
                        if (
                            name.isNotBlank() &&
                            age.isNotBlank()
                        ) {
                            onContinue(name, age.toInt())
                        }
                    },
                    icon = Icons.AutoMirrored.Outlined.ArrowForward
                )
            }
        }
    }
}