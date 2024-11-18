package com.uvg.directhealth.layouts.mainFlow.user.newPrescription

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable

@Serializable
data class NewPrescriptionDestination (
    val loggedUserId: String,
    val userProfileId: String
)

fun NavController.navigateToNewPrescriptionScreen (
    loggedUserId: String,
    userProfileId: String,
    navOptions: NavOptions? = null
) {
    this.navigate(
        route = NewPrescriptionDestination(loggedUserId = loggedUserId, userProfileId = userProfileId),
        navOptions = navOptions
    )
}

fun NavGraphBuilder.newPrescriptionScreen (
    onNavigateBack: () -> Unit,
) {
    composable<NewPrescriptionDestination> { backStackEntry ->
        val destination: NewPrescriptionDestination = backStackEntry.toRoute()

        NewPrescriptionRoute (
            loggedUserId = destination.loggedUserId,
            userProfileId = destination.userProfileId,
            onBackNavigation = onNavigateBack
        )
    }
}