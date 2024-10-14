package com.uvg.directhealth.layouts.welcome

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object WelcomeDestination

fun NavGraphBuilder.welcomeScreen (
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit
) {
    composable<WelcomeDestination>{
        WelcomeRoute(
            onLoginClick = onLoginClick,
            onRegisterClick = onRegisterClick
        )
    }
}