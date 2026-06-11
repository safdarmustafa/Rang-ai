package com.example.rangai.navigation
import com.example.rangai.ui.home.HomeScreen
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.rangai.ui.login.LoginScreen
import com.example.rangai.ui.login.OtpScreen
import com.example.rangai.ui.login.SplashScreen

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

                    navController.navigate(Screen.Login.route) {

                        popUpTo(Screen.Splash.route) {
                            inclusive = true
                        }
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

                    navController.navigate(Screen.Home.route) {

                        popUpTo(Screen.Login.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable(Screen.Home.route) {
            HomeScreen()
        }
    }
}
