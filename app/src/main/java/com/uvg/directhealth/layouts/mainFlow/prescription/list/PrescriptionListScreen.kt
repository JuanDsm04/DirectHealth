package com.uvg.directhealth.layouts.mainFlow.prescription.list

import com.uvg.directhealth.data.model.Prescription
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.uvg.directhealth.R
import com.uvg.directhealth.data.model.DoctorInfo
import com.uvg.directhealth.data.model.PatientInfo
import com.uvg.directhealth.data.model.Role
import com.uvg.directhealth.data.model.Specialty
import com.uvg.directhealth.data.model.User
import com.uvg.directhealth.data.source.UserDb
import com.uvg.directhealth.data.source.PrescriptionDb
import com.uvg.directhealth.layouts.mainFlow.appointment.CustomMediumTopAppBar
import com.uvg.directhealth.ui.theme.DirectHealthTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun PrescriptionListRoute (
    userId: String,
    onPrescriptionClick: (String) -> Unit
) {
    val userDb = UserDb()
    val user = userDb.getUserById(userId)
    val prescriptionDb = PrescriptionDb()

    val prescriptions = if (user.role == Role.DOCTOR) {
        prescriptionDb.getPrescriptionsByDoctorId(userId)
    } else {
        prescriptionDb.getPrescriptionsByPatientId(userId)
    }

    PrescriptionListScreen(
        user = user,
        prescriptions = prescriptions,
        onPrescriptionClick = onPrescriptionClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PrescriptionListScreen(
    user: User,
    prescriptions: List<Prescription>,
    onPrescriptionClick: (String) -> Unit
){
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ){
        TopAppBar(
            title = {
                Text(
                    text = stringResource(id = R.string.prescription_list_title),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        )

        Box(modifier = Modifier.weight(1f)) {
            PrescriptionList(prescriptions = prescriptions, onPrescriptionClick = onPrescriptionClick, isDoctor = user.role == Role.DOCTOR)
        }
    }
}

@Composable
fun PrescriptionList(
    prescriptions: List<Prescription>,
    onPrescriptionClick: (String) -> Unit,
    isDoctor: Boolean
) {
    if (prescriptions.isEmpty()){
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
                .background(MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.5f)),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                painter =  painterResource(id = R.drawable.ic_folder_off),
                contentDescription = stringResource(id = R.string.folder_off_icon)
            )
            Text(text = stringResource(id = R.string.prescription_empty))
            Spacer(modifier = Modifier.weight(1f))
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                .background(MaterialTheme.colorScheme.surface),
            verticalArrangement = Arrangement.spacedBy(1.dp)
        ) {
            items(prescriptions) { item ->
                PrescriptionListItem(
                    prescription = item,
                    onPrescriptionClick = onPrescriptionClick,
                    isDoctor = isDoctor
                )
            }
        }
    }
}

@Composable
fun PrescriptionListItem(
    prescription: Prescription,
    onPrescriptionClick: (String) -> Unit,
    isDoctor: Boolean
) {
    val userDb = UserDb()
    val dateFormatter = remember { DateTimeFormatter.ofPattern("dd/MM/yyyy") }
    val name = if(isDoctor) {
        userDb.getUserById(prescription.patientId).name
    } else {
        userDb.getUserById(prescription.doctorId).name
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.5f))
            .clickable { onPrescriptionClick(prescription.id) }
            .padding(15.dp)
    ){
        Row (
            horizontalArrangement = Arrangement.spacedBy(15.dp)
        ){
            Icon(
                painter = painterResource(id = R.drawable.ic_prescriptions),
                contentDescription = stringResource(id = R.string.date_icon),
                tint = MaterialTheme.colorScheme.primary
            )
            Column {
                Text(
                    text = "Receta #${prescription.id}",
                    style = MaterialTheme.typography.titleMedium.copy()
                )
                Text(
                    text = stringResource(id = R.string.patient) + ": " + name,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                    )
                )
                Text(
                    text = stringResource(id = R.string.emissionDate) + ": ${prescription.emissionDate.format(dateFormatter)}",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewPrescriptionListScreenDoctor() {
    DirectHealthTheme {
        Surface {
            val prescriptionDb = PrescriptionDb()
            PrescriptionListScreen(
                user = User(
                    id = "1",
                    role = Role.DOCTOR,
                    name = "Dr. Juan Pérez",
                    email = "juan.perez@directhealth.com",
                    password = "password123",
                    birthDate = LocalDate.of(1975, 5, 12),
                    dpi = "1234567890123",
                    phoneNumber = "12345678",
                    patientInfo = null,
                    doctorInfo = DoctorInfo(
                        number = 1122,
                        address = "Calle Salud 123",
                        summary = "Cardiólogo experimentado con más de 20 años en el campo.",
                        specialty = Specialty.CARDIOLOGY
                    )
                ),
                prescriptions = prescriptionDb.getPrescriptionsByDoctorId("1"),
                onPrescriptionClick = { }
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewPrescriptionListScreenPatient() {
    DirectHealthTheme {
        Surface {
            val prescriptionDb = PrescriptionDb()
            PrescriptionListScreen(
                user = User(
                    id = "2",
                    role = Role.PATIENT,
                    name = "Ana Martínez",
                    email = "ana.martinez@gmail.com",
                    password = "password123",
                    birthDate = LocalDate.of(1990, 2, 20),
                    dpi = "9876543210123",
                    phoneNumber = "87654321",
                    patientInfo = PatientInfo(
                        medicalHistory = "Sin alergias conocidas. Cirugías previas: apendicectomía en 2010."
                    ),
                    doctorInfo = null
                ),
                prescriptions = prescriptionDb.getPrescriptionsByPatientId("2"),
                onPrescriptionClick = { }
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewPrescriptionListScreenPatientEmpty() {
    DirectHealthTheme {
        Surface {
            val prescriptionDb = PrescriptionDb()
            PrescriptionListScreen(
                user = User(
                    id = "5",
                    role = Role.PATIENT,
                    name = "Luis Fernández",
                    email = "luis.fernandez@gmail.com",
                    password = "password123",
                    birthDate = LocalDate.of(1985, 4, 10),
                    dpi = "1472583690123",
                    phoneNumber = "34567890",
                    patientInfo = PatientInfo(
                        medicalHistory = "Alergia a la penicilina. Sin cirugías previas."
                    ),
                    doctorInfo = null
                ),
                prescriptions = prescriptionDb.getPrescriptionsByPatientId("5"),
                onPrescriptionClick = { }
            )
        }
    }
}