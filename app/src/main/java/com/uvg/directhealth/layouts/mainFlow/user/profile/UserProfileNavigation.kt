package com.uvg.directhealth.layouts.mainFlow.user.profile

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable

@Serializable
data class UserProfileDestination (
    val loggedUserId: String,
    val userProfileId: String
)

fun NavController.navigateToUserProfileScreen (
    loggedUserId: String,
    userProfileId: String,
    navOptions: NavOptions? = null
) {
    this.navigate(
        route = UserProfileDestination(loggedUserId = loggedUserId, userProfileId = userProfileId),
        navOptions = navOptions
    )
}

fun NavGraphBuilder.userProfileScreen (
    createNewPrescription: (String) -> Unit,
    onNavigateBack: () -> Unit
) {
    composable<UserProfileDestination> { backStackEntry ->
        val destination: UserProfileDestination = backStackEntry.toRoute()

        UserProfileRoute(
            loggedUserId = destination.loggedUserId,
            userProfileId = destination.userProfileId,
            createNewPrescription = createNewPrescription,
            onNavigateBack = onNavigateBack
        )
    }
}