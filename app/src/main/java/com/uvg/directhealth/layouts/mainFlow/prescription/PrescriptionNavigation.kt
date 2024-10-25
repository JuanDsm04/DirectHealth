package com.uvg.directhealth.layouts.mainFlow.prescription

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.uvg.directhealth.layouts.mainFlow.prescription.details.navigateToPrescriptionDetailsScreen
import com.uvg.directhealth.layouts.mainFlow.prescription.details.prescriptionDetailsScreen
import com.uvg.directhealth.layouts.mainFlow.prescription.list.PrescriptionListDestination
import com.uvg.directhealth.layouts.mainFlow.prescription.list.prescriptionListScreen
import kotlinx.serialization.Serializable

@Serializable
data object PrescriptionNavGraph

fun NavGraphBuilder.prescriptionGraph(
    navController: NavController,
    userId: String
) {
    navigation<PrescriptionNavGraph> (
        startDestination = PrescriptionListDestination(userId =userId)
    ) {
        prescriptionListScreen(
            onPrescriptionClick = navController::navigateToPrescriptionDetailsScreen
        )
        prescriptionDetailsScreen(
            onNavigateBack = navController::navigateUp
        )
    }
}