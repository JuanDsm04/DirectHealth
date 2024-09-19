package com.uvg.directhealth.ui.layouts.editProfile

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.uvg.directhealth.R
import com.uvg.directhealth.db.Role
import com.uvg.directhealth.db.UserDb
import com.uvg.directhealth.ui.layouts.register.FormComponent
import com.uvg.directhealth.ui.layouts.register.CustomMediumTopAppBar
import com.uvg.directhealth.ui.layouts.register.getSpecialtyItems
import com.uvg.directhealth.ui.layouts.register.specialtyToStringResource
import com.uvg.directhealth.ui.layouts.welcome.CustomButton
import com.uvg.directhealth.ui.theme.DirectHealthTheme
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun EditProfileScreen(idUser: String, userDb: UserDb){
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    val user = userDb.getUserById(idUser)
    var name by remember { mutableStateOf(user.name) }
    var email by remember { mutableStateOf(user.email) }
    var password by remember { mutableStateOf(user.password) }
    var passwordVisible by remember { mutableStateOf(false) }
    var birthdate by remember { mutableStateOf(dateFormat.format(SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(user.birthDate.toString()) ?: "")) }
    var dpi by remember { mutableStateOf(user.dpi) }
    var phoneNumber by remember { mutableStateOf(user.phoneNumber) }

    var medicalHistory by remember { mutableStateOf(user.patientInfo?.medicalHistory ?: "") }

    var membership by remember { mutableStateOf(user.doctorInfo?.number ?: "") }
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
            onNavigationClick = {/*TODO*/}
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
                Text(
                    text = stringResource(id = R.string.edit_profile_photo),
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .clickable { }
                )
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
                    onValueChange = { password = it },
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
                    onValueChange = { dpi = it },
                    singleLine = true
                )
                FormComponent(
                    textId = R.string.patient_register_phone_number,
                    textFieldId = R.string.patient_register_text_field_phone_number,
                    keyboardType = KeyboardType.Phone,
                    value = phoneNumber,
                    onValueChange = { phoneNumber = it },
                    singleLine = true
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
                        value = membership.toString(),
                        onValueChange = { membership = it },
                        singleLine = true
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

@Preview(showBackground = true)
@Composable
private fun PreviewPatientEditProfileScreen() {
    DirectHealthTheme {
        val userDb = UserDb()

        Surface {
            EditProfileScreen("2", userDb)
        }
    }
}

@Preview(
    showBackground = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun PreviewPatientEditProfileScreenDark() {
    DirectHealthTheme {
        val userDb = UserDb()

        Surface {
            EditProfileScreen("2", userDb)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewDoctorEditProfileScreen() {
    DirectHealthTheme {
        val userDb = UserDb()

        Surface {
            EditProfileScreen("1", userDb)
        }
    }
}

@Preview(
    showBackground = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun PreviewDoctorEditProfileScreenDark() {
    DirectHealthTheme {
        val userDb = UserDb()

        Surface {
            EditProfileScreen("1", userDb)
        }
    }
}