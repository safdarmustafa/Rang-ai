package com.example.rangai.navigation

sealed class Screen(val route: String) {

    data object Splash : Screen("splash")
    data object Login : Screen("login")

    data object Otp : Screen("otp")

    data object Home : Screen("home")
}