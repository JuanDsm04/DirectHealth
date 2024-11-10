package com.uvg.directhealth.layouts.mainFlow.user.directory

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data class UserDirectoryDestination(
    val userId: String
)

fun NavGraphBuilder.userDirectoryScreen(
    onUserClick: (String) -> Unit
) {
    composable<UserDirectoryDestination> {
        UserDirectoryRoute(
            onUserClick = onUserClick
        )
    }
}