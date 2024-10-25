package com.uvg.directhealth.layouts.mainFlow.user

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.uvg.directhealth.layouts.mainFlow.user.directory.UserDirectoryDestination
import com.uvg.directhealth.layouts.mainFlow.user.directory.userDirectoryScreen
import com.uvg.directhealth.layouts.mainFlow.user.newPrescription.navigateToNewPrescriptionScreen
import com.uvg.directhealth.layouts.mainFlow.user.newPrescription.newPrescriptionScreen
import com.uvg.directhealth.layouts.mainFlow.user.profile.navigateToUserProfileScreen
import com.uvg.directhealth.layouts.mainFlow.user.profile.userProfileScreen
import kotlinx.serialization.Serializable

@Serializable
data object UserNavGraph

fun NavGraphBuilder.userGraph (
    navController: NavController,
    userId: String
) {
    navigation<UserNavGraph> (
        startDestination = UserDirectoryDestination(userId = userId)
    ) {
        userDirectoryScreen(
            onUserClick = { userProfileId ->
                navController.navigateToUserProfileScreen(loggedUserId = userId, userProfileId = userProfileId)
            }
        )
        userProfileScreen(
            createNewPrescription = { userProfileId ->
                navController.navigateToNewPrescriptionScreen(loggedUserId = userId, userProfileId = userProfileId)
            },
            onNavigateBack = navController::navigateUp
        )
        newPrescriptionScreen (
            onNavigateBack = navController::navigateUp
        )
    }
}