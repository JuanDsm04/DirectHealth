package com.uvg.directhealth.layouts.mainFlow

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable

@Serializable
data class MainNavigationGraph (
    val userId: String
)

fun NavController.navigateToMainGraph(
    navOptions: NavOptions? = null,
    userId: String
) {
    this.navigate(
        MainNavigationGraph(userId = userId),
        navOptions,
    )
}

fun NavGraphBuilder.mainNavigationGraph(
    onLogOutClick: () -> Unit
) {
    composable<MainNavigationGraph> { backStackEntry ->
        val nestedNavController = rememberNavController()
        val destination: MainNavigationGraph = backStackEntry.toRoute()

        MainFlowScreen(
            navController = nestedNavController,
            userId = destination.userId,
            onLogOutClick = onLogOutClick
        )
    }
}