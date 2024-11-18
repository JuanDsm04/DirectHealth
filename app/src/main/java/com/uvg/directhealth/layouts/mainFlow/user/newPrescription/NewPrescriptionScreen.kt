package com.uvg.directhealth.layouts.mainFlow.user.newPrescription

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.uvg.directhealth.R
import com.uvg.directhealth.layouts.common.SectionHeader
import com.uvg.directhealth.layouts.common.CustomButton
import com.uvg.directhealth.layouts.common.CustomListItem
import com.uvg.directhealth.domain.model.DoctorInfo
import com.uvg.directhealth.data.model.Medication
import com.uvg.directhealth.domain.model.Role
import com.uvg.directhealth.domain.model.Specialty
import com.uvg.directhealth.domain.model.User
import com.uvg.directhealth.layouts.mainFlow.user.profile.UserProfileEvent
import com.uvg.directhealth.ui.theme.DirectHealthTheme
import java.time.LocalDate

@Composable
fun NewPrescriptionRoute(
    loggedUserId: String,
    userProfileId: String,
    viewModel: NewPrescriptionViewModel = viewModel(factory = NewPrescriptionViewModel.Factory),
    onBackNavigation: () -> Unit,
    ) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val context = LocalContext.current
    if (state.successfulCreatePrescription) {
        Toast.makeText(
            context,
            context.getString(R.string.prescription_success_message),
            Toast.LENGTH_SHORT
        ).show()

        viewModel.onEvent(NewPrescriptionEvent.ResetPrescription)
    }

    viewModel.onEvent(NewPrescriptionEvent.PopulateData(loggedUserId, userProfileId))

    NewPrescriptionScreen(
        state = state,
        onBackNavigation = onBackNavigation,
        onConfirmPrescription = {
            viewModel.onEvent(NewPrescriptionEvent.NewPrescription)
        },
        onEvent = viewModel::onEvent,
        onNameMedicineChange = {
            viewModel.onEvent(NewPrescriptionEvent.NameMedicineChange(it))
        },
        onDescriptionMedicineChange = {
            viewModel.onEvent(NewPrescriptionEvent.DescriptionMedicineChange(it))
        },
        onNoteChange = {
            viewModel.onEvent(NewPrescriptionEvent.NoteChange(it))
        }
    )
}

