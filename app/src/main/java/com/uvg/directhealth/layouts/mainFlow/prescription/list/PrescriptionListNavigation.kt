package com.uvg.directhealth.layouts.mainFlow.prescription.list

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data class PrescriptionListDestination(
    val userId: String
)

fun NavGraphBuilder.prescriptionListScreen (
    onPrescriptionClick: (String) -> Unit
) {
    composable<PrescriptionListDestination> {
        PrescriptionListRoute(
            onPrescriptionClick = onPrescriptionClick
        )
    }
}