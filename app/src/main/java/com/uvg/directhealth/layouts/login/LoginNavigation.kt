package com.uvg.directhealth.layouts.login

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object LoginDestination

fun NavGraphBuilder.loginScreen (
    onLogIn: () -> Unit,
    onRegister: () -> Unit,
    onNavigateBack: () -> Unit
) {
    composable<LoginDestination> {
        LoginRoute(
            onLogIn = onLogIn,
            onRegister = onRegister,
            onNavigateBack = onNavigateBack
        )
    }
}