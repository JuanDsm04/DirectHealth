package com.uvg.directhealth.layouts.roleRegister

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.uvg.directhealth.data.model.Role
import kotlinx.serialization.Serializable

@Serializable
data object RoleRegisterDestination

fun NavGraphBuilder.roleRegisterScreen(
    onNavigateBack: () -> Unit,
    onRoleRegisterClick: (Role) -> Unit
) {
    composable<RoleRegisterDestination> {
        RoleRegisterRoute(
            onNavigateBack = onNavigateBack,
            onRoleRegisterClick = onRoleRegisterClick
        )
    }
}