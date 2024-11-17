package com.uvg.directhealth.layouts.mainFlow.user.profile

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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.uvg.directhealth.R
import com.uvg.directhealth.domain.model.DoctorInfo
import com.uvg.directhealth.domain.model.Role
import com.uvg.directhealth.domain.model.Specialty
import com.uvg.directhealth.domain.model.User
import com.uvg.directhealth.layouts.common.CustomTopAppBar
import com.uvg.directhealth.layouts.common.SectionHeader
import com.uvg.directhealth.layouts.common.CustomButton
import com.uvg.directhealth.layouts.common.HasError
import com.uvg.directhealth.layouts.common.IsLoading
import com.uvg.directhealth.layouts.mainFlow.prescription.list.PrescriptionList
import com.uvg.directhealth.ui.theme.DirectHealthTheme
import java.time.LocalDate
import java.util.Calendar
import java.util.TimeZone

@Composable
fun UserProfileRoute(
    loggedUserId: String,
    userProfileId: String,
    viewModel: UserProfileViewModel = viewModel(factory = UserProfileViewModel.Factory),
    createNewPrescription: (String) -> Unit,
    scheduleAppointment: (String) -> Unit,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    viewModel.onEvent(UserProfileEvent.PopulateData(loggedUserId, userProfileId))

    UserProfileScreen(
        state = state,
        createNewPrescription = createNewPrescription,
        scheduleAppointment = scheduleAppointment,
        onNavigateBack = onNavigateBack,
        onEvent = { event -> viewModel.onEvent(event) }
    )
}

@Composable
private fun UserProfileScreen(
    state: UserProfileState,
    createNewPrescription: (String) -> Unit,
    scheduleAppointment: (String) -> Unit,
    onNavigateBack: () -> Unit,
    onEvent: (UserProfileEvent) -> Unit
) {
    val userProfile = state.userProfile
    val loggedUser = state.loggedUser

    Column (
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        CustomTopAppBar(
            onNavigationClick = { onNavigateBack() },
            backgroundColor = MaterialTheme.colorScheme.surface
        )

        when {
            state.isLoading -> IsLoading()
            state.hasError -> HasError()
            else -> {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 15.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    Alignment.CenterHorizontally,
                ) {
                    if (userProfile != null) {
                        ProfileHeader(name = userProfile.name)

                        // Patient Profile
                        if (userProfile.role == Role.PATIENT){
                            PatientProfile(userProfile, createNewPrescription)
                        }

                        // Doctor Profile
                        if (userProfile.role == Role.DOCTOR){
                            DoctorProfile(userProfile, scheduleAppointment, state, onEvent)
                        }
                    }

                }
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
    userSelected: User,
    createNewPrescription: (String) -> Unit
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
                userSelected.medicalHistory?.let {
                    Text(
                        text = userSelected.medicalHistory,
                        style = MaterialTheme.typography.bodyMedium.copy()
                    )
                }
            }
        }
    }

    CustomButton(
        text = stringResource(id = R.string.create_prescription),
        onClick = { createNewPrescription(userSelected.id) },
        colorBackground = MaterialTheme.colorScheme.secondaryContainer,
        colorText = MaterialTheme.colorScheme.onSecondaryContainer,
        icon = Icons.Filled.Create,
        contentDescriptionIcon = stringResource(id = R.string.create_prescription_icon)
    )

    Spacer(modifier = Modifier.width(10.dp))
}

@Composable
fun DoctorProfile(
    userSelected: User,
    scheduleAppointment: (String) -> Unit,
    state: UserProfileState,
    onEvent: (UserProfileEvent) -> Unit
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
                            text = userSelected.doctorInfo.address,
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
                        text = userSelected.doctorInfo.summary,
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
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .padding(20.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.select_date),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.ExtraBold
                        ),
                    )
                }
                MakeAppointment(state, onEvent)
            }

        }
    }
    CustomButton(
        text = stringResource(id = R.string.confirm_appointment),
        onClick = { scheduleAppointment(userSelected.id) },
        colorBackground = MaterialTheme.colorScheme.secondaryContainer,
        colorText = MaterialTheme.colorScheme.onSecondaryContainer,
        icon = Icons.Filled.Check,
        contentDescriptionIcon = stringResource(id = R.string.check_icon)
    )
}

