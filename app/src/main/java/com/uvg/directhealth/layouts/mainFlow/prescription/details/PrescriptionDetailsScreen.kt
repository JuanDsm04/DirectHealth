package com.uvg.directhealth.layouts.mainFlow.prescription.details

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.uvg.directhealth.R
import com.uvg.directhealth.data.model.Medication
import com.uvg.directhealth.data.model.Prescription
import com.uvg.directhealth.data.source.PrescriptionDb
import com.uvg.directhealth.data.source.UserDb
import com.uvg.directhealth.ui.theme.DirectHealthTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun PrescriptionDetailsRoute(
    id: String,
    onNavigateBack: () -> Unit
) {
    val prescriptionDb = PrescriptionDb()
    val prescription = prescriptionDb.getPrescriptionById(id)
    PrescriptionDetailsScreen(
        prescription = prescription,
        onNavigateBack = onNavigateBack
    )
}

@Composable
private fun PrescriptionDetailsScreen(
    prescription: Prescription,
    onNavigateBack: () -> Unit
){
    val userDb = UserDb()
    val user = userDb.getUserById(prescription.patientId)
    val age = LocalDate.now().year - user.birthDate.year

    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .verticalScroll(rememberScrollState())
    ){
        CustomLargeTopAppBar(
            prescription.id,
            prescription.emissionDate
        )

        Column (
            modifier = Modifier
                .padding(horizontal = 15.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ){
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

            SectionWithItems(
                title = stringResource(id = R.string.medications),
                items = prescription.medicationList
            ) { medicine ->
                CustomListItem(
                    title = medicine.name,
                    content = medicine.description
                )
            }

            SectionWithItems(
                title = stringResource(id = R.string.notes),
                items = prescription.notes
            ) { note ->
                CustomListItem(
                    content = note
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
fun SectionHeader(
    title: String
){
    Box (
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp))
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(20.dp)
    ){
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        )
    }
}

@Composable
fun <T> SectionWithItems(
    title: String,
    items: List<T>,
    itemContent: @Composable (T) -> Unit
) {
    Column (
        modifier = Modifier
            .fillMaxWidth()
    ){
        SectionHeader(title.uppercase())
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomStart = 15.dp, bottomEnd = 15.dp))
                .background(MaterialTheme.colorScheme.surface)
        ) {
            if(items.isEmpty()){
                Column (
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceContainer),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Text(
                        stringResource(id = R.string.empty_list) + " " + title.lowercase(),
                        style = MaterialTheme.typography.bodyMedium.copy(),
                        modifier = Modifier
                            .padding(15.dp)
                    )
                }

            } else {
                Column(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surface),
                    verticalArrangement = Arrangement.spacedBy(1.dp)
                ) {
                    items.forEach { item ->
                        itemContent(item)
                    }
                }
            }
        }
    }

}

@Composable
fun CustomListItem(
    title: String? = null,
    content: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(20.dp)
    ) {
        Column {
            title?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }
            Text(
                text = content,
                style = MaterialTheme.typography.bodyMedium.copy()
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomLargeTopAppBar(
    prescriptionId: String,
    prescriptionEmissionDate: LocalDate
){
    val dateFormatter = remember { DateTimeFormatter.ofPattern("dd/MM/yyyy") }
    LargeTopAppBar(
        title = {
            Column {
                Text(
                    text = stringResource(id = R.string.prescription_id) + prescriptionId,
                )
                Text(
                    text = stringResource(id = R.string.emissionDate) + ": " + prescriptionEmissionDate.format(dateFormatter),
                    style = MaterialTheme.typography.bodyLarge.copy()
                )
            }
        },
        navigationIcon = {
            IconButton({}) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = stringResource(id = R.string.back_icon)
                )
            }
        },
        actions = {
            IconButton({/**/}) {
                Icon(
                    Icons.Default.Settings,
                    contentDescription = stringResource(id = R.string.settings_icon)
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Preview(uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewPrescriptionDetailsScreen() {
    DirectHealthTheme {
        Surface {
            PrescriptionDetailsScreen(
                prescription = Prescription(
                    id = "1",
                    doctorId = "1",
                    patientId = "2",
                    emissionDate = LocalDate.of(2024, 1, 12),
                    medicationList = listOf(
                        Medication(
                            name = "Aspirina",
                            description = "Para aliviar el dolor y reducir la inflamación."
                        ),
                        Medication(
                            name = "Atorvastatina",
                            description = "Ayuda a reducir los niveles de colesterol."
                        )
                    ),
                    notes = listOf(
                        "Tome aspirina una vez al día después de las comidas.",
                        "Controlar los niveles de colesterol cada 3 meses."
                    )
                ),
                onNavigateBack = { }
            )
        }
    }
}

@Preview(showBackground = true)
@Preview (uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewPrescriptionDetailsScreenNotesEmpty() {
    DirectHealthTheme {
        Surface {
            PrescriptionDetailsScreen(
                prescription = Prescription(
                    id = "2",
                    doctorId = "3",
                    patientId = "7",
                    emissionDate = LocalDate.of(2024, 8, 27),
                    medicationList = listOf(
                        Medication(
                            name = "Hidrocortisona",
                            description = "Se utiliza para reducir la inflamación y tratar afecciones de la piel."
                        )
                    ),
                    notes = listOf()
                ),
                onNavigateBack = { }
            )
        }
    }
}