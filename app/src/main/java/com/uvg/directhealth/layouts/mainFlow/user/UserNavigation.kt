package com.uvg.directhealth.layouts.mainFlow.user

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.uvg.directhealth.layouts.mainFlow.user.directory.UserDirectoryDestination
import com.uvg.directhealth.layouts.mainFlow.user.directory.navigateToUserProfileScreen
import com.uvg.directhealth.layouts.mainFlow.user.directory.userDirectoryScreen
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
            onUserClick = navController::navigateToUserProfileScreen
        )
    }
}