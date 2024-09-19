package com.uvg.directhealth.ui.layouts.profile

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.uvg.directhealth.R
import com.uvg.directhealth.db.Appointment
import com.uvg.directhealth.db.AppointmentDb
import com.uvg.directhealth.db.Role
import com.uvg.directhealth.db.User
import com.uvg.directhealth.db.UserDb
import com.uvg.directhealth.ui.layouts.login.CustomTopAppBar
import com.uvg.directhealth.ui.layouts.prescription.SectionHeader
import com.uvg.directhealth.ui.layouts.welcome.CustomButton
import com.uvg.directhealth.ui.theme.DirectHealthTheme
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.UUID

@Composable
fun ProfileScreen(idUser: String, idUserSelected: String, userDb: UserDb, appointmentDb: AppointmentDb) {
    val user = userDb.getUserById(idUser)
    val userSelected = userDb.getUserById(idUserSelected)

    Column (
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        CustomTopAppBar(
            onNavigationClick = { /* */ },
            onActionsClick = { /* */ },
            backgroundColor = MaterialTheme.colorScheme.surface
        )
        Column(
            modifier = Modifier
                .padding(horizontal = 15.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            Alignment.CenterHorizontally,
        ) {
            ProfileHeader(name = userSelected.name)

            // Patient Profile
            if (userSelected.role == Role.PATIENT){
                PatientProfile(userSelected)
            }

            // Doctor Profile
            if (userSelected.role == Role.DOCTOR){
                DoctorProfile(user, userSelected, appointmentDb)
            }

        }
    }
}

@Composable
fun ProfileHeader(
    name: String
){
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        Alignment.CenterVertically
    ){
        Text(
            text = name,
            style = MaterialTheme.typography.titleLarge.copy(
                color = MaterialTheme.colorScheme.onSurface
            ),
            modifier = Modifier
                .weight(1f)
                .padding(end = 10.dp)
        )
        Image(
            painter = painterResource(id = R.drawable.user),
            contentDescription = stringResource(id = R.string.user_img),
            modifier = Modifier
                .size(125.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun PatientProfile(
    userSelected: User
) {
    val age = LocalDate.now().year - userSelected.birthDate.year
    Box{
        Column {
            SectionHeader(stringResource(id = R.string.general_data))
            Box (
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(bottomStart = 15.dp, bottomEnd = 15.dp))
                    .background(MaterialTheme.colorScheme.surfaceContainer)
                    .padding(20.dp)
            ){
                Column {
                    Row {
                        Text(
                            text = stringResource(id = R.string.phone_number) + ": ",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.ExtraBold
                            )
                        )
                        Text(
                            text = userSelected.phoneNumber,
                            style = MaterialTheme.typography.bodyMedium.copy()
                        )
                    }
                    Row {
                        Text(
                            text = stringResource(id = R.string.age) + ": ",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.ExtraBold
                            )
                        )
                        Text(
                            text = age.toString(),
                            style = MaterialTheme.typography.bodyMedium.copy()
                        )
                    }
                }
            }
        }
    }

    Box {
        Column {
            SectionHeader(stringResource(id = R.string.medical_history))
            Box (
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(bottomStart = 15.dp, bottomEnd = 15.dp))
                    .background(MaterialTheme.colorScheme.surfaceContainer)
                    .padding(20.dp)
            ){
                userSelected.patientInfo?.let {
                    Text(
                        text = it.medicalHistory,
                        style = MaterialTheme.typography.bodyMedium.copy()
                    )
                }
            }
        }
    }

    CustomButton(
        text = stringResource(id = R.string.create_prescription),
        onClick = {/**/},
        colorBackground = MaterialTheme.colorScheme.secondaryContainer,
        colorText = MaterialTheme.colorScheme.onSecondaryContainer,
        icon = Icons.Filled.Create,
        contentDescriptionIcon = stringResource(id = R.string.create_prescription_icon)
    )

    Spacer(modifier = Modifier.width(10.dp))
}

