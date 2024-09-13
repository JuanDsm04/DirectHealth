package com.uvg.directhealth.ui.layouts.appointmentList

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.uvg.directhealth.R
import com.uvg.directhealth.Role
import com.uvg.directhealth.db.Appointment
import com.uvg.directhealth.db.AppointmentDb
import com.uvg.directhealth.db.UserDb
import com.uvg.directhealth.ui.theme.DirectHealthTheme
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun AppointmentListScreen(userId: String, appointmentDb: AppointmentDb, userDb: UserDb){
    val user = userDb.getUserById(userId)

    val appointments = if (user.role == Role.DOCTOR) {
        appointmentDb.getAppointmentsByDoctorId(userId)
    } else {
        appointmentDb.getAppointmentsByPatientId(userId)
    }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onPrimary)
    ){
        AppBar()

        Box(modifier = Modifier.weight(1f)) {
            AppointmentList(appointments = appointments, userDb = userDb, isDoctor = user.role == Role.DOCTOR)
        }

        BottomNavigationBar(isDoctor = user.role == Role.DOCTOR)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(){
    MediumTopAppBar(
        title = {
            Text(text = stringResource(id = R.string.appointment_list_title))
        },
        actions = {
            IconButton({/**/}) {
                Icon(
                    Icons.Default.Settings,
                    contentDescription = stringResource(id = R.string.settings_icon)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.primary,
        )
    )
}

@Composable
fun AppointmentList(appointments: List<Appointment>, userDb: UserDb, isDoctor: Boolean) {
    if (appointments.isEmpty()){
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
            Text(text = stringResource(id = R.string.appointment_empty))
            Spacer(modifier = Modifier.weight(1f))
        }

    } else {
        LazyColumn(
            modifier = Modifier
                .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                .background(MaterialTheme.colorScheme.surfaceContainer),
            verticalArrangement = Arrangement.spacedBy(1.dp)
        ) {
            items(appointments.size) { index ->
                val appointment = appointments[index]
                val doctor = userDb.getUserById(appointment.doctorId)
                val patient = userDb.getUserById(appointment.patientId)

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
            .background(MaterialTheme.colorScheme.surface)
            .clickable { /**/ }
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
                    text = stringResource(id = R.string.appointment_date) + ": ${appointmentDate.format(dateFormatter)}",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                    )
                )
                Text(
                    text = stringResource(id = R.string.appointment_time) + ": ${appointmentDate.toLocalTime().format(timeFormatter)}",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                    )
                )
                if(isDoctor){
                    Text(
                        text = stringResource(id = R.string.appointment_contact) + ": $patientPhone",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                        )
                    )
                } else {
                    Text(
                        text = stringResource(id = R.string.appointment_contact) + ": $doctorPhone",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                        )
                    )
                    doctorAddress?.let {
                        Text(
                            text = stringResource(id = R.string.appointment_address) + ": $it",
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

@Composable
fun BottomNavigationBar(
    isDoctor: Boolean
) {
    var selectedItem by remember { mutableIntStateOf(2) }

    NavigationBar (
        containerColor = MaterialTheme.colorScheme.surfaceContainer
    ){
        NavigationBarItem(
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_groups),
                    contentDescription = stringResource(id = R.string.groups_icon)
                )},
            selected = selectedItem == 0,
            onClick = { selectedItem = 0 },
            label = {
                if (!isDoctor) {
                    Text(text = stringResource(id = R.string.nav_doctors))
                } else {
                    Text(text = stringResource(id = R.string.nav_patients))
                }},
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.onTertiaryContainer,
                indicatorColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        )
        NavigationBarItem(
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_prescriptions),
                    contentDescription = stringResource(id = R.string.prescriptions_icon)
                )},
            selected = selectedItem == 1,
            onClick = { selectedItem = 1 },
            label = { Text(text = stringResource(id = R.string.nav_prescriptions)) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.onTertiaryContainer,
                indicatorColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        )
        NavigationBarItem(
            icon = {
                Icon(
                    Icons.Filled.DateRange,
                    contentDescription = stringResource(id = R.string.date_icon),
                )},
            selected = selectedItem == 2,
            onClick = { selectedItem = 2 },
            label = { Text(text = stringResource(id = R.string.nav_appointments)) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.onTertiaryContainer,
                indicatorColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewDoctorAppointmentListScreen() {
    val userDb = UserDb()
    val appointmentDb = AppointmentDb(userDb)

    DirectHealthTheme {
        Surface {
            AppointmentListScreen("1", appointmentDb, userDb)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewPatientAppointmentListScreen() {
    val userDb = UserDb()
    val appointmentDb = AppointmentDb(userDb)

    DirectHealthTheme {
        Surface {
            AppointmentListScreen("2", appointmentDb, userDb)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewAppointmentListEmptyScreen() {
    val userDb = UserDb()
    val appointmentDb = AppointmentDb(userDb)

    DirectHealthTheme {
        Surface {
            AppointmentListScreen("5", appointmentDb, userDb)
        }
    }
}