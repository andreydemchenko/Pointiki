package com.example.pointiki.presentation.start

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pointiki.presentation.main.MainScreenNavigation
import com.example.pointiki.presentation.start.login.LoginScreen
import com.example.pointiki.presentation.start.registration.RegistrationScreen
import com.example.pointiki.presentation.utils.theme.PointikiTheme
import com.example.pointiki.presentation.utils.theme.SecondaryColor

@Composable
fun WelcomeScreen() {

    val navController = rememberNavController()

    Box(modifier = Modifier.fillMaxSize()) {
        NavHost(navController, startDestination = WelcomeScreenNavigation.Initial.route) {
            composable(WelcomeScreenNavigation.Initial.route) { InitialScreen(navController = navController) }
            composable(WelcomeScreenNavigation.Registration.route) { RegistrationScreen(navController = navController) }
            composable(WelcomeScreenNavigation.Login.route) { LoginScreen(navController = navController) }
        }
    }
}

sealed class WelcomeScreenNavigation(val route: String) {
    object Initial : WelcomeScreenNavigation("initial")
    object Registration : WelcomeScreenNavigation("registration")
    object Login : WelcomeScreenNavigation("login")
}

@Preview(showBackground = true)
@Composable
fun WelcomeScreenPreview() {
    PointikiTheme {
        WelcomeScreen()
    }
}
