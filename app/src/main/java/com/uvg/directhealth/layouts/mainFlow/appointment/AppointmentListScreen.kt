package com.uvg.directhealth.layouts.mainFlow.appointment

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
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.uvg.directhealth.R
import com.uvg.directhealth.data.model.Role
import com.uvg.directhealth.data.model.Appointment
import com.uvg.directhealth.data.model.DoctorInfo
import com.uvg.directhealth.data.model.PatientInfo
import com.uvg.directhealth.data.model.Specialty
import com.uvg.directhealth.data.model.User
import com.uvg.directhealth.data.source.AppointmentDb
import com.uvg.directhealth.data.source.UserDb
import com.uvg.directhealth.ui.theme.DirectHealthTheme
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun AppointmentListRoute (
    id: String
) {
    val userDb = UserDb()
    val user = userDb.getUserById(id)
    val appointmentDb = AppointmentDb()

    val appointments = if (user.role == Role.DOCTOR) {
        appointmentDb.getAppointmentsByDoctorId(user.id)
    } else {
        appointmentDb.getAppointmentsByPatientId(user.id)
    }

    AppointmentListScreen(
        user = user,
        appointments = appointments,
    )
}

@Composable
fun AppointmentListScreen(
    user: User,
    appointments: List<Appointment>
){
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ){
        CustomMediumTopAppBar(
            title = stringResource(id = R.string.appointment_list_title),
            onActionsClick = {/* */},
            backgroundColor = MaterialTheme.colorScheme.surface
        )

        Box(modifier = Modifier.weight(1f)) {
            AppointmentList(appointments = appointments, isDoctor = user.role == Role.DOCTOR)
        }

        CustomBottomNavigationBar(isDoctor = user.role == Role.DOCTOR, 2)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomMediumTopAppBar(
    title: String,
    onNavigationClick: (() -> Unit)? = null,
    onActionsClick: (() -> Unit)? = null,
    backgroundColor: Color
){
    MediumTopAppBar(
        title = { Text(text = title)},
        navigationIcon = {
            if (onNavigationClick != null) {
                IconButton(onClick = { onNavigationClick() }) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = stringResource(id = R.string.back_icon)
                    )
                }
            }
        },
        actions = {
            if (onActionsClick != null) {
                IconButton(onClick = { onActionsClick() }) {
                    Icon(
                        Icons.Default.Settings,
                        contentDescription = stringResource(id = R.string.settings_icon)
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = backgroundColor
        )
    )
}

@Composable
fun AppointmentList(appointments: List<Appointment>, isDoctor: Boolean) {
    val userDb = UserDb()
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
                if(isDoctor){
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

@Composable
fun CustomBottomNavigationBar(
    isDoctor: Boolean,
    itemSelected: Int
) {
    var selectedItem by remember { mutableIntStateOf(itemSelected) }

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
@Preview(
    showBackground = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun PreviewPatientAppointmentListScreen() {
    DirectHealthTheme {
        Surface {
            val appointmentDb = AppointmentDb()

            AppointmentListScreen(
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
                appointments = appointmentDb.getAppointmentsByPatientId("2")
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(
    showBackground = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun PreviewDoctorAppointmentListScreen() {

    DirectHealthTheme {
        val appointmentDb = AppointmentDb()
        Surface {
            AppointmentListScreen(
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
                appointments = appointmentDb.getAppointmentsByDoctorId("1")
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(
    showBackground = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun PreviewAppointmentListEmptyScreen() {
    DirectHealthTheme {
        Surface {
            val appointmentDb = AppointmentDb()

            AppointmentListScreen(
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
                appointments = appointmentDb.getAppointmentsByPatientId("5")
            )
        }
    }
}