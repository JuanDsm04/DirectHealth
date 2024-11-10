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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.uvg.directhealth.R
import com.uvg.directhealth.data.model.Role
import com.uvg.directhealth.data.source.UserDb
import com.uvg.directhealth.data.source.PrescriptionDb
import com.uvg.directhealth.ui.theme.DirectHealthTheme
import java.time.format.DateTimeFormatter
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun PrescriptionListRoute (
    viewModel: PrescriptionListViewModel = viewModel(),
    onPrescriptionClick: (String) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    viewModel.onEvent(PrescriptionListEvent.PopulateData)

    PrescriptionListScreen(
        state = state,
        onPrescriptionClick = onPrescriptionClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PrescriptionListScreen(
    state: PrescriptionListState,
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
            PrescriptionList(prescriptions = state.prescriptionList, onPrescriptionClick = onPrescriptionClick, isDoctor = state.role == Role.DOCTOR)
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
                state = PrescriptionListState(
                    prescriptionList = prescriptionDb.getPrescriptionsByDoctorId("1"),
                    role = Role.DOCTOR
                ),
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
                state = PrescriptionListState(
                    prescriptionList = prescriptionDb.getPrescriptionsByPatientId("2"),
                    role = Role.PATIENT
                ),
                onPrescriptionClick = { }
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewEmptyPrescriptionListScreen() {
    DirectHealthTheme {
        Surface {
            val prescriptionDb = PrescriptionDb()
            PrescriptionListScreen(
                state = PrescriptionListState(
                    prescriptionList = prescriptionDb.getPrescriptionsByPatientId("5"),
                    role = Role.PATIENT
                ),
                onPrescriptionClick = { }
            )
        }
    }
}