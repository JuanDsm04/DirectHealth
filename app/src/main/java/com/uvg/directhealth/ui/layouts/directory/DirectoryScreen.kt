package com.uvg.directhealth.ui.layouts.directory

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.uvg.directhealth.R
import com.uvg.directhealth.Role
import com.uvg.directhealth.Specialty
import com.uvg.directhealth.db.AppointmentDb
import com.uvg.directhealth.db.UserDb
import com.uvg.directhealth.ui.layouts.appointmentList.CustomBottomNavigationBar
import com.uvg.directhealth.ui.layouts.login.CustomTopAppBar
import com.uvg.directhealth.ui.theme.DirectHealthTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.ui.platform.LocalSoftwareKeyboardController

@Composable
fun DirectoryScreen(idUser: String, userDb: UserDb, appointmentDb: AppointmentDb){
    val user = userDb.getUserById(idUser)
    var selectedSpecialty by remember { mutableStateOf<Specialty?>(null) }
    var searchQuery by remember { mutableStateOf("") }

    val users = if (user.role == Role.DOCTOR) {
        userDb.getPatientsByDoctorId(user.id, appointmentDb)
            .filter { it.name.contains(searchQuery, ignoreCase = true) }
    } else {
        userDb.getAllDoctors().filter { doctor ->
            (selectedSpecialty == null || doctor.doctorInfo?.specialty == selectedSpecialty) && doctor.name.contains(searchQuery, ignoreCase = true)
        }
    }

    Column (
        modifier = Modifier
            .fillMaxSize()
    ){
        CustomTopAppBar(
            onActionsClick = {/*TODO*/},
            backgroundColor = MaterialTheme.colorScheme.surface
        )

        Column (
            modifier = Modifier
                .padding(horizontal = 20.dp)
        ){
            if (user.role == Role.DOCTOR){
                WelcomeHeader(nameUser = user.name, helloMessage = stringResource(id = R.string.hello_message_doctor))
            } else {
                WelcomeHeader(nameUser = user.name, helloMessage = stringResource(id = R.string.hello_message_patient))
            }

            Spacer(modifier = Modifier.height(10.dp))
            SearchBar(
                searchQuery = searchQuery,
                onSearchQueryChanged = { searchQuery = it },
                onSearch = {}
            )

            Spacer(modifier = Modifier.height(10.dp))
            if (user.role == Role.PATIENT) {
                SpecialtySelector(
                    selectedSpecialty = selectedSpecialty,
                    onSpecialtySelected = { selected -> selectedSpecialty = selected }
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(start = 20.dp, top = 20.dp, end = 20.dp)
        ) {
            if (users.isNotEmpty()) {
                LazyColumn {
                    items(users.size) { index ->
                        UserCard(
                            nameUser = users[index].name
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            } else {
                Column (
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_person_off),
                        contentDescription = stringResource(id = R.string.person_off_icon),
                        modifier = Modifier.size(50.dp),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )

                    val emptyMessageRes = if (user.role == Role.PATIENT) {
                        R.string.empty_doctors
                    } else {
                        R.string.empty_patients
                    }

                    Text(
                        text = stringResource(id = emptyMessageRes),
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        CustomBottomNavigationBar(isDoctor = user.role == Role.DOCTOR, itemSelected = 0)
    }
}

@Composable
fun UserCard(
    nameUser: String
){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(15.dp))
            .clickable { /*TODO*/ }
            .background(MaterialTheme.colorScheme.onPrimary)
            .padding(15.dp)
    ){
        Row (
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ){
            Icon(
                imageVector = Icons.Filled.AccountCircle,
                contentDescription = stringResource(id = R.string.account_icon),
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                nameUser,
                style = MaterialTheme.typography.bodyMedium.copy()
            )
        }
    }
}

@Composable
fun WelcomeHeader(
    nameUser: String,
    helloMessage: String
){
    Box (
        modifier = Modifier
            .fillMaxWidth()
    ){
        Column {
            Text(
                text = stringResource(id = R.string.hello_again) + ", \n" + nameUser,
                style = MaterialTheme.typography.titleLarge.copy()
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = helloMessage,
                style = MaterialTheme.typography.bodyMedium.copy()
            )
        }
    }
}

@Composable
fun SpecialtySelector(
    selectedSpecialty: Specialty?,
    onSpecialtySelected: (Specialty?) -> Unit
){
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val specialtyNames = context.resources.getStringArray(R.array.specialties).toList()
    val specialties = Specialty.entries

    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column {
            Box {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp)
                        .clip(RoundedCornerShape(28.dp))
                        .clickable { expanded = true }
                        .background(MaterialTheme.colorScheme.surfaceContainer)
                        .padding(horizontal = 17.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = selectedSpecialty?.let { specialtyNames[specialties.indexOf(it)] }
                            ?: stringResource(id = R.string.search_specialty),
                        style = MaterialTheme.typography.bodyMedium.copy(),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Icon(
                        Icons.Default.ArrowDropDown,
                        contentDescription = stringResource(id = R.string.arrow_drop_down_icon),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.height(200.dp)
                ) {
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = stringResource(id = R.string.all_users)
                            )
                        },
                        onClick = {
                            onSpecialtySelected(null)
                            expanded = false
                        }
                    )

                    specialties.forEachIndexed { index, specialty ->
                        DropdownMenuItem(
                            text = { Text(specialtyNames[index]) },
                            onClick = {
                                onSpecialtySelected(specialty)
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    onSearch: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column {
            SearchBar(
                query = searchQuery,
                onQueryChange = onSearchQueryChanged,
                onSearch = {
                    onSearch()
                    keyboardController?.hide()
                },
                placeholder = { Text(
                    text = stringResource(id = R.string.search_placeholder),
                    style = MaterialTheme.typography.bodyMedium.copy(),
                    color = MaterialTheme.colorScheme.onSurface
                ) },
                active = false,
                onActiveChange = {},
                trailingIcon = {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .clickable {
                                onSearch()
                                keyboardController?.hide()
                            }
                            .padding(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = stringResource(id = R.string.search_icon),
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier
                                .fillMaxSize()
                                .align(Alignment.Center)
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(),
                colors = SearchBarDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                ),
                content = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewDoctorDirectoryScreen() {
    val userDb = UserDb()
    val appointmentDb = AppointmentDb()

    DirectHealthTheme {
        Surface {
            DirectoryScreen("7", userDb, appointmentDb)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewPatientDirectoryScreen() {
    val userDb = UserDb()
    val appointmentDb = AppointmentDb()

    DirectHealthTheme {
        Surface {
            DirectoryScreen("1", userDb, appointmentDb)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewDoctorEmptyDirectoryScreen() {
    val userDb = UserDb()
    val appointmentDb = AppointmentDb()

    DirectHealthTheme {
        Surface {
            DirectoryScreen("6", userDb, appointmentDb)
        }
    }
}