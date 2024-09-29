package com.uvg.directhealth.ui.layouts.register

import android.app.DatePickerDialog
import android.widget.DatePicker
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import com.uvg.directhealth.R
import com.uvg.directhealth.db.Role
import com.uvg.directhealth.db.Specialty
import com.uvg.directhealth.ui.layouts.welcome.CustomButton
import com.uvg.directhealth.ui.theme.DirectHealthTheme
import java.util.Calendar

@Composable
fun RegisterRoute(role: Role) {
    var isPasswordError by remember { mutableStateOf(false) }
    var password by remember { mutableStateOf("") }

    var dpi by remember { mutableStateOf("") }
    var isDpiError by remember { mutableStateOf(false) }

    var phoneNumber by remember { mutableStateOf("") }
    var isPhoneNumberError by remember { mutableStateOf(false) }

    var membership by remember { mutableStateOf("") }
    var isMembershipError by remember { mutableStateOf(false) }

    RegisterScreen(
        role = role,
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
        membership = membership,
        isMembershipError = isMembershipError,
        onMembershipChange = {
            membership = it
            isMembershipError = !membership.isDigitsOnly()
        }
    )
}

@Composable
private fun RegisterScreen(
    role: Role,
    password: String,
    isPasswordError: Boolean,
    onPasswordChange: (String) -> Unit,
    dpi: String,
    isDpiError: Boolean,
    onDpiChange: (String) -> Unit,
    phoneNumber: String,
    isPhoneNumberError: Boolean,
    onPhoneNumberChange: (String) -> Unit,
    membership: String,
    isMembershipError: Boolean,
    onMembershipChange: (String) -> Unit
){
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var birthdate by remember { mutableStateOf("") }

    var medicalHistory by remember { mutableStateOf("") }

    var direction by remember { mutableStateOf("") }
    var experience by remember { mutableStateOf("") }

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
            onNavigationClick = {/*TODO*/}
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
                    value = name,
                    onValueChange = { name = it },
                    singleLine = true,
                    isError = false
                )
                FormComponent(
                    textId = R.string.patient_register_email,
                    textFieldId = R.string.patient_register_text_field_email,
                    keyboardType = KeyboardType.Email,
                    value = email,
                    onValueChange = { email = it },
                    singleLine = true,
                    isError = false
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
                        if (isPasswordError) Text(text = "La contraseña debe tener más de 8 caracteres")
                    }
                )
                FormComponent(
                    textId = R.string.patient_register_birthdate,
                    textFieldId = R.string.patient_register_text_field_birthdate,
                    keyboardType = KeyboardType.Text,
                    value = birthdate,
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
                    },
                    isError = false
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
                        if (isDpiError) Text("El DPI solo debe contener dígitos")
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
                        if (isPhoneNumberError) Text("El número de teléfono solo debe contener dígitos")
                    }
                )

                if (role == Role.PATIENT){
                    FormComponent(
                        textId = R.string.patient_register_medical_history,
                        textFieldId = R.string.patient_register_text_field_medical_history,
                        keyboardType = KeyboardType.Text,
                        value = medicalHistory,
                        onValueChange = { medicalHistory = it },
                        singleLine = false,
                        isError = false
                    )
                }

                if (role == Role.DOCTOR){
                    FormComponent(
                        textId = R.string.doctor_register_membership_number,
                        textFieldId = R.string.doctor_register_text_field_membership_number,
                        keyboardType = KeyboardType.Text,
                        value = membership,
                        onValueChange = onMembershipChange,
                        singleLine = true,
                        isError = isMembershipError,
                        supportingText = {
                            if (isMembershipError) Text("El número de colegiado solo debe contener dígitos")
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
                        value = selectedSpecialty?.second ?: "",
                        onValueChange = { },
                        singleLine = true,
                        readOnly = true,
                        expanded = expanded,
                        onExpandedChange = { expanded = it },
                        dropdownItems = specialties.map { it.second },
                        onDropdownSelect = { selectedName ->
                            selectedSpecialty = specialties.first { it.second == selectedName }
                        }
                    )
                }

                CustomButton(
                    text = stringResource(id = R.string.register_button),
                    onClick = { /*TODO*/ },
                    colorBackground = MaterialTheme.colorScheme.primary,
                    maxWidth = true,
                    colorText = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomMediumTopAppBar(
    title: String,
    onNavigationClick: (() -> Unit),
){
    MediumTopAppBar(
        title = {
            Text(text = title)
        },
        navigationIcon = {
            IconButton(onClick = { onNavigationClick() }) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
        )
    )
}

@Composable
fun FormComponent(
    textId: Int,
    textFieldId: Int,
    keyboardType: KeyboardType,
    value: String,
    onValueChange: (String) -> Unit,
    singleLine: Boolean,
    trailingIcon: (@Composable (() -> Unit))? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    readOnly: Boolean = false,
    expanded: Boolean = false,
    onExpandedChange: (Boolean) -> Unit = {},
    dropdownItems: List<String> = emptyList(),
    onDropdownSelect: (String) -> Unit = {},
    isError: Boolean = false,
    supportingText: @Composable() (() -> Unit) = {}
) {
    Column {
        Text(
            text = stringResource(id = textId),
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.SemiBold
            )
        )
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            isError = isError,
            supportingText = supportingText,
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { if (dropdownItems.isNotEmpty()) onExpandedChange(true) },
            placeholder = {
                Text(
                    text = stringResource(id = textFieldId),
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            textStyle = MaterialTheme.typography.bodyMedium,
            readOnly = readOnly,
            shape = RoundedCornerShape(10.dp),
            singleLine = singleLine,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = keyboardType
            ),
            trailingIcon = trailingIcon
                ?: if (dropdownItems.isNotEmpty()) {
                    {
                        IconButton(onClick = { onExpandedChange(!expanded) }) {
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = null
                            )
                        }
                    }
                } else null,
            visualTransformation = visualTransformation
        )
        if (dropdownItems.isNotEmpty()) {
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { onExpandedChange(false) },
                modifier = Modifier
                    .height(200.dp)
            ) {
                dropdownItems.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(item) },
                        onClick = {
                            onDropdownSelect(item)
                            onExpandedChange(false)
                        }
                    )
                }
            }
        }
    }
}

