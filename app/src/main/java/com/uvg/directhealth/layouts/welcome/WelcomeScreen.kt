package com.uvg.directhealth.layouts.welcome

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uvg.directhealth.R
import com.uvg.directhealth.ui.theme.DirectHealthTheme
import com.uvg.directhealth.layouts.common.CustomButton

@Composable
fun WelcomeRoute (
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit
) {
    WelcomeScreen (
        onLoginClick = onLoginClick,
        onRegisterClick = onRegisterClick
    )
}

@Composable
private fun WelcomeScreen(
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit
){
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
                onClick = { onLoginClick() },
                colorBackground = MaterialTheme.colorScheme.primary,
                colorText = MaterialTheme.colorScheme.onPrimary,
                maxWidth = true
            )

            CustomButton(
                text = stringResource(id = R.string.register_button),
                onClick = { onRegisterClick() },
                colorBackground = MaterialTheme.colorScheme.secondaryContainer,
                colorText = MaterialTheme.colorScheme.onSecondaryContainer,
                maxWidth = true
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewWelcomeScreen() {
    DirectHealthTheme {
        Surface {
            WelcomeScreen(
                onLoginClick = { },
                onRegisterClick = { }
            )
        }
    }
}