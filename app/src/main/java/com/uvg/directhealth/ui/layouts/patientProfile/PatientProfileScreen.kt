package com.uvg.directhealth.ui.layouts.patientProfile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.uvg.directhealth.db.UserDb
import com.uvg.directhealth.ui.layouts.prescription.SectionHeader
import com.uvg.directhealth.ui.theme.DirectHealthTheme
import java.time.LocalDate
import com.uvg.directhealth.ui.layouts.welcome.CustomButton
import com.uvg.directhealth.ui.layouts.login.CustomTopAppBar

@Composable
fun PatientProfileScreen(idUser: String, userDb: UserDb){
    val user = userDb.getUserById(idUser)
    val age = LocalDate.now().year - user.birthDate.year

    Column (
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ){
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
        ){
            ProfileHeader(name = user.name)

            Box{
                Column {
                    SectionHeader(stringResource(id = R.string.general_data))
                    Box (
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(bottomStart = 15.dp, bottomEnd = 15.dp))
                            .background(MaterialTheme.colorScheme.onPrimary)
                            .padding(20.dp)
                    ){
                        Column {
                            Row {
                                Text(
                                    text = stringResource(id = R.string.phone_number) + ": ",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                )
                                Text(user.phoneNumber)
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
            }

            Box {
                Column {
                    SectionHeader(stringResource(id = R.string.medical_history))
                    Box (
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(bottomStart = 15.dp, bottomEnd = 15.dp))
                            .background(MaterialTheme.colorScheme.onPrimary)
                            .padding(20.dp)
                    ){
                        user.patientInfo?.let { Text(it.medicalHistory) }
                    }
                }
            }

            CustomButton(
                text = stringResource(id = R.string.create_prescription),
                onClick = {/**/},
                colorBackground = MaterialTheme.colorScheme.secondaryContainer,
                colorText = MaterialTheme.colorScheme.onSecondaryContainer,
                icon = Icons.Filled.Create,
                contentDescriptionIcon = stringResource(id = R.string.create_prescription_icon),
                cornerRadius = 100.dp
            )

            Spacer(modifier = Modifier.width(10.dp))
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

@Preview(showBackground = true)
@Composable
private fun PreviewPatientProfileScreen() {
    val userDb = UserDb()

    DirectHealthTheme {
        Surface {
            PatientProfileScreen("7", userDb)
        }
    }
}
