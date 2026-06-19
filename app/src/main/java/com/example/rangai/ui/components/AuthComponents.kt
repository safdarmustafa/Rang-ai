package com.example.rangai.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.rangai.ui.theme.BurgundySecondary
import com.example.rangai.ui.theme.ChipShape
import com.example.rangai.ui.theme.GlassBorder
import com.example.rangai.ui.theme.MaroonPrimary
import com.example.rangai.ui.theme.TextPrimary
import com.example.rangai.ui.theme.TextSecondary
import com.example.rangai.ui.theme.TextTertiary
import com.example.rangai.ui.theme.WarmRedAccent

@Composable
fun PremiumTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    leadingIcon: ImageVector? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    singleLine: Boolean = true
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        label = {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        placeholder = {
            if (placeholder.isNotEmpty()) {
                Text(
                    text = placeholder,
                    color = TextTertiary,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        },
        leadingIcon = leadingIcon?.let { icon ->
            {
                androidx.compose.material3.Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = WarmRedAccent.copy(alpha = 0.85f)
                )
            }
        },
        singleLine = singleLine,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        visualTransformation = visualTransformation,
        shape = ChipShape,
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = TextPrimary,
            unfocusedTextColor = TextPrimary,
            disabledTextColor = TextTertiary,
            cursorColor = WarmRedAccent,
            focusedBorderColor = WarmRedAccent,
            unfocusedBorderColor = GlassBorder,
            focusedLabelColor = WarmRedAccent,
            unfocusedLabelColor = TextSecondary,
            focusedContainerColor = BurgundySecondary.copy(alpha = 0.15f),
            unfocusedContainerColor = MaroonPrimary.copy(alpha = 0.08f)
        )
    )
}

@Composable
fun AuthHeader(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineLarge,
            color = TextPrimary
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary
        )
    }
}

@Composable
fun AuthFormCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    GlassCard(
        modifier = modifier,
        animate = true
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            content()
        }
    }
}
