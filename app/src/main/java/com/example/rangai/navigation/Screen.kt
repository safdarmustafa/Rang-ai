package com.example.rangai.navigation

import android.net.Uri

sealed class Screen(val route: String) {

    data object Splash : Screen("splash")
    data object Login : Screen("login")
    data object Otp : Screen("otp")

    data object Profile : Screen("profile")
    data object Home : Screen("home")

    object Result : Screen("result/{imageUrl}/{originalUri}") {

        fun createRoute(
            imageUrl: String,
            originalUri: String
        ): String {
            return "result/${Uri.encode(imageUrl)}/${Uri.encode(originalUri)}"
        }
    }

    object Compare : Screen("compare/{originalUri}/{enhancedUrl}") {

        fun createRoute(
            originalUri: String,
            enhancedUrl: String
        ): String {
            return "compare/${Uri.encode(originalUri)}/${Uri.encode(enhancedUrl)}"
        }
    }
}
