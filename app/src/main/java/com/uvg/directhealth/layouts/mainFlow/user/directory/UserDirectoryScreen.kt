package com.uvg.directhealth.layouts.mainFlow.user.directory

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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import com.uvg.directhealth.R
import com.uvg.directhealth.data.model.Role
import com.uvg.directhealth.data.model.Specialty
import com.uvg.directhealth.data.source.UserDb
import com.uvg.directhealth.ui.theme.DirectHealthTheme
import com.uvg.directhealth.data.model.User
import com.uvg.directhealth.data.source.specialtyToStringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun UserDirectoryRoute(
    viewModel: UserDirectoryViewModel = viewModel(),
    onUserClick: (String) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    viewModel.onEvent(UserDirectoryEvent.PopulateData)

    UserDirectoryScreen(
        state = state,
        onUserClick = onUserClick
    )
}

@Composable
private fun UserDirectoryScreen(
    state: UserDirectoryState,
    onUserClick: (String) -> Unit
){
    val users = state.userList

    var selectedSpecialty by remember { mutableStateOf<Specialty?>(null) }
    var searchQuery by remember { mutableStateOf("") }

    val usersList = if ((state.userRole?: Role.PATIENT) == Role.DOCTOR) {
        users.filter { it.name.contains(searchQuery, ignoreCase = true) }
    } else {
        users.filter { doctor ->
            (selectedSpecialty == null || doctor.doctorInfo?.specialty == selectedSpecialty) &&
                    doctor.name.contains(searchQuery, ignoreCase = true)
        }
    }

    Column (
        modifier = Modifier
            .fillMaxSize()
    ){
        Column (
            modifier = Modifier
                .padding(horizontal = 20.dp)
        ){
            if (state.userRole == Role.DOCTOR){
                state.userName?.let { WelcomeHeader(nameUser = it, helloMessage = stringResource(id = R.string.hello_message_doctor)) }
            } else {
                state.userName?.let { WelcomeHeader(nameUser = it, helloMessage = stringResource(id = R.string.hello_message_patient)) }
            }

            Spacer(modifier = Modifier.height(10.dp))
            SearchBar(
                searchQuery = searchQuery,
                onSearchQueryChanged = { searchQuery = it },
                onSearch = {}
            )

            Spacer(modifier = Modifier.height(10.dp))
            if (state.userRole == Role.PATIENT) {
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
                .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                .padding(start = 20.dp, top = 20.dp, end = 20.dp)
        ) {
            if (usersList.isNotEmpty()) {
                LazyColumn {
                    items(usersList) { item ->
                        UserCard(
                            user = item,
                            onUserClick
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

                    val emptyMessageRes = if ((state.userRole?: Role.PATIENT) == Role.PATIENT) {
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
    }
}

@Composable
fun UserCard(
    user: User,
    onUserClick: (String) -> Unit
){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(15.dp))
            .clickable { onUserClick(user.id) }
            .background(MaterialTheme.colorScheme.surface)
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
                user.name,
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
) {
    var expanded by remember { mutableStateOf(false) }
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
                        text = selectedSpecialty?.let {
                            stringResource(id = specialtyToStringResource(it))
                        } ?: stringResource(id = R.string.search_specialty),
                        style = MaterialTheme.typography.bodyMedium.copy(),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Icon(
                        Icons.Default.ArrowDropDown,
                        contentDescription = stringResource(id = R.string.arrow_drop_down_icon),
                        tint = MaterialTheme.colorScheme.primary
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

                    specialties.forEach { specialty ->
                        DropdownMenuItem(
                            text = {
                                Text(stringResource(id = specialtyToStringResource(specialty)))
                            },
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
                    text = stringResource(id = R.string.search_name),
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
                            tint = MaterialTheme.colorScheme.primary,
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
@Preview(uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewPatientUserDirectoryScreen() {
    DirectHealthTheme {
        Surface {
            val userDb = UserDb()

            UserDirectoryScreen(
                state = UserDirectoryState(
                    userName = "Ana Martínez",
                    userRole = Role.PATIENT,
                    userList = userDb.getAllDoctors(),
                ),
                onUserClick = { }
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewDoctorUserDirectoryScreen() {
    DirectHealthTheme {
        Surface {
            val userDb = UserDb()

            UserDirectoryScreen(
                state = UserDirectoryState(
                    userName = "Dr. Juan Pérez",
                    userRole = Role.DOCTOR,
                    userList = userDb.getPatientsByDoctorId("1"),
                ),
                onUserClick = { }
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewDoctorUserDirectoryScreenEmpty() {
    DirectHealthTheme {
        Surface {
            val userDb = UserDb()

            UserDirectoryScreen(
                state = UserDirectoryState(
                    userName = "Dr. Sofia Torres",
                    userRole = Role.DOCTOR,
                    userList = userDb.getPatientsByDoctorId("6"),
                ),
                onUserClick = { }
            )
        }
    }
}