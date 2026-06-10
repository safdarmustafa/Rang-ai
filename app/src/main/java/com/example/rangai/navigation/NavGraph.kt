package com.example.rangai.navigation

import com.example.rangai.ui.login.SplashScreen
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.rangai.ui.login.LoginScreen
import com.example.rangai.ui.login.OtpScreen



@Composable
fun NavGraph() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {

            SplashScreen(
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route)
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
                    // Home screen later
                }
            )
        }
    }
}