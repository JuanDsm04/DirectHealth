package com.uvg.directhealth.layouts.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.uvg.directhealth.R
import com.uvg.directhealth.layouts.common.CustomButton
import com.uvg.directhealth.layouts.common.CustomTopAppBar
import com.uvg.directhealth.ui.theme.DirectHealthTheme

@Composable
fun LoginRoute(
    onLogIn: () -> Unit,
    onRegister: () -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: LoginViewModel = viewModel(factory = LoginViewModel.Factory)
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    
    LaunchedEffect(state.successfulLogin) {
        if (state.successfulLogin) {
            onLogIn()
        }
    }

    LoginScreen(
        state = state,
        onEmailChange = {
            viewModel.onEvent(LoginEvent.EmailChange(it))
        },
        onPasswordChange = {
            viewModel.onEvent(LoginEvent.PasswordChange(it))
        },
        onIsPasswordVisibleChange = {
            viewModel.onEvent(LoginEvent.IsPasswordVisibleChange)
        },
        onLogIn = {
            viewModel.onEvent(LoginEvent.Login)
        },
        onRegister = onRegister,
        onNavigateBack = onNavigateBack
    )
}

@Composable
private fun LoginScreen(
    state: LoginState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onIsPasswordVisibleChange: () -> Unit,
    onLogIn: () -> Unit,
    onRegister: () -> Unit,
    onNavigateBack: () -> Unit
) {
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primaryContainer)
    ){
        CustomTopAppBar(
            onNavigationClick = { onNavigateBack() },
            backgroundColor = MaterialTheme.colorScheme.primaryContainer
        )

        Image(
            painter = painterResource(id = R.drawable.login),
            contentDescription = stringResource(id = R.string.login_img),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .size(225.dp)
        )

        Box (
            modifier = Modifier
                .clip(RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp))
                .background(MaterialTheme.colorScheme.surface)
                .fillMaxSize()
                .padding(30.dp)
        ){
            Column (
                modifier = Modifier,
                Arrangement.Center,
                Alignment.CenterHorizontally,
            ){
                Text(
                    text = stringResource(id = R.string.login_title),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 30.sp,
                        lineHeight = 40.sp,
                        textAlign = TextAlign.Center
                    )
                )

                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextField(
                    value = state.email,
                    onValueChange = onEmailChange,
                    label = {
                        Text(text = stringResource(id = R.string.enter_email))
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true,
                    isError = state.hasError
                )

                OutlinedTextField(
                    value = state.password,
                    onValueChange = onPasswordChange,
                    label = {
                        Text(text = stringResource(id = R.string.enter_password))
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true,
                    visualTransformation = if (state.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val imageResource = if (state.isPasswordVisible) R.drawable.ic_visibility_off else R.drawable.ic_visibility
                        val imageDescription = if (state.isPasswordVisible) stringResource(id = R.string.visibility_off_icon) else stringResource(id = R.string.visibility_icon)

                        IconButton(onClick = onIsPasswordVisibleChange) {
                            Icon(
                                painter = painterResource(id = imageResource),
                                contentDescription = imageDescription,
                                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    isError = state.hasError,
                    supportingText = {
                        if (state.hasError) Text(text = stringResource(id = R.string.login_error))
                    }
                )

                Spacer(modifier = Modifier.weight(1f))

                Row {
                    Text(
                        text = stringResource(id = R.string.not_account),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 14.sp,
                        )
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(text = stringResource(id = R.string.register_option),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        ),
                        modifier = Modifier.clickable { onRegister() }
                    )
                }

                Spacer(modifier = Modifier.height(15.dp))

                CustomButton(
                    text = stringResource(id = R.string.login_button),
                    onClick = { onLogIn() },
                    colorBackground = MaterialTheme.colorScheme.primary,
                    colorText = MaterialTheme.colorScheme.onPrimary,
                    maxWidth = true
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewLoginScreen() {
    DirectHealthTheme {
        Surface {
            LoginScreen(
                state = LoginState(
                    email = "",
                    password = "",
                    isPasswordVisible = false,
                    hasError = false
                ),
                onEmailChange = {},
                onPasswordChange = {},
                onIsPasswordVisibleChange = {},
                onLogIn = {},
                onRegister = {},
                onNavigateBack = {}
            )
        }
    }
}


@Preview(showBackground = true)
@Preview(uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewLoginScreenError() {
    DirectHealthTheme {
        Surface {
            LoginScreen(
                state = LoginState(
                    email = "",
                    password = "",
                    isPasswordVisible = false,
                    hasError = true
                ),
                onEmailChange = {},
                onPasswordChange = {},
                onIsPasswordVisibleChange = {},
                onLogIn = {},
                onRegister = {},
                onNavigateBack = {}
            )
        }
    }
}