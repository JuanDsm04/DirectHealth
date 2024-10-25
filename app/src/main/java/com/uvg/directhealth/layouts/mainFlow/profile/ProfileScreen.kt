package com.uvg.directhealth.layouts.mainFlow.profile

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import com.uvg.directhealth.R
import com.uvg.directhealth.data.model.Role
import com.uvg.directhealth.data.model.User
import com.uvg.directhealth.data.source.UserDb
import com.uvg.directhealth.layouts.register.FormComponent
import com.uvg.directhealth.layouts.register.CustomMediumTopAppBar
import com.uvg.directhealth.layouts.register.getSpecialtyItems
import com.uvg.directhealth.layouts.register.specialtyToStringResource
import com.uvg.directhealth.layouts.welcome.CustomButton
import com.uvg.directhealth.ui.theme.DirectHealthTheme
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun ProfileRoute(
    userId: String,
    onLogOut: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val user = UserDb().getUserById(userId)

    var password by remember { mutableStateOf(user.password) }
    var isPasswordError by remember { mutableStateOf(false) }

    var dpi by remember { mutableStateOf(user.dpi) }
    var isDpiError by remember { mutableStateOf(false) }

    var phoneNumber by remember { mutableStateOf(user.phoneNumber) }
    var isPhoneNumberError by remember { mutableStateOf(false) }

    if (user.role == Role.DOCTOR) {
        var membership by remember { mutableStateOf(user.doctorInfo?.number.toString()) }
        var isMembershipError by remember { mutableStateOf(false) }

        ProfileScreen(
            user = user,
            password = password,
            isPasswordError = isPasswordError,
            onPasswordChange = {
                password = it
                isPasswordError = password.length <= 8
            },
            dpi = dpi,
            isDpiError = isDpiError,
            onDpiChange = {
                dpi = it
                isDpiError = !dpi.isDigitsOnly()
            },
            phoneNumber = phoneNumber,
            isPhoneNumberError = isPhoneNumberError,
            onPhoneNumberChange = {
                phoneNumber = it
                isPhoneNumberError = !phoneNumber.isDigitsOnly()
            },
            membership = membership.toString(),
            isMembershipError = isMembershipError,
            onMembershipChange = {
                membership = it
                isMembershipError = !membership.isDigitsOnly()
            },
            onLogOut = onLogOut,
            onNavigateBack = onNavigateBack
        )

    } else {
        ProfileScreen(
            user = user,
            password = password,
            isPasswordError = isPasswordError,
            onPasswordChange = {
                password = it
                isPasswordError = password.length <= 8
            },
            dpi = dpi,
            isDpiError = isDpiError,
            onDpiChange = {
                dpi = it
                isDpiError = !dpi.isDigitsOnly()
            },
            phoneNumber = phoneNumber,
            isPhoneNumberError = isPhoneNumberError,
            onPhoneNumberChange = {
                phoneNumber = it
                isPhoneNumberError = !phoneNumber.isDigitsOnly()
            },
            onLogOut = onLogOut,
            onNavigateBack = onNavigateBack
        )
    }
}

