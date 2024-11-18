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
import androidx.compose.runtime.LaunchedEffect
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.uvg.directhealth.R
import com.uvg.directhealth.domain.model.DoctorInfo
import com.uvg.directhealth.domain.model.Role
import com.uvg.directhealth.domain.model.Specialty
import com.uvg.directhealth.domain.model.User
import com.uvg.directhealth.layouts.common.FormComponent
import com.uvg.directhealth.layouts.common.CustomMediumTopAppBar
import com.uvg.directhealth.layouts.register.getSpecialtyItems
import com.uvg.directhealth.layouts.common.CustomButton
import com.uvg.directhealth.layouts.common.HasError
import com.uvg.directhealth.layouts.common.IsLoading
import com.uvg.directhealth.ui.theme.DirectHealthTheme
import java.time.LocalDate
import java.util.Calendar

@Composable
    fun ProfileRoute(
        userId: String,
        viewModel: ProfileViewModel = viewModel(factory = ProfileViewModel.Factory),
        onLogOut: () -> Unit,
        onNavigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val role = state.loggedUser?.role
    val isFormValid = role?.let { viewModel.isFormValid(it) }

    LaunchedEffect(userId) {
        viewModel.onEvent(ProfileEvent.PopulateData(userId))
    }

    if (isFormValid != null) {
        ProfileScreen(
            state = state,
            isFormValid = isFormValid,
            onNameChange = {
                viewModel.onEvent(ProfileEvent.NameChange(it))
            },
            onEmailChange = {
                viewModel.onEvent(ProfileEvent.EmailChange(it))
            },
            onPasswordChange = {
                viewModel.onEvent(ProfileEvent.PasswordChange(it))
            },
            onPasswordVisibleChange = {
                viewModel.onEvent(ProfileEvent.PasswordVisibleChange)
            },
            onBirthDateChange = {
                viewModel.onEvent(ProfileEvent.BirthDateChange(it))
            },
            onDpiChange = {
                viewModel.onEvent(ProfileEvent.DpiChange(it))
            },
            onPhoneNumberChange = {
                viewModel.onEvent(ProfileEvent.PhoneNumberChange(it))
            },
            onMedicalHistoryChange = {
                viewModel.onEvent(ProfileEvent.MedicalHistoryChange(it))
            },
            onMembershipChange = {
                viewModel.onEvent(ProfileEvent.MembershipChange(it))
            },
            onAddressChange = {
                viewModel.onEvent(ProfileEvent.AddressChange(it))
            },
            onExperienceChange = {
                viewModel.onEvent(ProfileEvent.ExperienceChange(it))
            },
            onLogOut = onLogOut,
            onBackNavigation = onNavigateBack,
            onSaveProfile = {
                viewModel.onEvent(ProfileEvent.SaveProfile)
            }
        )
    }
}

@Composable
private fun ProfileScreen(
    state: ProfileState,
    isFormValid: Boolean,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onBirthDateChange: (String) -> Unit,
    onDpiChange: (String) -> Unit,
    onPhoneNumberChange: (String) -> Unit,
    onMedicalHistoryChange: (String) -> Unit,
    onMembershipChange: (String) -> Unit,
    onAddressChange: (String) -> Unit,
    onExperienceChange: (String) -> Unit,
    onPasswordVisibleChange: () -> Unit,
    onLogOut: () -> Unit,
    onBackNavigation: () -> Unit,
    onSaveProfile: () -> Unit
){
    val user = state.loggedUser

    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .verticalScroll(rememberScrollState())
    ){
        CustomMediumTopAppBar(
            title = stringResource(R.string.my_profile),
            onNavigationClick = { onBackNavigation() }
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

        when {
            state.isLoading -> IsLoading()
            state.hasError -> HasError()
            else ->
            Form(
                user = user,
                state = state,
                isFormValid = isFormValid,
                onNameChange = onNameChange,
                onEmailChange = onEmailChange,
                onPasswordChange = onPasswordChange,
                onBirthDateChange = onBirthDateChange,
                onDpiChange = onDpiChange,
                onPhoneNumberChange = onPhoneNumberChange,
                onMedicalHistoryChange = onMedicalHistoryChange,
                onMembershipChange = onMembershipChange,
                onAddressChange = onAddressChange,
                onExperienceChange = onExperienceChange,
                onPasswordVisibleChange = onPasswordVisibleChange,
                onSaveProfile = onSaveProfile
            )
        }
    }
}

