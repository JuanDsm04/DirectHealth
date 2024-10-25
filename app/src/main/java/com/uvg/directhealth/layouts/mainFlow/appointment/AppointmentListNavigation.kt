package com.uvg.directhealth.layouts.mainFlow.appointment

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object AppointmentListDestination

fun NavGraphBuilder.appointmentListScreen(
    userId: String
) {
    composable<AppointmentListDestination> {
        AppointmentListRoute(userId = userId)
    }
}