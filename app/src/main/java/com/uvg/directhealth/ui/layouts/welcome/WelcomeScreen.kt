package com.uvg.directhealth.ui.layouts.welcome

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uvg.directhealth.R
import com.uvg.directhealth.ui.theme.DirectHealthTheme

@Composable
fun WelcomeScreen(){
    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp),
        Arrangement.SpaceBetween,
        Alignment.CenterHorizontally
    ){
        Spacer(modifier = Modifier.weight(0.8f))

        Image(
            painter = painterResource(id = R.drawable.welcome),
            contentDescription = stringResource(id = R.string.welcome_img),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .size(250.dp)
        )

        Spacer(modifier = Modifier.height(30.dp))

        Column (
            modifier = Modifier
                .padding(15.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ){
            Text(
                text = stringResource(id = R.string.welcome_title),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 35.sp,
                    lineHeight = 40.sp,
                    textAlign = TextAlign.Center
                )
            )
            Text(
                text = stringResource(id = R.string.welcome_text),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 14.sp,
                    lineHeight = 24.sp,
                    textAlign = TextAlign.Center
                )
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Column (
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            CustomButton(
                text = stringResource(id = R.string.login_button),
                onClick = {/**/},
                colorBackground = MaterialTheme.colorScheme.primary,
                colorText = MaterialTheme.colorScheme.onPrimary,
                maxWidth = true
            )

            CustomButton(
                text = stringResource(id = R.string.register_button),
                onClick = {/**/},
                colorBackground = MaterialTheme.colorScheme.secondaryContainer,
                colorText = MaterialTheme.colorScheme.onSecondaryContainer,
                maxWidth = true
            )
        }
    }
}

@Composable
fun CustomButton(
    text: String,
    onClick: () -> Unit,
    colorBackground: Color,
    colorText: Color,
    icon: ImageVector? = null,
    contentDescriptionIcon: String? = null,
    maxWidth: Boolean = false
) {
    TextButton(
        onClick = onClick,
        modifier = Modifier
            .then(if (maxWidth) Modifier.fillMaxWidth() else Modifier)
            .height(50.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = colorBackground
        )
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

@Preview(showBackground = true)
@Composable
private fun PreviewWelcomeScreen() {
    DirectHealthTheme {
        Surface {
            WelcomeScreen()
        }
    }
}

@Preview(
    showBackground = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun PreviewWelcomeScreenDark() {
    DirectHealthTheme {
        Surface {
            WelcomeScreen()
        }
    }
}