package com.uvg.directhealth.layouts.common

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun CustomButton(
    text: String,
    onClick: () -> Unit,
    colorBackground: Color,
    colorText: Color,
    icon: ImageVector? = null,
    contentDescriptionIcon: String? = null,
    maxWidth: Boolean = false,
    enable: Boolean? = true
) {
    if (enable != null) {
        TextButton(
            onClick = onClick,
            modifier = Modifier
                .then(if (maxWidth) Modifier.fillMaxWidth() else Modifier)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorBackground
            ),
            enabled = enable
        ) {
            if (icon != null) {
                Icon(
                    icon,
                    contentDescription = contentDescriptionIcon,
                    tint = colorText
                )
                Spacer(modifier = Modifier.width(10.dp))
            }
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge.copy(
                    color = colorText
                )
            )
        }
    }
}