@Composable
private fun NewPrescriptionScreen(
    state: NewPrescriptionState,
    onBackNavigation: () -> Unit,
    onConfirmPrescription: () -> Unit,
    onEvent: (NewPrescriptionEvent) -> Unit,
    onNameMedicineChange: (String) -> Unit,
    onDescriptionMedicineChange: (String) -> Unit,
    onNoteChange: (String) -> Unit
){
    val patientUser = state.patientUser
    val age = LocalDate.now().year - (patientUser?.birthDate?.year ?: LocalDate.now().year)

    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .verticalScroll(rememberScrollState())
    ){
        CustomMediumTopAppBar(
            title = stringResource(id = R.string.new_prescription),
            onNavigationClick = { onBackNavigation() },
            backgroundColor = MaterialTheme.colorScheme.surface
        )

        Column (
            modifier = Modifier
                .padding(horizontal = 15.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            Alignment.CenterHorizontally
        ) {
            Column (
                modifier = Modifier
                    .fillMaxWidth()
            ){
                SectionHeader(stringResource(id = R.string.patient).uppercase())
                Column (
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(bottomStart = 15.dp, bottomEnd = 15.dp))
                        .background(MaterialTheme.colorScheme.surfaceContainer)
                        .padding(20.dp)
                ){
                    Row {
                        Text(
                            text = stringResource(id = R.string.name) + ": ",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.ExtraBold
                            )
                        )
                        if (patientUser != null) {
                            Text(
                                text = patientUser.name,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                    Row {
                        Text(
                            text = stringResource(id = R.string.age) + ": ",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.ExtraBold
                            )
                        )
                        Text(
                            text = "$age",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            Column {
                SectionHeader(stringResource(id = R.string.medications).uppercase())
                Column (
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(bottomStart = 15.dp, bottomEnd = 15.dp))
                        .background(MaterialTheme.colorScheme.surface)
                ) {
                    MedicationList(state.medicationList)

                    MedicationForm(
                        nameMedicine = state.nameMedicine,
                        onNameMedicineChange = onNameMedicineChange,
                        descriptionMedicine = state.descriptionMedicine,
                        onDescriptionMedicineChange = onDescriptionMedicineChange,
                        isFormVisible = state.isMedicationFormVisible,
                        onToggleFormVisibility = { onEvent(NewPrescriptionEvent.ToggleMedicationFormVisibility) },
                        onConfirmMedication = { name, description ->
                            onEvent(NewPrescriptionEvent.NameMedicineChange(name))
                            onEvent(NewPrescriptionEvent.DescriptionMedicineChange(description))
                            onEvent(NewPrescriptionEvent.AddMedication(name, description))
                        }
                    )
                }

            }

            Column {
                SectionHeader(stringResource(id = R.string.notes).uppercase())
                Column (
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(bottomStart = 15.dp, bottomEnd = 15.dp))
                        .background(MaterialTheme.colorScheme.surface)
                ){
                    NoteList(state.noteList)

                    NoteForm(
                        note = state.note,
                        onNoteChange = onNoteChange,
                        isFormVisible = state.isNoteFormVisible,
                        onToggleFormVisibility = { onEvent(NewPrescriptionEvent.ToggleNoteFormVisibility) },
                        onConfirmNote = { note ->
                            onEvent(NewPrescriptionEvent.NoteChange(note))
                            onEvent(NewPrescriptionEvent.AddNote(note))
                        }
                    )
                }
            }

            if (state.isErrorCreatePrescription) {
                Box(
                    modifier = Modifier
                        .height(75.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(15.dp))
                        .background(color = MaterialTheme.colorScheme.errorContainer)
                        .padding(10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_error),
                            modifier = Modifier
                                .height(40.dp)
                                .width(40.dp),
                            contentDescription = "Error",
                            tint = MaterialTheme.colorScheme.onErrorContainer
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        Text(
                            text = stringResource(id = R.string.prescription_error),
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            if (state.isErrorCreateMedication) {
                Box(
                    modifier = Modifier
                        .height(75.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(15.dp))
                        .background(color = MaterialTheme.colorScheme.errorContainer)
                        .padding(10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_error),
                            modifier = Modifier
                                .height(40.dp)
                                .width(40.dp),
                            contentDescription = "Error",
                            tint = MaterialTheme.colorScheme.onErrorContainer
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        Text(
                            text = stringResource(id = R.string.medicine_fields_required),
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }


            CustomButton(
                text = stringResource(id = R.string.confirm_prescription),
                onClick = { onConfirmPrescription() },
                colorBackground = MaterialTheme.colorScheme.secondaryContainer,
                colorText = MaterialTheme.colorScheme.onSecondaryContainer,
                maxWidth = true
            )
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
fun MedicationList(
    medicationList: List<Medication>
){
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface),
        verticalArrangement = Arrangement.spacedBy(1.dp)
    ) {
        medicationList.forEach { item ->
            CustomListItem(
                title = item.name,
                content = item.description
            )
        }
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
                        Icons.AutoMirrored.Filled.ArrowBack,
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
fun NoteList(
    noteList: List<String>
){
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface),
        verticalArrangement = Arrangement.spacedBy(1.dp)
    ) {
        noteList.forEach { item ->
            CustomListItem(
                content = item
            )
        }
    }
}

@Composable
fun DropdownForm(
    isFormVisible: Boolean,
    onToggleFormVisibility: () -> Unit,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(vertical = 10.dp),
        Arrangement.Center,
        Alignment.CenterHorizontally
    ) {
        if (!isFormVisible) {
            CustomButton(
                text = stringResource(id = R.string.add),
                onClick = onToggleFormVisibility,
                colorBackground = MaterialTheme.colorScheme.tertiaryContainer,
                colorText = MaterialTheme.colorScheme.onTertiaryContainer,
                icon = Icons.Filled.Add,
                contentDescriptionIcon = stringResource(id = R.string.add_icon)
            )
        }

        AnimatedVisibility(
            visible = isFormVisible,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                Arrangement.Center,
                Alignment.CenterHorizontally
            ) {
                content()
            }
        }
    }
}

@Composable
fun MedicationForm(
    nameMedicine: String,
    onNameMedicineChange: (String) -> Unit,
    descriptionMedicine: String,
    onDescriptionMedicineChange: (String) -> Unit,
    isFormVisible: Boolean,
    onToggleFormVisibility: () -> Unit,
    onConfirmMedication: (String, String) -> Unit
) {
    DropdownForm(
        isFormVisible = isFormVisible,
        onToggleFormVisibility = onToggleFormVisibility
    ) {
        OutlinedTextField(
            value = nameMedicine,
            onValueChange = onNameMedicineChange,
            label = { Text(stringResource(id = R.string.name)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            shape = RoundedCornerShape(16.dp),
            singleLine = true
        )
        OutlinedTextField(
            value = descriptionMedicine,
            onValueChange = onDescriptionMedicineChange,
            label = { Text(stringResource(id = R.string.medicine_description)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            shape = RoundedCornerShape(16.dp),
            singleLine = false
        )
        Spacer(modifier = Modifier.height(10.dp))
        Row {
            CustomButton(
                text = stringResource(id = R.string.cancel),
                onClick = onToggleFormVisibility,
                colorBackground = MaterialTheme.colorScheme.errorContainer,
                colorText = MaterialTheme.colorScheme.onErrorContainer,
                icon = Icons.Filled.Close,
                contentDescriptionIcon = stringResource(id = R.string.close_icon)
            )
            Spacer(modifier = Modifier.width(10.dp))
            CustomButton(
                text = stringResource(id = R.string.confirm),
                onClick = {
                    onConfirmMedication(nameMedicine, descriptionMedicine)
                    onNameMedicineChange("")
                    onDescriptionMedicineChange("")
                    onToggleFormVisibility()
                },
                colorBackground = MaterialTheme.colorScheme.secondaryContainer,
                colorText = MaterialTheme.colorScheme.onSecondaryContainer,
                icon = Icons.Filled.Check,
                contentDescriptionIcon = stringResource(id = R.string.check_icon)
            )
        }
    }
}

@Composable
fun NoteForm(
    note: String,
    onNoteChange: (String) -> Unit,
    isFormVisible: Boolean,
    onToggleFormVisibility: () -> Unit,
    onConfirmNote: (String) -> Unit
) {
    DropdownForm(
        isFormVisible = isFormVisible,
        onToggleFormVisibility = onToggleFormVisibility
    ) {
        OutlinedTextField(
            value = note,
            onValueChange = onNoteChange,
            label = { Text(stringResource(id = R.string.new_note)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            shape = RoundedCornerShape(16.dp),
            singleLine = false
        )
        Spacer(modifier = Modifier.height(10.dp))
        Row {
            CustomButton(
                text = stringResource(id = R.string.cancel),
                onClick = onToggleFormVisibility,
                colorBackground = MaterialTheme.colorScheme.errorContainer,
                colorText = MaterialTheme.colorScheme.onErrorContainer,
                icon = Icons.Filled.Close,
                contentDescriptionIcon = stringResource(id = R.string.close_icon)
            )
            Spacer(modifier = Modifier.width(10.dp))
            CustomButton(
                text = stringResource(id = R.string.confirm),
                onClick = {
                    onConfirmNote(note)
                    onNoteChange("")
                    onToggleFormVisibility()
                },
                colorBackground = MaterialTheme.colorScheme.secondaryContainer,
                colorText = MaterialTheme.colorScheme.onSecondaryContainer,
                icon = Icons.Filled.Check,
                contentDescriptionIcon = stringResource(id = R.string.check_icon)
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewNewPrescriptionScreen() {
    DirectHealthTheme {
        Surface {
            NewPrescriptionScreen(
                state = NewPrescriptionState(
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
                    patientUser = User(
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
                    isErrorCreatePrescription = true
                ),
                onBackNavigation = {},
                onConfirmPrescription = {},
                onEvent = {},
                onNameMedicineChange = {},
                onDescriptionMedicineChange = {},
                onNoteChange = {}
            )
        }
    }
}