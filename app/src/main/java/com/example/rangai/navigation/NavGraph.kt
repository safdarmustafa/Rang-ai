package com.example.rangai.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.rangai.auth.AuthViewModel
import com.example.rangai.auth.OtpAuthState
import com.example.rangai.auth.SplashViewModel
import com.example.rangai.ui.compare.CompareScreen
import com.example.rangai.ui.home.HomeScreen
import com.example.rangai.ui.login.LoginScreen
import com.example.rangai.ui.login.OtpScreen
import com.example.rangai.ui.login.SplashScreen
import com.example.rangai.ui.profile.ProfileScreen
import com.example.rangai.ui.profile.ProfileSetupScreen
import com.example.rangai.ui.profile.ProfileSetupViewModel
import com.example.rangai.ui.profile.ProfileUiState
import com.example.rangai.ui.profile.ProfileViewModel
import com.example.rangai.ui.result.ResultScreen

private const val TRANSITION_DURATION = 350

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()
    val profileSetupViewModel: ProfileSetupViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
        enterTransition = {
            fadeIn(animationSpec = tween(TRANSITION_DURATION)) +
                slideInHorizontally(
                    animationSpec = tween(TRANSITION_DURATION),
                    initialOffsetX = { it / 3 }
                )
        },
        exitTransition = {
            fadeOut(animationSpec = tween(TRANSITION_DURATION)) +
                slideOutHorizontally(
                    animationSpec = tween(TRANSITION_DURATION),
                    targetOffsetX = { -it / 3 }
                )
        },
        popEnterTransition = {
            fadeIn(animationSpec = tween(TRANSITION_DURATION)) +
                slideInHorizontally(
                    animationSpec = tween(TRANSITION_DURATION),
                    initialOffsetX = { -it / 3 }
                )
        },
        popExitTransition = {
            fadeOut(animationSpec = tween(TRANSITION_DURATION)) +
                slideOutHorizontally(
                    animationSpec = tween(TRANSITION_DURATION),
                    targetOffsetX = { it / 3 }
                )
        }
    ) {
        composable(Screen.Splash.route) {
            val splashViewModel: SplashViewModel = viewModel()
            val splashState by splashViewModel.splashState.collectAsState()

            SplashScreen(
                splashState = splashState,
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Login.route) {
            LoginScreen(
                onSendOtp = { phone ->
                    authViewModel.sendOtp(phone)
                    navController.navigate(Screen.Otp.route)
                }
            )
        }

        composable(Screen.Otp.route) {
            val isLoading by authViewModel.isLoading.collectAsState()
            val otpAuthState by authViewModel.otpAuthState.collectAsState()
            val otpErrorMessage = when (val state = otpAuthState) {
                OtpAuthState.InvalidOtp -> "Invalid OTP. Please try again."
                is OtpAuthState.Error -> state.message
                else -> null
            }

            LaunchedEffect(otpAuthState) {
                when (otpAuthState) {
                    OtpAuthState.ExistingUser -> {
                        android.util.Log.d("AUTH", "Routing to Home")
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                        authViewModel.resetOtpAuthState()
                    }

                    OtpAuthState.NewUser -> {
                        android.util.Log.d("AUTH", "Routing to Profile Setup")
                        navController.navigate(Screen.ProfileSetup.route)
                        authViewModel.resetOtpAuthState()
                    }

                    else -> Unit
                }
            }

            OtpScreen(
                isLoading = isLoading,
                errorMessage = otpErrorMessage,
                onVerify = { enteredOtp ->
                    authViewModel.onOtpSubmitted(enteredOtp)
                }
            )
        }

        composable(Screen.ProfileSetup.route) {
            ProfileSetupScreen(
                onContinue = { name, age ->
                    profileSetupViewModel.saveUser(
                        phoneNumber = authViewModel.getPhoneNumber(),
                        name = name,
                        age = age
                    )

                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(navController = navController)
        }

        composable(Screen.UserProfile.route) {
            val profileViewModel: ProfileViewModel = viewModel()
            val profileUiState by profileViewModel.uiState.collectAsState()
            var isLoggingOut by remember { mutableStateOf(false) }

            LaunchedEffect(profileUiState) {
                if (profileUiState is ProfileUiState.Error &&
                    (profileUiState as ProfileUiState.Error).message.contains("Session expired")
                ) {
                    android.util.Log.d("PROFILE", "Session expired on profile — routing to Login")
                    navController.navigate(Screen.Login.route) {
                        popUpTo(navController.graph.id) { inclusive = true }
                    }
                }
            }

            ProfileScreen(
                uiState = profileUiState,
                onNavigateBack = { navController.popBackStack() },
                onRetry = { profileViewModel.loadProfile() },
                onLogout = {
                    if (isLoggingOut) return@ProfileScreen
                    isLoggingOut = true
                    android.util.Log.d("PROFILE", "Logout initiated")
                    authViewModel.logout {
                        android.util.Log.d("PROFILE", "Logout complete — clearing back stack")
                        navController.navigate(Screen.Login.route) {
                            popUpTo(navController.graph.id) { inclusive = true }
                            launchSingleTop = true
                        }
                        isLoggingOut = false
                    }
                },
                isLoggingOut = isLoggingOut
            )
        }

        composable(
            route = Screen.Result.route,
            arguments = listOf(
                navArgument("imageUrl") { type = NavType.StringType },
                navArgument("originalUri") { type = NavType.StringType }
            )
        ) {
            val imageUrl = it.arguments?.getString("imageUrl") ?: ""
            val originalUri = it.arguments?.getString("originalUri") ?: ""

            ResultScreen(
                imageUrl = imageUrl,
                originalImageUri = originalUri,
                navController = navController
            )
        }

        composable(
            route = Screen.Compare.route,
            arguments = listOf(
                navArgument("originalUri") { type = NavType.StringType },
                navArgument("enhancedUrl") { type = NavType.StringType }
            )
        ) {
            val originalUri = it.arguments?.getString("originalUri") ?: ""
            val enhancedUrl = it.arguments?.getString("enhancedUrl") ?: ""

            CompareScreen(
                originalImageUri = originalUri,
                enhancedImageUrl = enhancedUrl,
                navController = navController
            )
        }
    }
}
