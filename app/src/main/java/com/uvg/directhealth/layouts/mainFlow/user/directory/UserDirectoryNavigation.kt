package com.uvg.directhealth.layouts.mainFlow.user.directory

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable

@Serializable
data class UserDirectoryDestination(
    val userId: String
)

fun NavController.navigateToUserProfileScreen(
    userId: String,
    navOptions: NavOptions? = null
){

}

fun NavGraphBuilder.userDirectoryScreen(
    onUserClick: (String) -> Unit
) {
    composable<UserDirectoryDestination> {
        UserDirectoryRoute(
            id = "1",
            onUserClick = onUserClick
        )
    }
}