package com.uvg.directhealth.layouts.mainFlow.appointment

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data class AppointmentListDestination(
    val userId: String
)

fun NavGraphBuilder.appointmentListScreen() {
    composable<AppointmentListDestination> {
        AppointmentListRoute()
    }
}