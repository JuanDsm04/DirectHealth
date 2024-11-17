package com.uvg.directhealth.layouts.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.uvg.directhealth.R
import com.uvg.directhealth.domain.model.Role
import com.uvg.directhealth.data.source.UserDb
import com.uvg.directhealth.layouts.mainFlow.appointment.AppointmentListDestination
import com.uvg.directhealth.layouts.mainFlow.prescription.PrescriptionNavGraph
import com.uvg.directhealth.layouts.mainFlow.prescription.list.PrescriptionListDestination
import com.uvg.directhealth.layouts.mainFlow.user.UserNavGraph
import com.uvg.directhealth.layouts.mainFlow.user.directory.UserDirectoryDestination

@Composable
fun BottomNavBar(
    checkItemSelected: (Any) -> Boolean,
    onNavItemClick: (Any) -> Unit,
    userId: String
) {
    NavigationBar (
        containerColor = MaterialTheme.colorScheme.surfaceContainer
    ){
        val isItemSelected1 = checkItemSelected(UserNavGraph)
        NavigationBarItem(
            selected = isItemSelected1,
            label = {
                Text(text = stringResource(id = R.string.nav_directory))
            },
            onClick = { onNavItemClick(UserNavGraph) },
            icon = {
                Icon(
                    painterResource(id = R.drawable.ic_groups),
                    contentDescription = stringResource(id = R.string.groups_icon)
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.onTertiaryContainer,
                indicatorColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        )
        val isItemSelected2 = checkItemSelected(PrescriptionNavGraph)
        NavigationBarItem(
            selected = isItemSelected2,
            label = { Text(text = stringResource(id = R.string.nav_prescriptions)) },
            onClick = { onNavItemClick(PrescriptionNavGraph) },
            icon = {
                Icon(
                    painterResource(id = R.drawable.ic_prescriptions),
                    contentDescription = stringResource(id = R.string.prescriptions_icon)
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.onTertiaryContainer,
                indicatorColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        )
        val isItemSelected3 = checkItemSelected(AppointmentListDestination(userId))
        NavigationBarItem(
            selected = isItemSelected3,
            label = { Text(text = stringResource(id = R.string.nav_appointments)) },
            onClick = { onNavItemClick(AppointmentListDestination(userId)) },
            icon = {
                Icon(
                    Icons.Filled.DateRange,
                    contentDescription = stringResource(id = R.string.date_icon)
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.onTertiaryContainer,
                indicatorColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        )
    }
}

val topLevelDestination = listOf(
    UserDirectoryDestination:: class,
    PrescriptionListDestination:: class,
    AppointmentListDestination:: class
)