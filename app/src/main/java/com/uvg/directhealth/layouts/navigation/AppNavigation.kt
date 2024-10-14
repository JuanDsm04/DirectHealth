package com.uvg.directhealth.layouts.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.uvg.directhealth.layouts.login.LoginDestination
import com.uvg.directhealth.layouts.login.loginScreen
import com.uvg.directhealth.layouts.register.navigateToRegisterScreen
import com.uvg.directhealth.layouts.register.registerScreen
import com.uvg.directhealth.layouts.roleRegister.RoleRegisterDestination
import com.uvg.directhealth.layouts.roleRegister.roleRegisterScreen
import com.uvg.directhealth.layouts.welcome.WelcomeDestination
import com.uvg.directhealth.layouts.welcome.welcomeScreen

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
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
            onLogIn = {},
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
            onRoleRegisterClick = { role ->
                navController.navigateToRegisterScreen(role = role)
            }
        )
        registerScreen(
            onBackNavigation = {
                navController.navigateUp()
            },
            onConfirmRegistration = {}
        )
    }
}