@Composable
private fun Form(
    user: User?,
    state: ProfileState,
    isFormValid: Boolean,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onBirthDateChange: (String) -> Unit,
    onDpiChange: (String) -> Unit,
    onPhoneNumberChange: (String) -> Unit,
    onMedicalHistoryChange: (String) -> Unit,
    onMembershipChange: (String) -> Unit,
    onAddressChange: (String) -> Unit,
    onExperienceChange: (String) -> Unit,
    onPasswordVisibleChange: () -> Unit,
    onSaveProfile: () -> Unit
) {
    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        LocalContext.current,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            onBirthDateChange("$dayOfMonth/${month + 1}/$year")
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

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
            if (user != null) {
                FormComponent(
                    textId = R.string.patient_register_name,
                    textFieldId = R.string.patient_register_text_field_name,
                    keyboardType = KeyboardType.Text,
                    value = state.name,
                    onValueChange = onNameChange,
                    singleLine = true
                )

                FormComponent(
                    textId = R.string.patient_register_email,
                    textFieldId = R.string.patient_register_text_field_email,
                    keyboardType = KeyboardType.Email,
                    value = state.email,
                    onValueChange = onEmailChange,
                    singleLine = true,
                    isError = state.hasEmailError,
                    supportingText = {
                        if (state.hasEmailError) Text(text = stringResource(id = R.string.email_error))
                    }
                )

                FormComponent(
                    textId = R.string.patient_register_password,
                    textFieldId = R.string.patient_register_text_field_password,
                    keyboardType = KeyboardType.Password,
                    value = state.password,
                    onValueChange = onPasswordChange,
                    singleLine = true,
                    visualTransformation = if (state.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val imageResource = if (state.isPasswordVisible) R.drawable.ic_visibility_off else R.drawable.ic_visibility

                        IconButton(onClick = onPasswordVisibleChange) {
                            Icon(
                                painter = painterResource(id = imageResource),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                    },
                    isError = state.hasPasswordError,
                    supportingText = {
                        if (state.hasPasswordError) Text(text = stringResource(id = R.string.password_format_error))
                    }
                )

                FormComponent(
                    textId = R.string.patient_register_birthdate,
                    textFieldId = R.string.patient_register_text_field_birthdate,
                    keyboardType = KeyboardType.Text,
                    value = state.birthDate,
                    onValueChange = onBirthDateChange,
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
                    },
                    isError = state.hasBirthDateError,
                    supportingText = {
                        if (state.hasBirthDateError) Text(stringResource(id = R.string.birthdate_error))
                    }
                )

                FormComponent(
                    textId = R.string.patient_register_dpi,
                    textFieldId = R.string.patient_register_text_field_dpi,
                    keyboardType = KeyboardType.Phone,
                    value = state.dpi,
                    onValueChange = onDpiChange,
                    singleLine = true,
                    isError = state.hasDpiError,
                    supportingText = {
                        if (state.hasDpiError) Text(text = stringResource(id = R.string.dpi_format_error))
                    }
                )

                FormComponent(
                    textId = R.string.patient_register_phone_number,
                    textFieldId = R.string.patient_register_text_field_phone_number,
                    keyboardType = KeyboardType.Phone,
                    value = state.phoneNumber,
                    onValueChange = onPhoneNumberChange,
                    singleLine = true,
                    isError = state.hasDpiError,
                    supportingText = {
                        if (state.hasDpiError) Text(text = stringResource(id = R.string.phone_format_error))
                    }
                )
            }

            if (user != null) {
                if (user.role == Role.PATIENT){
                    user.medicalHistory?.let {
                        FormComponent(
                            textId = R.string.patient_register_medical_history,
                            textFieldId = R.string.patient_register_text_field_medical_history,
                            keyboardType = KeyboardType.Text,
                            value = state.medicalHistory,
                            onValueChange = onMedicalHistoryChange,
                            singleLine = false
                        )
                    }
                }
            }

            if (user != null) {
                if (user.role == Role.DOCTOR){
                    FormComponent(
                        textId = R.string.doctor_register_membership_number,
                        textFieldId = R.string.doctor_register_text_field_membership_number,
                        keyboardType = KeyboardType.Text,
                        value = state.membership,
                        onValueChange = onMembershipChange,
                        singleLine = true,
                        isError = state.hasMembershipError,
                        supportingText = {
                            if (state.hasMembershipError) Text(text = stringResource(id = R.string.membership_format_error))
                        }
                    )

                    user.doctorInfo?.let {
                        FormComponent(
                            textId = R.string.doctor_register_direction,
                            textFieldId = R.string.doctor_register_text_field_direction,
                            keyboardType = KeyboardType.Text,
                            value = state.address,
                            onValueChange = onAddressChange,
                            singleLine = true
                        )
                    }

                    user.doctorInfo?.let {
                        FormComponent(
                            textId = R.string.doctor_register_professional_experience,
                            textFieldId = R.string.doctor_register_text_field_professional_experience,
                            keyboardType = KeyboardType.Text,
                            value = state.experience,
                            onValueChange = onExperienceChange,
                            singleLine = false
                        )
                    }
                }
            }

            CustomButton(
                text = stringResource(id = R.string.edit),
                onClick = { onSaveProfile() },
                colorBackground = MaterialTheme.colorScheme.primary,
                maxWidth = true,
                colorText = MaterialTheme.colorScheme.onPrimary,
                enable = isFormValid
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewPatientProfileScreen() {
    DirectHealthTheme {
        Surface {
            ProfileScreen(
                state = ProfileState(
                    loggedUser = User(
                        id = "2",
                        role = Role.PATIENT,
                        name = "Ana Martínez",
                        email = "ana.martinez@gmail.com",
                        password = "password123",
                        birthDate = LocalDate.of(1990, 2, 20),
                        dpi = "9876543210123",
                        phoneNumber = "87654321",
                        medicalHistory = "Sin alergias conocidas. Cirugías previas: apendicectomía en 2010.",
                        doctorInfo = null
                    ),
                    isLoading = false
                ),
                onAddressChange = {},
                onMedicalHistoryChange = {},
                onEmailChange = {},
                onNameChange = {},
                onBirthDateChange = {},
                onDpiChange = {},
                onPasswordChange = {},
                onMembershipChange = {},
                onPhoneNumberChange = {},
                onPasswordVisibleChange = {},
                onBackNavigation = {},
                onExperienceChange = {},
                onLogOut = {},
                onSaveProfile = {},
                isFormValid = true
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewDoctorProfileScreen() {
    DirectHealthTheme {
        Surface {
            ProfileScreen(
                state = ProfileState(
                    loggedUser = User(
                        id = "1",
                        role = Role.DOCTOR,
                        name = "Dr. Juan Pérez",
                        email = "juan.perez@directhealth.com",
                        password = "password123",
                        birthDate = LocalDate.of(1975, 5, 12),
                        dpi = "1234567890123",
                        phoneNumber = "12345678",
                        medicalHistory = null,
                        doctorInfo = DoctorInfo(
                            number = 1122,
                            address = "Calle Salud 123",
                            summary = "Cardiólogo experimentado con más de 20 años en el campo.",
                            specialty = Specialty.CARDIOLOGY
                        )
                    ),
                    isLoading = false
                ),
                onAddressChange = {},
                onMedicalHistoryChange = {},
                onEmailChange = {},
                onNameChange = {},
                onBirthDateChange = {},
                onDpiChange = {},
                onPasswordChange = {},
                onMembershipChange = {},
                onPhoneNumberChange = {},
                onPasswordVisibleChange = {},
                onBackNavigation = {},
                onExperienceChange = {},
                onLogOut = {},
                onSaveProfile = {},
                isFormValid = true
            )
        }
    }
}