fun specialtyToStringResource(specialty: Specialty): Int {
    return when (specialty) {
        Specialty.GENERAL -> R.string.specialty_general
        Specialty.CARDIOLOGY -> R.string.specialty_cardiology
        Specialty.DERMATOLOGY -> R.string.specialty_dermatology
        Specialty.ENDOCRINOLOGY -> R.string.specialty_endocrinology
        Specialty.GASTROENTEROLOGY -> R.string.specialty_gastroenterology
        Specialty.GYNECOLOGY -> R.string.specialty_gynecology
        Specialty.HEMATOLOGY -> R.string.specialty_hematology
        Specialty.INFECTIOLOGY -> R.string.specialty_infectiology
        Specialty.NEPHROLOGY -> R.string.specialty_nephrology
        Specialty.NEUROLOGY -> R.string.specialty_neurology
        Specialty.PULMONOLOGY -> R.string.specialty_pulmonology
        Specialty.OPHTHALMOLOGY -> R.string.specialty_ophthalmology
        Specialty.ONCOLOGY -> R.string.specialty_oncology
        Specialty.ORTHOPEDICS -> R.string.specialty_orthopedics
        Specialty.OTOLARYNGOLOGY -> R.string.specialty_otolaryngology
        Specialty.PEDIATRICS -> R.string.specialty_pediatrics
        Specialty.PSYCHIATRY -> R.string.specialty_psychiatry
        Specialty.RADIOLOGY -> R.string.specialty_radiology
        Specialty.RHEUMATOLOGY -> R.string.specialty_rheumatology
        Specialty.TRAUMATOLOGY -> R.string.specialty_traumatology
        Specialty.UROLOGY -> R.string.specialty_urology
        Specialty.ALLERGOLOGY -> R.string.specialty_allergology
        Specialty.ANGIOLOGY -> R.string.specialty_angiology
        Specialty.PLASTIC -> R.string.specialty_plastic
        Specialty.GERIATRICS -> R.string.specialty_geriatrics
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
@Composable
private fun PreviewPatientRegisterScreen() {
    DirectHealthTheme {
        Surface {
            RegisterScreen(
                role = Role.PATIENT,
                password = "",
                isPasswordError = false,
                onPasswordChange = {},
                dpi = "",
                isDpiError = false,
                onDpiChange = {},
                phoneNumber = "",
                isPhoneNumberError = false,
                onPhoneNumberChange = {},
                membership = "",
                isMembershipError = false,
                onMembershipChange = {}
            )
        }
    }
}

@Preview(
    showBackground = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun PreviewPatientRegisterScreenDark() {
    DirectHealthTheme {
        Surface {
            RegisterScreen(
                Role.PATIENT,
                password = "",
                isPasswordError = false,
                onPasswordChange = {},
                dpi = "",
                isDpiError = false,
                onDpiChange = {},
                phoneNumber = "",
                isPhoneNumberError = false,
                onPhoneNumberChange = {},
                membership = "",
                isMembershipError = false,
                onMembershipChange = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewDoctorRegisterScreen() {
    DirectHealthTheme {
        Surface {
            RegisterScreen(
                Role.DOCTOR,
                password = "",
                isPasswordError = false,
                onPasswordChange = {},
                dpi = "",
                isDpiError = false,
                onDpiChange = {},
                phoneNumber = "",
                isPhoneNumberError = false,
                onPhoneNumberChange = {},
                membership = "",
                isMembershipError = false,
                onMembershipChange = {}
            )
        }
    }
}

@Preview(
    showBackground = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun PreviewDoctorRegisterScreenDark() {
    DirectHealthTheme {
        Surface {
            RegisterScreen(
                Role.DOCTOR,
                password = "",
                isPasswordError = false,
                onPasswordChange = {},
                dpi = "",
                isDpiError = false,
                onDpiChange = {},
                phoneNumber = "",
                isPhoneNumberError = false,
                onPhoneNumberChange = {},
                membership = "",
                isMembershipError = false,
                onMembershipChange = {}
            )
        }
    }
}

@Preview
@Composable
private fun PreviewRegisterErrorScreen() {
    DirectHealthTheme {
        Surface {
            RegisterScreen(
                Role.PATIENT,
                password = "",
                isPasswordError = true,
                onPasswordChange = {},
                dpi = "",
                isDpiError = true,
                onDpiChange = {},
                phoneNumber = "",
                isPhoneNumberError = true,
                onPhoneNumberChange = {},
                membership = "",
                isMembershipError = true,
                onMembershipChange = {}
            )
        }
    }
}

@Preview(uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewRegisterErrorScreenDark() {
    DirectHealthTheme {
        Surface {
            RegisterScreen(
                Role.PATIENT,
                password = "",
                isPasswordError = true,
                onPasswordChange = {},
                dpi = "",
                isDpiError = true,
                onDpiChange = {},
                phoneNumber = "",
                isPhoneNumberError = true,
                onPhoneNumberChange = {},
                membership = "",
                isMembershipError = true,
                onMembershipChange = {}
            )
        }
    }
}