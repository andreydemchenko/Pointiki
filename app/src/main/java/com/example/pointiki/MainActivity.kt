package com.example.pointiki

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pointiki.presentation.home.HomeViewModel
import com.example.pointiki.presentation.leaderboard.LeaderboardViewModel
import com.example.pointiki.presentation.main.MainScreen
import com.example.pointiki.presentation.utils.theme.PointikiTheme
import com.example.pointiki.presentation.main.MainViewModel
import com.example.pointiki.presentation.start.WelcomeScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    companion object {
        var mainActivity: MainActivity? = null

        fun getInstance(): MainActivity? = mainActivity
    }

    private val mainViewModel: MainViewModel by viewModels()
    private val homeViewModel: HomeViewModel by viewModels()
    private val leaderboardViewModel: LeaderboardViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        mainActivity = this
        super.onCreate(savedInstanceState)
        setContent {
            PointikiTheme {
                val navController = rememberNavController()
                val isUserLoggedIn = mainViewModel.isUserLoggedIn.collectAsState()
                val startMainNavigation = if (isUserLoggedIn.value) Navigation.Main.route else Navigation.Login.route


                NavHost(navController, startDestination = startMainNavigation) {
                    composable(Navigation.Login.route) {
                        WelcomeScreen()
                    }
                    composable(Navigation.Main.route) {
                        MainScreen(
                           // navController,
                            mainViewModel,
                            homeViewModel,
                            leaderboardViewModel
                        )
                    }

                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mainActivity = this
    }

    override fun onRestart() {
        super.onRestart()
        mainActivity = this
    }

    override fun onDestroy() {
        super.onDestroy()
        mainActivity = null
    }
}

sealed class Navigation(val route: String) {
    object Login : Navigation("login")
    object Main : Navigation("main")
}