@SuppressLint("DefaultLocale")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MakeAppointment(
    state: UserProfileState,
    onEvent: (UserProfileEvent) -> Unit
) {
    val timePickerState = rememberTimePickerState(
        initialHour = state.initialHour,
        initialMinute = state.initialMinute,
        is24Hour = state.is24Hour
    )
    val datePickerState = rememberDatePickerState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            stringResource(id = R.string.date) + ": ${state.selectedDate}",
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            stringResource(id = R.string.time) + ": ${state.selectedTime}",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onClick = { onEvent(UserProfileEvent.ToggleDatePicker(true)) }) {
                Icon(
                    imageVector = Icons.Filled.DateRange,
                    contentDescription = stringResource(id = R.string.date_icon)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(stringResource(id = R.string.date))
            }
            Spacer(modifier = Modifier.width(10.dp))
            Button(onClick = { onEvent(UserProfileEvent.ToggleTimePicker(true)) }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_timer),
                    contentDescription = stringResource(id = R.string.time_icon)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(stringResource(id = R.string.time))
            }
        }

        if (state.showDatePicker) {
            Dialog(onDismissRequest = { onEvent(UserProfileEvent.ToggleDatePicker(false))  }) {
                Surface(shape = MaterialTheme.shapes.medium) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .wrapContentWidth()
                    ) {
                        DatePicker(state = datePickerState)
                        Row(
                            horizontalArrangement = Arrangement.End,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            TextButton(onClick = { onEvent(UserProfileEvent.ToggleDatePicker(false)) }) {
                                Text(stringResource(id = R.string.cancel))
                            }
                            TextButton(onClick = {
                                onEvent(UserProfileEvent.ToggleDatePicker(false))
                                datePickerState.selectedDateMillis?.let { millis ->
                                    val calendar = Calendar.getInstance().apply {
                                        timeInMillis = millis
                                        timeZone = TimeZone.getDefault()
                                    }
                                    val date = "${calendar.get(Calendar.DAY_OF_MONTH) + 1}/${calendar.get(Calendar.MONTH) + 1}/${calendar.get(Calendar.YEAR)}"
                                    onEvent(UserProfileEvent.UpdateSelectedDate(date))
                                } ?: "dd/mm/yyyy"

                            }) {
                                Text(stringResource(id = R.string.confirm))
                            }
                        }
                    }
                }
            }
        }

        if (state.showTimePicker) {
            Dialog(onDismissRequest = { onEvent(UserProfileEvent.ToggleTimePicker(false)) }) {
                Surface(shape = MaterialTheme.shapes.medium) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .wrapContentWidth()
                    ) {
                        Box(
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        ) {
                            TimePicker(state = timePickerState)
                        }
                        Row(
                            horizontalArrangement = Arrangement.End,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            TextButton(onClick = { onEvent(UserProfileEvent.ToggleTimePicker(false)) }) {
                                Text(stringResource(id = R.string.cancel))
                            }
                            TextButton(onClick = {
                                onEvent(UserProfileEvent.ToggleTimePicker(false))
                                val time = String.format("%02d:%02d", timePickerState.hour, timePickerState.minute)
                                onEvent(UserProfileEvent.UpdateSelectedTime(time))
                            }) {
                                Text(stringResource(id = R.string.confirm))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewPatientUserProfileScreen() {
    DirectHealthTheme {
        Surface {
            UserProfileScreen(
                state = UserProfileState(
                    loggedUser = User(
                        id = "1",
                        role = Role.DOCTOR,
                        name = "Dr. Juan Pérez",
                        email = "juan.perez@directhealth.com",
                        password = "password123",
                        birthDate = LocalDate.of(1975, 5, 12),
                        dpi = "1234567890123",
                        phoneNumber = "12345678",
                        medicalHistory = null,
                        doctorInfo = DoctorInfo(
                            number = 1122,
                            address = "Calle Salud 123",
                            summary = "Cardiólogo experimentado con más de 20 años en el campo.",
                            specialty = Specialty.CARDIOLOGY
                        )
                    ),
                    userProfile = User(
                        id = "2",
                        role = Role.PATIENT,
                        name = "Ana Martínez",
                        email = "ana.martinez@gmail.com",
                        password = "password123",
                        birthDate = LocalDate.of(1990, 2, 20),
                        dpi = "9876543210123",
                        phoneNumber = "87654321",
                        medicalHistory = "Sin alergias conocidas. Cirugías previas: apendicectomía en 2010.",
                        doctorInfo = null
                    ),
                    isLoading = false
                ),
                onNavigateBack = {},
                createNewPrescription = {},
                scheduleAppointment = {},
                onEvent = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewDoctorUserProfileScreen() {
    DirectHealthTheme {
        Surface {
            UserProfileScreen(
                state = UserProfileState(
                    loggedUser = User(
                        id = "2",
                        role = Role.PATIENT,
                        name = "Ana Martínez",
                        email = "ana.martinez@gmail.com",
                        password = "password123",
                        birthDate = LocalDate.of(1990, 2, 20),
                        dpi = "9876543210123",
                        phoneNumber = "87654321",
                        medicalHistory = "Sin alergias conocidas. Cirugías previas: apendicectomía en 2010.",
                        doctorInfo = null
                    ),
                    userProfile = User(
                        id = "1",
                        role = Role.DOCTOR,
                        name = "Dr. Juan Pérez",
                        email = "juan.perez@directhealth.com",
                        password = "password123",
                        birthDate = LocalDate.of(1975, 5, 12),
                        dpi = "1234567890123",
                        phoneNumber = "12345678",
                        medicalHistory = null,
                        doctorInfo = DoctorInfo(
                            number = 1122,
                            address = "Calle Salud 123",
                            summary = "Cardiólogo experimentado con más de 20 años en el campo.",
                            specialty = Specialty.CARDIOLOGY
                        )
                    ),
                    isLoading = false
                ),
                onNavigateBack = {},
                createNewPrescription = {},
                scheduleAppointment = {},
                onEvent = {}
            )
        }
    }
}