@Composable
fun DoctorProfile(
    user: User,
    userSelected: User,
    appointmentDb: AppointmentDb
) {
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp))
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(20.dp)
        ) {
            Column {
                Row {
                    Text(
                        text = stringResource(id = R.string.phone_number) + ": ",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.ExtraBold
                        ),
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = userSelected.phoneNumber,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                Row {
                    Text(
                        text = stringResource(id = R.string.address) + ": ",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                        ),
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    userSelected.doctorInfo?.let {
                        Text(
                            text = it.address,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
        }
        Box (
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomStart = 15.dp, bottomEnd = 15.dp))
                .background(MaterialTheme.colorScheme.surfaceContainer)
                .padding(20.dp)
        ){
            Column {
                userSelected.doctorInfo?.let {
                    Text(
                        text = it.summary,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
    Column {
        Box (
            modifier = Modifier
                .clip(RoundedCornerShape(15.dp))
                .background(MaterialTheme.colorScheme.surfaceContainer)
        ){
            CalendarLazyRow(user.id, userSelected.id, appointmentDb)
        }
    }
    Spacer(modifier = Modifier.height(15.dp))
}

@SuppressLint("DefaultLocale")
fun formatTime(time: Int): String {
    val hour = if (time > 12) time - 12 else time
    val period = if (time >= 12) "PM" else "AM"
    return String.format(Locale.getDefault(), "%02d:00 %s", hour, period)
}

fun formatDateTime(dateTime: LocalDateTime): String {
    val formatter = DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy")
    return dateTime.format(formatter)
}

@Composable
fun CalendarLazyRow(idUser: String, idDoctor: String, appointmentDb: AppointmentDb) {
    val currentDate by remember { mutableStateOf(LocalDateTime.now()) }
    var selectedDate by remember { mutableStateOf(currentDate.plusDays(1)) }
    var selectedTime by remember { mutableStateOf<Int?>(null) }

    val bookedAppointments = remember {
        mutableStateListOf(*appointmentDb.getAppointmentsByDoctorId(idDoctor).toTypedArray())
    }

    val availableTimes = listOf(7, 8, 9, 10, 15, 16)

    fun getDatesFromCurrentDate(): List<LocalDateTime> {
        val dates = mutableListOf<LocalDateTime>()
        var date = currentDate.plusDays(1)

        for (i in 0 until 30) {
            dates.add(date)
            date = date.plusDays(1)
        }
        return dates
    }

    Column {
        Text(
            text = stringResource(id = R.string.select_date),
            modifier = Modifier.padding(20.dp),
            style = MaterialTheme.typography.titleMedium
        )
        LazyRow (
            modifier = Modifier
                .padding(start = 20.dp)
        ) {
            items(getDatesFromCurrentDate()) { date ->
                TextButton(
                    onClick = { selectedDate = date },
                    colors = if (date == selectedDate) {
                        ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    } else {
                        ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    },
                ) {
                    Text(
                        text = formatDateTime(date).replaceFirstChar {
                            if (it.isLowerCase()) it.titlecase(
                                Locale.ROOT
                            ) else it.toString()
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (date == selectedDate) MaterialTheme.colorScheme.onPrimary
                        else MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column (
            modifier = Modifier
                .padding(horizontal = 20.dp)
        ) {
            Text(
                text = stringResource(id = R.string.available_times)+ " ${formatDateTime(selectedDate)}:",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .padding(8.dp)
            )

            Column(modifier = Modifier.fillMaxWidth()) {
                availableTimes.chunked(2).forEach { timeRow ->

                    Row(modifier = Modifier.fillMaxWidth()) {
                        timeRow.forEach { time ->
                            val isBooked = bookedAppointments.any {
                                it.date.toLocalDate() == selectedDate.toLocalDate() && it.date.hour == time
                            }

                            TextButton(
                                onClick = { selectedTime = time },
                                enabled = !isBooked,
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(8.dp),
                                colors = if (time == selectedTime) {
                                    ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.primary
                                    )
                                } else {
                                    ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.primaryContainer
                                    )
                                },
                            ) {
                                Text(
                                    text = formatTime(time),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = if (time == selectedTime) MaterialTheme.colorScheme.onPrimary
                                    else MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(
                onClick = {
                    val newAppointment = Appointment(
                        id = UUID.randomUUID().toString(),
                        doctorId = idDoctor,
                        patientId = idUser,
                        date = selectedDate!!.withHour(selectedTime!!)
                    )

                    appointmentDb.addAppointment(newAppointment)
                    bookedAppointments.add(newAppointment)

                    selectedDate = currentDate.plusDays(1)
                    selectedTime = null

                },
                enabled = selectedDate != null && selectedTime != null,
                modifier = Modifier
                    .clip(RoundedCornerShape(15.dp))
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedDate != null && selectedTime != null) {
                        MaterialTheme.colorScheme.secondaryContainer
                    } else {
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                    }
                ),
            ) {
                Text(
                    text = stringResource(id = R.string.confirm_appointment),
                    color = if (selectedDate != null && selectedTime != null) {
                        MaterialTheme.colorScheme.onSecondaryContainer
                    } else {
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                    }
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewPatientProfileScreen() {
    val userDb = UserDb()
    val appointmentDb = AppointmentDb()

    DirectHealthTheme {
        Surface {
            ProfileScreen("1","2", userDb, appointmentDb)
        }
    }
}

@Preview(
    showBackground = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun PreviewPatientProfileScreenDark() {
    val userDb = UserDb()
    val appointmentDb = AppointmentDb()

    DirectHealthTheme {
        Surface {
            ProfileScreen("1","2", userDb, appointmentDb)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewDoctorProfileScreen() {
    val userDb = UserDb()
    val appointmentDb = AppointmentDb()

    DirectHealthTheme {
        Surface {
            ProfileScreen("2","1", userDb, appointmentDb)
        }
    }
}

@Preview(
    showBackground = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun PreviewDoctorProfileScreenDark() {
    val userDb = UserDb()
    val appointmentDb = AppointmentDb()

    DirectHealthTheme {
        Surface {
            ProfileScreen("2","1", userDb, appointmentDb)
        }
    }
}