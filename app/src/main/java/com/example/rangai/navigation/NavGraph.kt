package com.example.rangai.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.rangai.ui.compare.CompareScreen
import com.example.rangai.ui.home.HomeScreen
import com.example.rangai.ui.login.LoginScreen
import com.example.rangai.ui.login.OtpScreen
import com.example.rangai.ui.login.SplashScreen
import com.example.rangai.ui.result.ResultScreen
import com.example.rangai.ui.profile.ProfileSetupScreen
private const val TRANSITION_DURATION = 350

@Composable
fun NavGraph() {
    val navController = rememberNavController()

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
            SplashScreen(
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Login.route) {
            LoginScreen(
                onSendOtp = {
                    navController.navigate(Screen.Otp.route)
                }
            )
        }

        composable(Screen.Otp.route) {
            OtpScreen(
                onVerify = {
                    navController.navigate(Screen.Profile.route)
                }
            )
        }

        composable(Screen.Profile.route) {
            ProfileSetupScreen(
                onContinue = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable(Screen.Home.route) {
            HomeScreen(navController = navController)
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
