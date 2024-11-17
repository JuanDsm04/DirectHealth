package com.uvg.directhealth.layouts.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.uvg.directhealth.data.local.DataStoreUserPrefs
import com.uvg.directhealth.layouts.login.LoginDestination
import com.uvg.directhealth.layouts.login.loginScreen
import com.uvg.directhealth.layouts.mainFlow.mainNavigationGraph
import com.uvg.directhealth.layouts.mainFlow.navigateToMainGraph
import com.uvg.directhealth.layouts.register.RegisterDestination
import com.uvg.directhealth.layouts.register.navigateToRegisterScreen
import com.uvg.directhealth.layouts.register.registerScreen
import com.uvg.directhealth.layouts.roleRegister.RoleRegisterDestination
import com.uvg.directhealth.layouts.roleRegister.roleRegisterScreen
import com.uvg.directhealth.layouts.welcome.WelcomeDestination
import com.uvg.directhealth.layouts.welcome.welcomeScreen
import kotlinx.coroutines.launch

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    dataStoreUserPrefs: DataStoreUserPrefs
) {
    val scope = rememberCoroutineScope()

    NavHost(
        navController = navController,
        startDestination = WelcomeDestination,
        modifier = modifier
    ) {
        welcomeScreen(
            onLoginClick = {
                navController.navigate(LoginDestination)
            },
            onRegisterClick = {
                navController.navigate(RoleRegisterDestination)
            }
        )
        loginScreen(
            onLogIn = {
                scope.launch {
                    val userId = dataStoreUserPrefs.getValue("userId")
                    userId?.let {
                        navController.navigateToMainGraph(
                            navOptions = NavOptions.Builder().setPopUpTo<LoginDestination>(inclusive = true).build(),
                            userId = it
                        )
                    }
                }
            },
            onRegister = {
                navController.navigate(RoleRegisterDestination)
            },
            onNavigateBack = {
                navController.navigateUp()
            }
        )
        roleRegisterScreen(
            onNavigateBack = {
                navController.navigateUp()
            },
            onRoleRegisterClick = navController::navigateToRegisterScreen
        )
        registerScreen(
            onBackNavigation = {
                navController.navigateUp()
            },
            onConfirmRegistration = {
                navController.navigate(LoginDestination) {
                    popUpTo(0)
                }
            }
        )
        mainNavigationGraph(
            onLogOutClick = {
                navController.navigate(LoginDestination) {
                    popUpTo(0)
                }
            }
        )
    }
}