package com.uvg.directhealth.login

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.uvg.directhealth.R
import com.uvg.directhealth.ui.theme.DirectHealthTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(){
    var text1 by remember { mutableStateOf("") }
    var text2 by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primaryContainer)
    ){
        TopAppBar(
            title = {},
            navigationIcon = {
                IconButton({}) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            )
        )

        Image(
            painter = painterResource(id = R.drawable.login),
            contentDescription = "Welcome Image",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .size(225.dp)
        )

        Box (
            modifier = Modifier
                .clip(RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp))
                .background(MaterialTheme.colorScheme.onPrimary)
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
                    value = text1,
                    onValueChange = { text1 = it },
                    label = {
                        Text(text = stringResource(id = R.string.login_text_field1))
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true
                )

                OutlinedTextField(
                    value = text2,
                    onValueChange = { text2 = it },
                    label = {
                        Text(text = stringResource(id = R.string.login_text_field2))
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val imageResource = if (passwordVisible) R.drawable.visibility_off else R.drawable.visibility

                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(painter = painterResource(id = imageResource), contentDescription = null, tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )

                Spacer(modifier = Modifier.weight(1f))

                Row {
                    Text(
                        text = stringResource(id = R.string.login_text1),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 14.sp,
                        )
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(text = stringResource(id = R.string.login_text2),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        ),
                        modifier = Modifier.clickable { }
                    )
                }

                Spacer(modifier = Modifier.height(15.dp))

                TextButton(
                    onClick = {  },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(15.dp))
                        .background(MaterialTheme.colorScheme.primary)
                        .height(50.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.login_button),
                        style = MaterialTheme.typography.labelLarge.copy(
                            color = MaterialTheme.colorScheme.onPrimary,
                        )
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewLoginScreen() {
    DirectHealthTheme {
        Surface {
            LoginScreen()
        }
    }
}