@Composable
private fun ProfileScreen(
    user: User,
    password: String,
    isPasswordError: Boolean,
    onPasswordChange: (String) -> Unit,
    dpi: String,
    isDpiError: Boolean,
    onDpiChange: (String) -> Unit,
    phoneNumber: String,
    isPhoneNumberError: Boolean,
    onPhoneNumberChange: (String) -> Unit,
    membership: String = "",
    isMembershipError: Boolean = false,
    onMembershipChange: ((String) -> Unit) = {},
    onLogOut: () -> Unit,
    onNavigateBack: () -> Unit
){
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    var name by remember { mutableStateOf(user.name) }
    var email by remember { mutableStateOf(user.email) }
    var passwordVisible by remember { mutableStateOf(false) }
    var birthdate by remember { mutableStateOf(dateFormat.format(SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(user.birthDate.toString()) ?: "")) }

    var medicalHistory by remember { mutableStateOf(user.patientInfo?.medicalHistory ?: "") }

    var direction by remember { mutableStateOf(user.doctorInfo?.address ?: "") }
    var experience by remember { mutableStateOf(user.doctorInfo?.summary ?: "") }

    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        LocalContext.current,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            birthdate = "$dayOfMonth/${month + 1}/$year"
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    val specialties = getSpecialtyItems()
    val specialtyStringResource = user.doctorInfo?.specialty?.let { specialtyToStringResource(it) }
    val specialtyText = specialtyStringResource?.let { stringResource(it) } ?: ""
    var selectedSpecialty by remember { mutableStateOf(specialtyText) }
    var expanded by remember { mutableStateOf(false) }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .verticalScroll(rememberScrollState())
    ){
        CustomMediumTopAppBar(
            title = stringResource(R.string.my_profile),
            onNavigationClick = { onNavigateBack() }
        )

        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Column (
                modifier = Modifier,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Image(
                    painter = painterResource(id = R.drawable.user),
                    contentDescription = stringResource(id = R.string.user_img),
                    modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))
                )
                Spacer(modifier = Modifier.height(10.dp))
                CustomButton(
                    text = stringResource(id = R.string.log_out),
                    onClick = { onLogOut() },
                    colorBackground = MaterialTheme.colorScheme.errorContainer,
                    colorText = MaterialTheme.colorScheme.onErrorContainer
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
        }

        Box (
            modifier = Modifier
                .padding(15.dp)
                .clip(RoundedCornerShape(15.dp))
                .background(MaterialTheme.colorScheme.surfaceContainer)
                .fillMaxSize()
                .padding(20.dp)
        ){
            Column(
                verticalArrangement = Arrangement.spacedBy(25.dp)
            ){
                FormComponent(
                    textId = R.string.patient_register_name,
                    textFieldId = R.string.patient_register_text_field_name,
                    keyboardType = KeyboardType.Text,
                    value = name,
                    onValueChange = { name = it },
                    singleLine = true
                )
                FormComponent(
                    textId = R.string.patient_register_email,
                    textFieldId = R.string.patient_register_text_field_email,
                    keyboardType = KeyboardType.Email,
                    value = email,
                    onValueChange = { email = it },
                    singleLine = true
                )
                FormComponent(
                    textId = R.string.patient_register_password,
                    textFieldId = R.string.patient_register_text_field_password,
                    keyboardType = KeyboardType.Password,
                    value = password,
                    onValueChange = onPasswordChange,
                    singleLine = true,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val imageResource = if (passwordVisible) R.drawable.ic_visibility_off else R.drawable.ic_visibility

                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                painter = painterResource(id = imageResource),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                    },
                    isError = isPasswordError,
                    supportingText = {
                        if (isPasswordError) Text(text = stringResource(id = R.string.password_format_error))
                    }
                )
                FormComponent(
                    textId = R.string.patient_register_birthdate,
                    textFieldId = R.string.patient_register_text_field_birthdate,
                    keyboardType = KeyboardType.Text,
                    value = birthdate.toString(),
                    onValueChange = { birthdate = it },
                    singleLine = true,
                    readOnly = true,
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Select Date",
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            modifier = Modifier.clickable {
                                datePickerDialog.show()
                            }
                        )
                    }
                )
                FormComponent(
                    textId = R.string.patient_register_dpi,
                    textFieldId = R.string.patient_register_text_field_dpi,
                    keyboardType = KeyboardType.Phone,
                    value = dpi,
                    onValueChange = onDpiChange,
                    singleLine = true,
                    isError = isDpiError,
                    supportingText = {
                        if (isDpiError) Text(text = stringResource(id = R.string.dpi_format_error))
                    }
                )
                FormComponent(
                    textId = R.string.patient_register_phone_number,
                    textFieldId = R.string.patient_register_text_field_phone_number,
                    keyboardType = KeyboardType.Phone,
                    value = phoneNumber,
                    onValueChange = onPhoneNumberChange,
                    singleLine = true,
                    isError = isPhoneNumberError,
                    supportingText = {
                        if (isPhoneNumberError) Text(text = stringResource(id = R.string.phone_format_error))
                    }
                )

                if (user.role == Role.PATIENT){
                    FormComponent(
                        textId = R.string.patient_register_medical_history,
                        textFieldId = R.string.patient_register_text_field_medical_history,
                        keyboardType = KeyboardType.Text,
                        value = medicalHistory,
                        onValueChange = { medicalHistory = it },
                        singleLine = false
                    )
                }

                if (user.role == Role.DOCTOR){
                    FormComponent(
                        textId = R.string.doctor_register_membership_number,
                        textFieldId = R.string.doctor_register_text_field_membership_number,
                        keyboardType = KeyboardType.Text,
                        value = membership,
                        onValueChange = onMembershipChange,
                        singleLine = true,
                        isError = isMembershipError,
                        supportingText = {
                            if (isMembershipError) Text(text = stringResource(id = R.string.membership_format_error))
                        }
                    )
                    FormComponent(
                        textId = R.string.doctor_register_direction,
                        textFieldId = R.string.doctor_register_text_field_direction,
                        keyboardType = KeyboardType.Text,
                        value = direction,
                        onValueChange = { direction = it },
                        singleLine = true
                    )
                    FormComponent(
                        textId = R.string.doctor_register_professional_experience,
                        textFieldId = R.string.doctor_register_text_field_professional_experience,
                        keyboardType = KeyboardType.Text,
                        value = experience,
                        onValueChange = { experience = it },
                        singleLine = false
                    )
                    FormComponent(
                        textId = R.string.doctor_register_specialty,
                        textFieldId = R.string.doctor_register_text_field_specialty,
                        keyboardType = KeyboardType.Text,
                        value = selectedSpecialty,
                        onValueChange = { },
                        singleLine = true,
                        readOnly = true,
                        expanded = expanded,
                        onExpandedChange = { expanded = it },
                        dropdownItems = specialties.map { it.second },
                        onDropdownSelect = { selectedSpecialty = it }
                    )
                }

                CustomButton(
                    text = stringResource(id = R.string.edit),
                    onClick = { /*TODO*/},
                    colorBackground = MaterialTheme.colorScheme.primary,
                    maxWidth = true,
                    colorText = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewPatientProfileScreen() {
    DirectHealthTheme {
        val userDb = UserDb()

        Surface {
            ProfileScreen(
                user = userDb.getUserById("2"),
                password = "",
                isPasswordError = false,
                onPasswordChange = {},
                dpi = "",
                isDpiError = false,
                onDpiChange = {},
                phoneNumber = "",
                isPhoneNumberError = false,
                onPhoneNumberChange = {},
                onNavigateBack = {},
                onLogOut = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewDoctorProfileScreen() {
    DirectHealthTheme {
        val userDb = UserDb()

        Surface {
            ProfileScreen(
                user = userDb.getUserById("1"),
                password = "",
                isPasswordError = false,
                onPasswordChange = {},
                dpi = "",
                isDpiError = false,
                onDpiChange = {},
                phoneNumber = "",
                isPhoneNumberError = false,
                onPhoneNumberChange = {},
                onLogOut = {},
                onNavigateBack = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewDoctorEditProfileScreenError() {
    DirectHealthTheme {
        val userDb = UserDb()

        Surface {
            ProfileScreen(
                user = userDb.getUserById("1"),
                password = "",
                isPasswordError = true,
                onPasswordChange = {},
                dpi = "",
                isDpiError = true,
                onDpiChange = {},
                phoneNumber = "",
                isPhoneNumberError = true,
                onPhoneNumberChange = {},
                onLogOut = {},
                onNavigateBack = {}
            )
        }
    }
}