package com.uvg.directhealth.layouts.register

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.uvg.directhealth.domain.model.Role
import kotlinx.serialization.Serializable

@Serializable
data class RegisterDestination (
    val roleUser: Role
)

fun NavController.navigateToRegisterScreen(role: Role, navOptions: NavOptions? = null) {
    this.navigate(route = RegisterDestination(roleUser = role), navOptions = navOptions)
}

fun NavGraphBuilder.registerScreen(
    onBackNavigation: () -> Unit,
    onConfirmRegistration: () -> Unit
) {
    composable<RegisterDestination> { backStackEntry ->
        val destination: RegisterDestination = backStackEntry.toRoute()
        RegisterRoute(
            role = destination.roleUser,
            onBackNavigation = onBackNavigation,
            onConfirmRegistration = onConfirmRegistration
        )
    }
}