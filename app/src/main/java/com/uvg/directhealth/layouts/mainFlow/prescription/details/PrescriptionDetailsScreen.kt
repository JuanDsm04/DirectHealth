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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.uvg.directhealth.R
import com.uvg.directhealth.data.model.Medication
import com.uvg.directhealth.data.model.Prescription
import com.uvg.directhealth.ui.theme.DirectHealthTheme
import com.uvg.directhealth.layouts.common.SectionHeader
import com.uvg.directhealth.layouts.common.CustomListItem
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import androidx.lifecycle.viewmodel.compose.viewModel
import com.uvg.directhealth.layouts.common.CustomTopAppBar
import com.uvg.directhealth.layouts.common.HasError
import com.uvg.directhealth.layouts.common.IsLoading

@Composable
fun PrescriptionDetailsRoute(
    prescriptionId: String,
    onNavigateBack: () -> Unit,
    viewModel: PrescriptionDetailsViewModel = viewModel(factory = PrescriptionDetailsViewModel.Factory)
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.onEvent(PrescriptionDetailsEvent.PopulateData(prescriptionId))
    }

    PrescriptionDetailsScreen(
        state = state,
        onNavigateBack = onNavigateBack
    )
}

@Composable
private fun PrescriptionDetailsScreen(
    state: PrescriptionDetailsState,
    onNavigateBack: () -> Unit
) {
    val prescription = state.prescription

    Column {
        CustomTopAppBar(
            onNavigationClick = { onNavigateBack() },
            backgroundColor = MaterialTheme.colorScheme.background
        )
        when {
            state.isLoading -> IsLoading()
            state.hasError -> HasError()
            else ->
                Details(
                prescription = prescription,
                state = state
            )
        }
    }
}

@Composable
fun Details(
    prescription: Prescription?,
    state: PrescriptionDetailsState
){
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .verticalScroll(rememberScrollState())
    ){
        Column (
            modifier = Modifier
                .padding(horizontal = 15.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ){
            Column (
                modifier = Modifier
                    .fillMaxWidth()
            ){
                val dateFormatter = remember { DateTimeFormatter.ofPattern("dd/MM/yyyy") }

                Spacer(modifier = Modifier.height(10.dp))
                Column {
                    Text(
                        text = stringResource(id = R.string.emissionDate) + ": " + (state.prescription?.emissionDate?.format(dateFormatter)
                            ?: ""),
                        style = MaterialTheme.typography.bodyLarge.copy()
                    )
                    Text(
                        text = stringResource(id = R.string.prescription_id) + (state.prescription?.id
                            ?: ""),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

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
                            text = state.patientName,
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
                            text = state.patientAge.toString(),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            if (prescription != null) {
                SectionWithItems(
                    title = stringResource(id = R.string.medications),
                    items = prescription.medicationList
                ) { medicine: Medication ->
                    CustomListItem(
                        title = medicine.name,
                        content = medicine.description
                    )
                }

                SectionWithItems(
                    title = stringResource(id = R.string.notes),
                    items = prescription.notes ?: emptyList()
                ) { note: String ->
                    CustomListItem(
                        content = note
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
        }
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
            if (items.isEmpty()){
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

@Preview(showBackground = true)
@Preview(uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewPrescriptionDetailsScreen() {
    DirectHealthTheme {
        Surface {
            PrescriptionDetailsScreen(
                state = PrescriptionDetailsState(
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
                    isLoading = false
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
                state = PrescriptionDetailsState(
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
                    isLoading = false
                ),
                onNavigateBack = { }
            )
        }
    }
}