package com.uvg.directhealth.layouts.mainFlow.prescription.details

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable

@Serializable
data class PrescriptionDetailsDestination (
    val prescriptionId: String
)

fun NavController.navigateToPrescriptionDetailsScreen (
    prescriptionId: String,
    navOptions: NavOptions? = null
) {
    this.navigate(
        route = PrescriptionDetailsDestination(prescriptionId = prescriptionId),
        navOptions = navOptions
    )
}

fun NavGraphBuilder.prescriptionDetailsScreen (
    onNavigateBack: () -> Unit
) {
    composable<PrescriptionDetailsDestination> { backStackEntry ->
        val destination: PrescriptionDetailsDestination = backStackEntry.toRoute()
        PrescriptionDetailsRoute(
            id = destination.prescriptionId,
            onNavigateBack = onNavigateBack
        )
    }
}