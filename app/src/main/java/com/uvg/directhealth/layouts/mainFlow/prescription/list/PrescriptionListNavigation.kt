package com.uvg.directhealth.layouts.mainFlow.prescription.list

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable

@Serializable
data class PrescriptionListDestination(
    val userId: String
)

fun NavController.navigateToPrescriptionDetailsScreen (
    userId: String,
    navOptions: NavOptions? = null
) {

}

fun NavGraphBuilder.prescriptionListScreen (
    onPrescriptionClick: (String) -> Unit
) {
    composable<PrescriptionListDestination> { backStackEntry ->
        val destination: PrescriptionListDestination = backStackEntry.toRoute()

        PrescriptionListRoute(
            id = "1",
            onPrescriptionClick = onPrescriptionClick
        )
    }
}