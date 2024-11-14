package com.uvg.directhealth.layouts.register

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.uvg.directhealth.R
import com.uvg.directhealth.domain.model.Role
import com.uvg.directhealth.domain.model.Specialty
import com.uvg.directhealth.layouts.common.CustomButton
import com.uvg.directhealth.ui.theme.DirectHealthTheme
import com.uvg.directhealth.layouts.common.FormComponent
import com.uvg.directhealth.layouts.common.CustomMediumTopAppBar
import com.uvg.directhealth.data.source.specialtyToStringResource
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun RegisterRoute(
    role: Role,
    viewModel: RegisterViewModel = viewModel(factory = RegisterViewModel.Factory),
    onBackNavigation: () -> Unit,
    onConfirmRegistration: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.successfulRegistration) {
        if (state.successfulRegistration) onConfirmRegistration()
    }

    RegisterScreen(
        state = state,
        role = role,
        onBackNavigation = onBackNavigation,
        onConfirmRegistration = {
            viewModel.onEvent(RegisterEvent.Register)
        },
        onNameChange = {
            viewModel.onEvent(RegisterEvent.NameChange(it))
        },
        onEmailChange = {
            viewModel.onEvent(RegisterEvent.EmailChange(it))
        },
        onPasswordChange = {
            viewModel.onEvent(RegisterEvent.PasswordChange(it))
        },
        onPasswordVisibleChange = {
            viewModel.onEvent(RegisterEvent.PasswordVisibleChange)
        },
        onBirthDateChange = {
            viewModel.onEvent(RegisterEvent.BirthDateChange(it))
        },
        onDpiChange = {
            viewModel.onEvent(RegisterEvent.DpiChange(it))
        },
        onPhoneNumberChange = {
            viewModel.onEvent(RegisterEvent.PhoneNumberChange(it))
        },
        onMedicalHistoryChange = {
            viewModel.onEvent(RegisterEvent.MedicalHistoryChange(it))
        },
        onMembershipChange = {
            viewModel.onEvent(RegisterEvent.MembershipChange(it))
        },
        onAddressChange = {
            viewModel.onEvent(RegisterEvent.AddressChange(it))
        },
        onExperienceChange = {
            viewModel.onEvent(RegisterEvent.ExperienceChange(it))
        },
        onSpecialtyChange = {
            println(Specialty.valueOf(it))
            viewModel.onEvent(RegisterEvent.SpecialtyChange(Specialty.valueOf(it)))
        }
    )
}

