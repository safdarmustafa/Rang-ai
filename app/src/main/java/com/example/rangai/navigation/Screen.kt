package com.example.rangai.navigation
import android.net.Uri
sealed class Screen(val route: String) {

    data object Splash : Screen("splash")
    data object Login : Screen("login")

    data object Otp : Screen("otp")

    data object Home : Screen("home")


    object Result : Screen("result/{imageUrl}") {

        fun createRoute(
            imageUrl: String
        ): String {

            return "result/${Uri.encode(imageUrl)}"
        }
    }

}
