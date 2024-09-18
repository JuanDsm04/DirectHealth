package com.uvg.directhealth.ui.layouts.doctorProfile

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.uvg.directhealth.R
import com.uvg.directhealth.db.AppointmentDb
import com.uvg.directhealth.db.UserDb
import com.uvg.directhealth.ui.layouts.login.CustomTopAppBar
import com.uvg.directhealth.ui.layouts.patientProfile.ProfileHeader
import com.uvg.directhealth.ui.theme.DirectHealthTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import com.uvg.directhealth.db.Appointment
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.UUID

@Composable
fun DoctorProfileScreen(idUser: String, idDoctor: String, userDb: UserDb, appointmentDb: AppointmentDb){
    val doctor = userDb.getUserById(idDoctor)

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
        Column (
            modifier = Modifier
                .padding(horizontal = 15.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            Alignment.CenterHorizontally,
        ) {
            ProfileHeader(name = doctor.name)

            Column {
                Box (
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer)
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
                                text = doctor.phoneNumber,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        Row {
                            Text(
                                text = stringResource(id = R.string.address) + ": ",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.ExtraBold
                                )
                            )
                            doctor.doctorInfo?.let {
                                Text(
                                    text = it.address,
                                    style = MaterialTheme.typography.bodyMedium
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
                        doctor.doctorInfo?.let {
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
                    CalendarLazyRow(idDoctor, idUser, appointmentDb)
                }
            }
            Spacer(modifier = Modifier.height(15.dp))
        }
    }
}

@Composable
fun CalendarLazyRow(idDoctor: String, idUser: String, appointmentDb: AppointmentDb) {
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

    Column {
        Text(
            text = stringResource(id = R.string.select_date),
            modifier = Modifier.padding(20.dp)
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

                    selectedDate = currentDate
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
            Spacer(modifier = Modifier.height(16.dp))
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
            DoctorProfileScreen("6","1", userDb, appointmentDb)
        }
    }
}