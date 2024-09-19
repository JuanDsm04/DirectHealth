package com.uvg.directhealth.ui.layouts.newPrescription

import Prescription
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
import com.uvg.directhealth.R
import com.uvg.directhealth.db.PrescriptionDb
import com.uvg.directhealth.db.UserDb
import com.uvg.directhealth.ui.layouts.appointmentList.CustomMediumTopAppBar
import com.uvg.directhealth.ui.layouts.prescription.SectionHeader
import com.uvg.directhealth.ui.layouts.welcome.CustomButton
import com.uvg.directhealth.ui.theme.DirectHealthTheme
import java.time.LocalDate
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.ui.draw.clip
import com.uvg.directhealth.ui.layouts.prescription.CustomListItem

@Composable
fun NewPrescription(prescriptionId: String, prescriptionDb: PrescriptionDb, userDb: UserDb){
    val prescription = prescriptionDb.getPrescriptionById(prescriptionId)
    val user = userDb.getUserById(prescription.patientId)
    val age = LocalDate.now().year - user.birthDate.year
    var nameMedicine by remember { mutableStateOf("") }
    var descriptionMedicine by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .verticalScroll(rememberScrollState())
    ){
        CustomMediumTopAppBar(
            title = stringResource(id = R.string.new_prescription),
            onNavigationClick = {/**/},
            onActionsClick = {/**/},
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
                        Text(
                            text = user.name,
                            style = MaterialTheme.typography.bodyMedium
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
                    MedicationList(prescription = prescription)

                    Spacer(modifier = Modifier.height(1.dp))

                    MedicationForm(
                        nameMedicine = nameMedicine,
                        onNameMedicineChange = { nameMedicine = it },
                        descriptionMedicine = descriptionMedicine,
                        onDescriptionMedicineChange = { descriptionMedicine = it }
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
                    NoteList(prescription = prescription)

                    Spacer(modifier = Modifier.height(1.dp))

                    NoteForm(
                        note = note,
                        onNoteChange = { note = it }
                    )
                }
            }

            CustomButton(
                text = stringResource(id = R.string.confirm_prescription),
                onClick = { /* */ },
                colorBackground = MaterialTheme.colorScheme.secondaryContainer,
                colorText = MaterialTheme.colorScheme.onSecondaryContainer,
                maxWidth = true
            )
            Spacer(modifier = Modifier.height(5.dp))
        }
    }
}

@Composable
fun MedicationList(
    prescription: Prescription
){
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface),
        verticalArrangement = Arrangement.spacedBy(1.dp)
    ) {
        prescription.medicationList.forEach { item ->
            CustomListItem(
                title = item.name,
                content = item.description
            )
        }
    }
}

@Composable
fun NoteList(
    prescription: Prescription
){
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface),
        verticalArrangement = Arrangement.spacedBy(1.dp)
    ) {
        prescription.notes.forEach { item ->
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
                Spacer(modifier = Modifier.height(5.dp))
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
                        onClick = { /* */ },
                        colorBackground = MaterialTheme.colorScheme.secondaryContainer,
                        colorText = MaterialTheme.colorScheme.onSecondaryContainer,
                        icon = Icons.Filled.Check,
                        contentDescriptionIcon = stringResource(id = R.string.check_icon)
                    )
                }
            }
        }
    }
}

@Composable
fun MedicationForm(
    nameMedicine: String,
    onNameMedicineChange: (String) -> Unit,
    descriptionMedicine: String,
    onDescriptionMedicineChange: (String) -> Unit
) {
    var isFormVisible by remember { mutableStateOf(false) }

    DropdownForm(
        isFormVisible = isFormVisible,
        onToggleFormVisibility = { isFormVisible = !isFormVisible }
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
    }
}

@Composable
fun NoteForm(
    note: String,
    onNoteChange: (String) -> Unit
) {
    var isFormVisible by remember { mutableStateOf(false) }

    DropdownForm(
        isFormVisible = isFormVisible,
        onToggleFormVisibility = { isFormVisible = !isFormVisible }
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
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewNewPrescriptionScreen() {
    val prescriptionDb = PrescriptionDb()
    val userDb = UserDb()

    DirectHealthTheme {
        Surface {
            NewPrescription("1", prescriptionDb, userDb)
        }
    }
}

@Preview(
    showBackground = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun PreviewNewPrescriptionScreenDark() {
    val prescriptionDb = PrescriptionDb()
    val userDb = UserDb()

    DirectHealthTheme {
        Surface {
            NewPrescription("1", prescriptionDb, userDb)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewNewPrescriptionEmptyScreen() {
    val prescriptionDb = PrescriptionDb()
    val userDb = UserDb()

    DirectHealthTheme {
        Surface {
            NewPrescription("3", prescriptionDb, userDb)
        }
    }
}

@Preview(
    showBackground = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun PreviewNewPrescriptionEmptyScreenDark() {
    val prescriptionDb = PrescriptionDb()
    val userDb = UserDb()

    DirectHealthTheme {
        Surface {
            NewPrescription("3", prescriptionDb, userDb)
        }
    }
}