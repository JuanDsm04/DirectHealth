package com.uvg.directhealth.layouts.mainFlow.prescription.list

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable

@Serializable
data class PrescriptionListDestination(
    val userId: String
)

fun NavGraphBuilder.prescriptionListScreen (
    onPrescriptionClick: (String) -> Unit
) {
    composable<PrescriptionListDestination> { backStackEntry ->
        val destination: PrescriptionListDestination = backStackEntry.toRoute()

        PrescriptionListRoute(
            userId = destination.userId,
            onPrescriptionClick = onPrescriptionClick
        )
    }
}