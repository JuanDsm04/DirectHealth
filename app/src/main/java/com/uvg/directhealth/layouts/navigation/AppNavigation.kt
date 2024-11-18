package com.uvg.directhealth.layouts.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.uvg.directhealth.data.local.DataStoreUserPrefs
import com.uvg.directhealth.layouts.login.LoginDestination
import com.uvg.directhealth.layouts.login.loginScreen
import com.uvg.directhealth.layouts.mainFlow.mainNavigationGraph
import com.uvg.directhealth.layouts.mainFlow.navigateToMainGraph
import com.uvg.directhealth.layouts.navigation.authentication.AuthState
import com.uvg.directhealth.layouts.navigation.authentication.AuthViewModel
import com.uvg.directhealth.layouts.register.RegisterDestination
import com.uvg.directhealth.layouts.register.navigateToRegisterScreen
import com.uvg.directhealth.layouts.register.registerScreen
import com.uvg.directhealth.layouts.roleRegister.RoleRegisterDestination
import com.uvg.directhealth.layouts.roleRegister.roleRegisterScreen
import com.uvg.directhealth.layouts.welcome.WelcomeDestination
import com.uvg.directhealth.layouts.welcome.welcomeScreen
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
data object SplashDestination

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    dataStoreUserPrefs: DataStoreUserPrefs,
    viewModel: AuthViewModel = viewModel(factory = AuthViewModel.Factory)
) {
    val scope = rememberCoroutineScope()
    val status by viewModel.authStatus.collectAsStateWithLifecycle()

    NavHost(
        navController = navController,
        startDestination = SplashDestination,
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
                    viewModel.login()
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
                viewModel.logout()
                navController.navigate(LoginDestination) {
                    popUpTo(0)
                }
            }
        )

        composable<SplashDestination> {  }
    }

    LaunchedEffect(status) {
        when (status) {
            AuthState.Authenticated -> {
                scope.launch {
                    val userId = dataStoreUserPrefs.getValue("userId")
                    userId?.let {
                        navController.navigateToMainGraph(
                            navOptions = NavOptions.Builder().setPopUpTo<LoginDestination>(inclusive = true).build(),
                            userId = it
                        )
                    }
                }
            }
            AuthState.NonAuthenticated -> {
                navController.navigate(WelcomeDestination) {
                    popUpTo(0)
                }
            }
            AuthState.Loading -> {}
        }
    }
}