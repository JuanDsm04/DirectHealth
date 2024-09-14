package com.uvg.directhealth.ui.layouts.prescription

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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.uvg.directhealth.R
import com.uvg.directhealth.db.PrescriptionDb
import com.uvg.directhealth.db.UserDb
import com.uvg.directhealth.ui.theme.DirectHealthTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun PrescriptionScreen(prescriptionId: String, prescriptionDb: PrescriptionDb, userDb: UserDb){
    val prescription = prescriptionDb.getPrescriptionById(prescriptionId)
    val user = userDb.getUserById(prescription.patientId)
    val age = LocalDate.now().year - user.birthDate.year

    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onPrimary)
            .verticalScroll(rememberScrollState())
    ){
        PersonalizedLargeTopAppBar(prescription.id, prescription.emissionDate)

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
                Box (
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(20.dp)
                ){
                    Column {
                        Row {
                            Text(
                                text = stringResource(id = R.string.name) + ": ",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.ExtraBold
                                )
                            )
                            Text(text = user.name)
                        }
                        Row {
                            Text(
                                text = stringResource(id = R.string.age) + ": ",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.ExtraBold
                                )
                            )
                            Text(text = "$age")
                        }
                    }
                }
            }

            SectionWithItems(
                title = stringResource(id = R.string.medications),
                items = prescription.medicationList
            ) { medicine ->
                SectionItem(
                    title = medicine.name,
                    content = medicine.description
                )
            }

            SectionWithItems(
                title = stringResource(id = R.string.notes),
                items = prescription.notes
            ) { note ->
                SectionItem(
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
                fontWeight = FontWeight.ExtraBold
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
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column {
            SectionHeader(title.uppercase())
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                if(items.isEmpty()){
                    Column (
                        modifier = Modifier
                            .fillMaxWidth(),
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
                            .background(MaterialTheme.colorScheme.surfaceContainer),
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
}

@Composable
fun SectionItem(
    title: String? = null,
    content: String,
    titleStyle: TextStyle = MaterialTheme.typography.titleMedium.copy(
        fontWeight = FontWeight.SemiBold
    ),
    contentStyle: TextStyle = MaterialTheme.typography.bodyMedium.copy()
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(20.dp)
    ) {
        Column {
            title?.let {
                Text(
                    text = it,
                    style = titleStyle
                )
            }
            Text(
                text = content,
                style = contentStyle
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalizedLargeTopAppBar(prescriptionId: String, prescriptionEmissionDate: LocalDate){
    val dateFormatter = remember { DateTimeFormatter.ofPattern("dd/MM/yyyy") }
    LargeTopAppBar(
        title = {
            Column {
                Text(
                    text = stringResource(id = R.string.prescription_id) + prescriptionId,
                )
                Text(
                    text = stringResource(id = R.string.prescription_emissionDate) + ": " + prescriptionEmissionDate.format(dateFormatter),
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
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewPrescriptionScreen() {
    val prescriptionDb = PrescriptionDb()
    val userDb = UserDb()

    DirectHealthTheme {
        Surface {
            PrescriptionScreen("1", prescriptionDb, userDb)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewPrescriptionNotesEmptyScreen() {
    val prescriptionDb = PrescriptionDb()
    val userDb = UserDb()

    DirectHealthTheme {
        Surface {
            PrescriptionScreen("2", prescriptionDb, userDb)
        }
    }
}