package com.uvg.directhealth.layouts.mainFlow.profile

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable

@Serializable
data class ProfileDestination (
    val userId: String,
)

fun NavController.navigateToProfile (
    userId: String
) {
    this.navigate(
        route = ProfileDestination(userId = userId)
    )
}

fun NavGraphBuilder.profileScreen (
    onLogOut: () -> Unit,
    onNavigateBack: () -> Unit
) {
    composable<ProfileDestination> { backStackEntry ->
        val destination: ProfileDestination = backStackEntry.toRoute()

        ProfileRoute(
            userId = destination.userId,
            onLogOut = onLogOut,
            onNavigateBack = onNavigateBack
        )
    }
}