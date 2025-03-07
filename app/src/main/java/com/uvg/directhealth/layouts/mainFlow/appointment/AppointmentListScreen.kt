package com.uvg.directhealth.layouts.mainFlow.appointment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.uvg.directhealth.R
import com.uvg.directhealth.domain.model.Role
import com.uvg.directhealth.data.model.Appointment
import com.uvg.directhealth.data.source.AppointmentDb
import com.uvg.directhealth.domain.model.User
import com.uvg.directhealth.layouts.common.HasError
import com.uvg.directhealth.layouts.common.IsLoading
import com.uvg.directhealth.ui.theme.DirectHealthTheme
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun AppointmentListRoute(
    viewModel: AppointmentListViewModel = viewModel(factory = AppointmentListViewModel.Factory)
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.onEvent(AppointmentListEvent.PopulateData)
    }

    AppointmentListScreen(
        state = state,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentListScreen(
    state: AppointmentListState,
){
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ){
        TopAppBar(
            title = {
                Text(
                    text = stringResource(id = R.string.appointment_list_title),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        )

        when {
            state.isLoading -> IsLoading()
            state.hasError -> HasError()
            else -> {
                Box(modifier = Modifier.weight(1f)) {
                    AppointmentList(
                        appointments = state.appointmentList,
                        userDetails = state.userDetails,
                        isDoctor = state.role == Role.DOCTOR
                    )
                }
            }
        }
    }
}

@Composable
fun AppointmentList(appointments: List<Appointment>, userDetails: List<User>, isDoctor: Boolean) {
    if (appointments.isEmpty()){
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
            Text(text = stringResource(id = R.string.appointment_empty))
            Spacer(modifier = Modifier.weight(1f))
        }

    } else {
        LazyColumn(
            modifier = Modifier
                .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                .background(MaterialTheme.colorScheme.surface),
            verticalArrangement = Arrangement.spacedBy(1.dp)
        ) {
            items(appointments.size) { index ->
                val appointment = appointments[index]
                val doctor = userDetails.find { it.id == appointment.doctorId }
                val patient = userDetails.find { it.id == appointment.patientId }

                if (doctor != null && patient != null) {
                    AppointmentListItem(
                        doctorName = doctor.name,
                        patientName = patient.name,
                        patientPhone = patient.phoneNumber,
                        doctorPhone = doctor.phoneNumber,
                        doctorAddress = doctor.doctorInfo?.address,
                        appointmentDate = appointment.date,
                        isDoctor = isDoctor
                    )
                }
            }
        }
    }
}

@Composable
fun AppointmentListItem(
    doctorName: String,
    patientName: String,
    patientPhone: String,
    doctorPhone: String,
    doctorAddress: String?,
    appointmentDate: LocalDateTime,
    isDoctor: Boolean
) {
    val dateFormatter = remember { DateTimeFormatter.ofPattern("dd/MM/yyyy") }
    val timeFormatter = remember { DateTimeFormatter.ofPattern("HH:mm") }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.5f))
            .padding(15.dp)
    ){
        Row (
            horizontalArrangement = Arrangement.spacedBy(15.dp)
        ){
            Icon(
                Icons.Filled.DateRange,
                contentDescription = stringResource(id = R.string.date_icon),
                tint = MaterialTheme.colorScheme.primary
            )

            Column {

                if (isDoctor) {
                    Text(
                        text = patientName,
                        style = MaterialTheme.typography.titleMedium.copy()
                    )

                } else {
                    Text(
                        text = doctorName,
                        style = MaterialTheme.typography.titleMedium.copy()
                    )
                }

                Text(
                    text = stringResource(id = R.string.date) + ": ${appointmentDate.format(dateFormatter)}",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                    )
                )

                Text(
                    text = stringResource(id = R.string.time) + ": ${appointmentDate.toLocalTime().format(timeFormatter)}",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                    )
                )

                if (isDoctor){
                    Text(
                        text = stringResource(id = R.string.contact) + ": $patientPhone",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                        )
                    )

                } else {
                    Text(
                        text = stringResource(id = R.string.contact) + ": $doctorPhone",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                        )
                    )

                    doctorAddress?.let {
                        Text(
                            text = stringResource(id = R.string.address) + ": $it",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                            )
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewPatientAppointmentListScreen() {
    DirectHealthTheme {
        Surface {
            val appointmentDb = AppointmentDb()
            AppointmentListScreen(
                state = AppointmentListState(
                    appointmentList = appointmentDb.getAppointmentsByPatientId("2"),
                    role = Role.PATIENT
                )
            )
        }
    }
}