@Composable
private fun RegisterScreen(
    state: RegisterState,
    role: Role,
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
    onSpecialtyChange: (String) -> Unit,
    onPasswordVisibleChange: () -> Unit,
    onBackNavigation: () -> Unit,
    onConfirmRegistration: () -> Unit
) {
    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        LocalContext.current,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            val formattedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)
            onBirthDateChange(formattedDate)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    val specialties = getSpecialtyItems()
    var selectedSpecialty by remember { mutableStateOf<Pair<Specialty, String>?>(null) }
    var expanded by remember { mutableStateOf(false) }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .verticalScroll(rememberScrollState())
    ){
        CustomMediumTopAppBar(
            title = stringResource(
                id = if (role == Role.PATIENT) R.string.patient_register_title
                else R.string.doctor_register_title
            ),
            onNavigationClick = { onBackNavigation() }
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
                FormComponent(
                    textId = R.string.patient_register_name,
                    textFieldId = R.string.patient_register_text_field_name,
                    keyboardType = KeyboardType.Text,
                    value = state.name,
                    onValueChange = onNameChange,
                    singleLine = true,
                    isError = false
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
                        if (state.hasDpiError) Text(stringResource(id = R.string.dpi_format_error))
                    }
                )

                FormComponent(
                    textId = R.string.patient_register_phone_number,
                    textFieldId = R.string.patient_register_text_field_phone_number,
                    keyboardType = KeyboardType.Phone,
                    value = state.phoneNumber,
                    onValueChange = onPhoneNumberChange,
                    singleLine = true,
                    isError = state.hasPhoneNumberError,
                    supportingText = {
                        if (state.hasPhoneNumberError) Text(stringResource(id = R.string.phone_format_error))
                    }
                )

                if (role == Role.PATIENT){
                    FormComponent(
                        textId = R.string.patient_register_medical_history,
                        textFieldId = R.string.patient_register_text_field_medical_history,
                        keyboardType = KeyboardType.Text,
                        value = state.medicalHistory,
                        onValueChange = onMedicalHistoryChange,
                        singleLine = false,
                        isError = false
                    )
                }

                if (role == Role.DOCTOR){
                    FormComponent(
                        textId = R.string.doctor_register_membership_number,
                        textFieldId = R.string.doctor_register_text_field_membership_number,
                        keyboardType = KeyboardType.Text,
                        value = state.membership,
                        onValueChange = onMembershipChange,
                        singleLine = true,
                        isError = state.hasMembershipError,
                        supportingText = {
                            if (state.hasMembershipError) Text(stringResource(id = R.string.membership_format_error))
                        }
                    )

                    FormComponent(
                        textId = R.string.doctor_register_direction,
                        textFieldId = R.string.doctor_register_text_field_direction,
                        keyboardType = KeyboardType.Text,
                        value = state.address,
                        onValueChange = onAddressChange,
                        singleLine = true
                    )

                    FormComponent(
                        textId = R.string.doctor_register_professional_experience,
                        textFieldId = R.string.doctor_register_text_field_professional_experience,
                        keyboardType = KeyboardType.Text,
                        value = state.experience,
                        onValueChange = onExperienceChange,
                        singleLine = false
                    )

                    FormComponent(
                        textId = R.string.doctor_register_specialty,
                        textFieldId = R.string.doctor_register_text_field_specialty,
                        keyboardType = KeyboardType.Text,
                        value = selectedSpecialty?.second ?: "",
                        onValueChange = { },
                        singleLine = true,
                        readOnly = true,
                        expanded = expanded,
                        onExpandedChange = { expanded = it },
                        dropdownItems = specialties.map { it.second },
                        onDropdownSelect = { selectedName ->
                            selectedSpecialty = specialties.first { it.second == selectedName }
                            onSpecialtyChange(selectedSpecialty?.first.toString())
                        }
                    )
                }

                CustomButton(
                    text = stringResource(id = R.string.register_button),
                    onClick = { onConfirmRegistration() },
                    colorBackground = MaterialTheme.colorScheme.primary,
                    maxWidth = true,
                    colorText = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

@Composable
fun getSpecialtyItems(): List<Pair<Specialty, String>> {
    return Specialty.entries.map { specialty ->
        val specialtyName = stringResource(id = specialtyToStringResource(specialty))
        specialty to specialtyName
    }
}

@Preview(showBackground = true)
@Preview(uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewPatientRegisterScreen() {
    DirectHealthTheme {
        Surface {
            RegisterScreen(
                role = Role.PATIENT,
                state = RegisterState(role = Role.PATIENT),
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
                onConfirmRegistration = {},
                onBackNavigation = {},
                onExperienceChange = {},
                onSpecialtyChange = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewPatientRegisterScreenWithErrors() {
    DirectHealthTheme {
        Surface {
            RegisterScreen(
                role = Role.PATIENT,
                state = RegisterState(
                    hasPasswordError = true,
                    hasMembershipError = true,
                    hasEmailError = true,
                    hasPhoneNumberError = true,
                    hasDpiError = true,
                    hasBirthDateError = true,
                    role = Role.PATIENT
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
                onConfirmRegistration = {},
                onBackNavigation = {},
                onExperienceChange = {},
                onSpecialtyChange = {}
            )
        }
    }
}