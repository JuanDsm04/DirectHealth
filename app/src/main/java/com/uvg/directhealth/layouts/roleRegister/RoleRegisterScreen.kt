package com.uvg.directhealth.layouts.roleRegister

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uvg.directhealth.R
import com.uvg.directhealth.data.model.Role
import com.uvg.directhealth.layouts.common.CustomTopAppBar
import com.uvg.directhealth.layouts.common.CustomButton
import com.uvg.directhealth.ui.theme.DirectHealthTheme

@Composable
fun RoleRegisterRoute(
    onNavigateBack: () -> Unit,
    onRoleRegisterClick: (Role) -> Unit
) {
    RoleRegisterScreen(
        onNavigateBack = onNavigateBack,
        onRoleRegisterClick = onRoleRegisterClick
    )
}

@Composable
private fun RoleRegisterScreen(
    onNavigateBack: () -> Unit,
    onRoleRegisterClick: (Role) -> Unit
){
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
            painter = painterResource(id = R.drawable.register),
            contentDescription = stringResource(id = R.string.register_img),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .size(225.dp)
        )

        Box (
            modifier = Modifier
                .clip(RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp))
                .background(MaterialTheme.colorScheme.surface)
                .fillMaxSize()
                .padding(start = 30.dp, top = 30.dp, end = 30.dp)
        ){
            Column (
                modifier = Modifier,
                Arrangement.Center,
                Alignment.CenterHorizontally,
            ){
                Text(
                    text = stringResource(id = R.string.register_title),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 35.sp,
                        lineHeight = 40.sp,
                        textAlign = TextAlign.Center
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = stringResource(id = R.string.register_rol),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 14.sp,
                        lineHeight = 24.sp,
                        textAlign = TextAlign.Center
                    )
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text =  stringResource(id = R.string.register_option1),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 14.sp,
                        lineHeight = 24.sp,
                        textAlign = TextAlign.Center
                    )
                )
                Spacer(modifier = Modifier.height(5.dp))
                CustomButton(
                    text = stringResource(id = R.string.register_patient_button),
                    onClick = { onRoleRegisterClick(Role.PATIENT) },
                    colorBackground = MaterialTheme.colorScheme.primary,
                    colorText = MaterialTheme.colorScheme.onPrimary,
                    maxWidth = true
                )

                Spacer(modifier = Modifier.height(35.dp))

                Text(
                    text =  stringResource(id = R.string.register_option2),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 14.sp,
                        lineHeight = 24.sp,
                        textAlign = TextAlign.Center
                    )
                )
                Spacer(modifier = Modifier.height(5.dp))
                CustomButton(
                    text = stringResource(id = R.string.register_doctor_button),
                    onClick = { onRoleRegisterClick(Role.DOCTOR) },
                    colorBackground = MaterialTheme.colorScheme.secondaryContainer,
                    colorText = MaterialTheme.colorScheme.onSecondaryContainer,
                    maxWidth = true
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewRoleRegisterScreen() {
    DirectHealthTheme {
        Surface {
            RoleRegisterScreen(
                onNavigateBack = {},
                onRoleRegisterClick = {}
            )
        }
    }
}