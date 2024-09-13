package com.uvg.directhealth.ui.layouts.doctorEdit

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
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
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
import com.uvg.directhealth.ui.theme.DirectHealthTheme
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorEditScreen(){
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var birthdate by remember { mutableStateOf("") }
    var dpi by remember { mutableStateOf("") }
    var membership by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var direction by remember { mutableStateOf("") }
    var experience by remember { mutableStateOf("") }
    var certifications by remember { mutableStateOf("") }
    val calendar = Calendar.getInstance()

    var selectedSpecialty by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    val specialties = LocalContext.current.resources.getStringArray(R.array.specialties).toList()

    val datePickerDialog = DatePickerDialog(
        LocalContext.current,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            birthdate = "$dayOfMonth/${month + 1}/$year"
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onPrimary)
            .verticalScroll(rememberScrollState())
    ){
        MediumTopAppBar(
            title = {
                Text(text = stringResource(id = R.string.edit_profile_title))
            },
            navigationIcon = {
                IconButton({}) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.onPrimary
            )
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
                .background(MaterialTheme.colorScheme.primaryContainer)
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
                    value = birthdate,
                    onValueChange = { birthdate = it },
                    singleLine = true,
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
                    textId = R.string.doctor_register_membership_number,
                    textFieldId = R.string.doctor_register_text_field_membership_number,
                    keyboardType = KeyboardType.Text,
                    value = membership,
                    onValueChange = { membership = it },
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
                    textId = R.string.doctor_register_certifications,
                    textFieldId = R.string.doctor_register_text_field_certifications,
                    keyboardType = KeyboardType.Text,
                    value = certifications,
                    onValueChange = { certifications = it },
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
                    dropdownItems = specialties,
                    onDropdownSelect = { selectedSpecialty = it }
                )
                TextButton(
                    onClick = { },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(15.dp))
                        .background(MaterialTheme.colorScheme.primary)
                        .height(50.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.register_button),
                        style = MaterialTheme.typography.labelLarge.copy(
                            color = MaterialTheme.colorScheme.onPrimary,
                        )
                    )
                }
            }
        }
    }
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
    onDropdownSelect: (String) -> Unit = {}
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
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.onPrimary)
                .clickable { if (dropdownItems.isNotEmpty()) onExpandedChange(true) },
            placeholder = {
                Text(
                    text = stringResource(id = textFieldId),
                    style = MaterialTheme.typography.bodySmall
                )
            },
            readOnly = readOnly,
            shape = RoundedCornerShape(10.dp),
            singleLine = singleLine,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = keyboardType
            ),
            trailingIcon = trailingIcon ?: {
                if (dropdownItems.isNotEmpty()) {
                    IconButton(onClick = { onExpandedChange(!expanded) }) {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = null
                        )
                    }
                }
            },
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

@Preview(showBackground = true)
@Composable
private fun PreviewDoctorEditScreen() {
    DirectHealthTheme {
        Surface {
            DoctorEditScreen()
        }
    }
}