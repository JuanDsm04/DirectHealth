package com.uvg.directhealth.ui.layouts.prescriptionList

import Prescription
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.uvg.directhealth.R
import com.uvg.directhealth.Role
import com.uvg.directhealth.db.UserDb
import com.uvg.directhealth.db.PrescriptionDb
import com.uvg.directhealth.ui.layouts.appointmentList.CustomBottomNavigationBar
import com.uvg.directhealth.ui.layouts.appointmentList.CustomMediumTopAppBar
import com.uvg.directhealth.ui.theme.DirectHealthTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun PrescriptionListScreen(userId: String, prescriptionDb: PrescriptionDb, userDb: UserDb){
    val user = userDb.getUserById(userId)

    val prescriptions = if (user.role == Role.DOCTOR) {
        prescriptionDb.getPrescriptionsByDoctorId(userId)
    } else {
        prescriptionDb.getPrescriptionsByPatientId(userId)
    }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ){
        CustomMediumTopAppBar(
            title = stringResource(id = R.string.prescription_list_title),
            onActionsClick = {/* */},
            backgroundColor = MaterialTheme.colorScheme.surface
        )

        Box(modifier = Modifier.weight(1f)) {
            PrescriptionList(prescriptions = prescriptions, userDb = userDb, isDoctor = user.role == Role.DOCTOR)
        }

        CustomBottomNavigationBar(isDoctor = user.role == Role.DOCTOR, 1)
    }
}

@Composable
fun PrescriptionList(prescriptions: List<Prescription>, userDb: UserDb, isDoctor: Boolean) {
    if (prescriptions.isEmpty()){
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
                .background(MaterialTheme.colorScheme.surfaceContainer),
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
            items(prescriptions.size) { index ->
                val prescription = prescriptions[index]
                val doctor = userDb.getUserById(prescription.doctorId)
                val patient = userDb.getUserById(prescription.patientId)

                PrescriptionListItem(
                    prescriptionId = prescription.id,
                    doctorName = doctor.name,
                    patientName = patient.name,
                    emissionDate = prescription.emissionDate,
                    isDoctor = isDoctor
                )
            }
        }
    }
}

@Composable
fun PrescriptionListItem(
    prescriptionId: String,
    doctorName: String,
    patientName: String,
    emissionDate: LocalDate,
    isDoctor: Boolean
) {
    val dateFormatter = remember { DateTimeFormatter.ofPattern("dd/MM/yyyy") }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceContainer.copy(alpha =0.5f ))
            .clickable { /**/ }
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
                    text = "Receta #$prescriptionId",
                    style = MaterialTheme.typography.titleMedium.copy()
                )
                if (!isDoctor) {
                    Text(
                        text = stringResource(id = R.string.prescription_doctor) + ": $doctorName",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                        )
                    )
                }
                if (isDoctor) {
                    Text(
                        text = stringResource(id = R.string.prescription_patient) + ": $patientName",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                        )
                    )
                }
                Text(
                    text = stringResource(id = R.string.prescription_emissionDate) + ": ${emissionDate.format(dateFormatter)}",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewDoctorPrescriptionListScreen() {
    val userDb = UserDb()
    val prescriptionDb = PrescriptionDb()

    DirectHealthTheme {
        Surface {
            PrescriptionListScreen("1", prescriptionDb, userDb)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewPatientPrescriptionListScreen() {
    val userDb = UserDb()
    val prescriptionDb = PrescriptionDb()

    DirectHealthTheme {
        Surface {
            PrescriptionListScreen("2", prescriptionDb, userDb)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewPrescriptionListEmptyScreen() {
    val userDb = UserDb()
    val prescriptionDb = PrescriptionDb()

    DirectHealthTheme {
        Surface {
            PrescriptionListScreen("5", prescriptionDb, userDb)
        }
    }
}