package com.uvg.directhealth.layouts.mainFlow

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.uvg.directhealth.layouts.mainFlow.appointment.appointmentListScreen
import com.uvg.directhealth.layouts.navigation.BottomNavBar
import com.uvg.directhealth.layouts.navigation.topLevelDestination
import com.uvg.directhealth.layouts.mainFlow.prescription.prescriptionGraph
import com.uvg.directhealth.layouts.mainFlow.profile.navigateToProfile
import com.uvg.directhealth.layouts.mainFlow.profile.profileScreen
import com.uvg.directhealth.layouts.mainFlow.user.UserNavGraph
import com.uvg.directhealth.layouts.mainFlow.user.userGraph
import com.uvg.directhealth.layouts.navigation.CustomTopAppBar

@Composable
fun MainFlowScreen (
    onLogOutClick: () -> Unit,
    navController: NavHostController = rememberNavController(),
    userId: String
) {
    var topAppBarVisible by rememberSaveable {
        mutableStateOf(false)
    }

    var bottomBarVisible by rememberSaveable {
        mutableStateOf(false)
    }

    val currentDestination = navController.currentBackStackEntryAsState().value?.destination
    bottomBarVisible = if (currentDestination != null ){
        topLevelDestination.any {destination ->
            currentDestination.hasRoute(destination)
        }
    } else {
        false
    }

    topAppBarVisible = if (currentDestination != null ){
        topLevelDestination.any {destination ->
            currentDestination.hasRoute(destination)
        }
    } else {
        false
    }

    Scaffold(
        topBar = {
            AnimatedVisibility(visible = topAppBarVisible) {
                CustomTopAppBar(onActionsClick = { navController.navigateToProfile(userId = userId) })
            }
        },
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            AnimatedVisibility(
                visible = bottomBarVisible,
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it }),
            ) {
                BottomNavBar(
                    checkItemSelected = { destination ->
                        currentDestination?.hierarchy?.any { it.hasRoute(destination::class) } ?: false
                    },
                    onNavItemClick = { destination ->
                        navController.navigate(destination) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    userId = userId
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = UserNavGraph,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            userGraph(navController, userId)
            prescriptionGraph(navController, userId)
            appointmentListScreen()
            profileScreen(
                onNavigateBack = { navController.navigateUp() },
                onLogOut = onLogOutClick
            )
        }
    }
}