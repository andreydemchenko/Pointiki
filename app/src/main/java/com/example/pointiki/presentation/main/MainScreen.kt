package com.example.pointiki.presentation.main

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pointiki.R
import com.example.pointiki.presentation.utils.theme.PointikiTheme
import com.example.pointiki.presentation.utils.theme.SecondaryColor
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pointiki.presentation.home.events.EventsScreen
import com.example.pointiki.presentation.home.HomeScreen
import com.example.pointiki.presentation.leaderboard.LeaderboardScreen
import com.example.pointiki.presentation.shop.ShopScreen
import com.example.pointiki.presentation.qrcode.QRCodeScreen
import com.example.pointiki.presentation.home.HomeViewModel
import com.example.pointiki.presentation.home.achievements.AchievementDetailsScreen
import com.example.pointiki.presentation.home.achievements.AchievementsScreen
import com.example.pointiki.presentation.home.events.EventDetailsScreen
import com.example.pointiki.presentation.leaderboard.LeaderboardViewModel
import com.example.pointiki.presentation.leaderboard.UserDetailsScreen
import com.example.pointiki.presentation.shop.ShopViewModel
import com.example.pointiki.presentation.utils.theme.OrangeColor
import com.example.pointiki.presentation.utils.theme.luckiestGuyFont
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun MainScreen(
    //navController: NavController,
    viewModel: MainViewModel,
    homeViewModel: HomeViewModel,
    leaderboardViewModel: LeaderboardViewModel
) {

    val navController = rememberNavController()
    val currentPage by viewModel.currentPage.collectAsState()
    val shopViewModel by lazy { ShopViewModel() }

    val selectedEvent by homeViewModel.selectedEvent.collectAsState()
    val selectedAchievement by homeViewModel.selectedAchievement.collectAsState()
    val selectedUser by leaderboardViewModel.selectedUser.collectAsState()

//    if (currentPage == MainNavigation.HOME) {
//        val systemUiController = rememberSystemUiController()
//        systemUiController.setStatusBarColor(
//            color = SecondaryColor,
//            darkIcons = false
//        )
//    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            TopBarView(currentPage = currentPage, viewModel = viewModel)
            when (currentPage) {
                MainNavigation.HOME -> HomeScreen(navController, homeViewModel)
                MainNavigation.SCANNER -> QRCodeScreen()
                MainNavigation.LEADERBOARD -> LeaderboardScreen(navController, leaderboardViewModel)
                MainNavigation.SHOP -> ShopScreen(shopViewModel)
            }
        }

        Column {
            Spacer(modifier = Modifier.weight(1f))
            BottomNavigationView(currentPage, viewModel)
        }

        NavHost(navController, startDestination = "initial") {
            composable("initial") {}
            composable(MainScreenNavigation.Events.route) {
                AnimatedVisibility(
                    visible = true,
                    enter = slideInHorizontally(initialOffsetX = { it }),
                    exit = slideOutHorizontally(targetOffsetX = { it })
                ) {
                    EventsScreen(navController, viewModel = homeViewModel)
                }
            }
            composable(MainScreenNavigation.EventDetails.route) {
                AnimatedVisibility(
                    visible = selectedEvent != null,
                    enter = slideInHorizontally(initialOffsetX = { it }),
                    exit = slideOutHorizontally(targetOffsetX = { it })
                ) {
                    selectedEvent?.let {
                        EventDetailsScreen(navController, viewModel = homeViewModel, event = it)
                    }
                }
            }
            composable(MainScreenNavigation.Achievements.route) {
                AnimatedVisibility(
                    visible = true,
                    enter = slideInHorizontally(initialOffsetX = { it }),
                    exit = slideOutHorizontally(targetOffsetX = { it })
                ) {
                    AchievementsScreen(navController, viewModel = homeViewModel)
                }
            }
            composable(MainScreenNavigation.AchievementDetails.route) {
                AnimatedVisibility(
                    visible = selectedAchievement != null,
                    enter = slideInHorizontally(initialOffsetX = { it }),
                    exit = slideOutHorizontally(targetOffsetX = { it })
                ) {
                    selectedAchievement?.let {
                        AchievementDetailsScreen(navController, achievement = it)
                    }
                }
            }
            composable(MainScreenNavigation.UserDetails.route) {
                AnimatedVisibility(
                    visible = selectedUser != null,
                    enter = slideInHorizontally(initialOffsetX = { it }),
                    exit = slideOutHorizontally(targetOffsetX = { it })
                ) {
                    selectedUser?.let {
                        UserDetailsScreen(navController, user = it, viewModel = leaderboardViewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun BottomNavigationView(currentPage: MainNavigation, viewModel: MainViewModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 30.dp)
            .height(54.dp)
            .shadow(20.dp, shape = RoundedCornerShape(30.dp), ambientColor = Color.White),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = SecondaryColor.copy(alpha = 0.7f)),
        elevation = CardDefaults.cardElevation(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            BottomNavigationItem(
                icon = R.drawable.home_tab_icon,
                selected = currentPage == MainNavigation.HOME,
                onClick = { viewModel.setCurrentPage(MainNavigation.HOME) }
            )
            BottomNavigationItem(
                icon = R.drawable.scanner_tab_icon,
                selected = currentPage == MainNavigation.SCANNER,
                onClick = { viewModel.setCurrentPage(MainNavigation.SCANNER) }
            )
            BottomNavigationItem(
                icon = R.drawable.top_tab_item,
                selected = currentPage == MainNavigation.LEADERBOARD,
                onClick = { viewModel.setCurrentPage(MainNavigation.LEADERBOARD) }
            )
            BottomNavigationItem(
                icon = R.drawable.shop_tab_icon,
                selected = currentPage == MainNavigation.SHOP,
                onClick = { viewModel.setCurrentPage(MainNavigation.SHOP) }
            )
        }
    }
}

@Composable
fun TopBarView(currentPage: MainNavigation, viewModel: MainViewModel) {
    if (currentPage == MainNavigation.HOME) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(SecondaryColor),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                viewModel.logout()
            }) {
                Icon(imageVector = Icons.Default.Settings, contentDescription = null, tint = Color.White)
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "Pointiki",
                color = OrangeColor,
                fontSize = 28.sp,
                fontFamily = luckiestGuyFont
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Default.Notifications, contentDescription = null, tint = Color.White)
            }
        }
    }
}

@Composable
fun BottomNavigationItem(@DrawableRes icon: Int, selected: Boolean, onClick: () -> Unit) {
    val tint = if (selected) Color.Unspecified else Color.White
    IconButton(
        onClick = onClick,
        modifier = Modifier.padding(10.dp)
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
            tint = tint
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    PointikiTheme {
        MainScreen(
          //  navController = rememberNavController(),
            viewModel = hiltViewModel(),
            homeViewModel = hiltViewModel(),
            leaderboardViewModel = hiltViewModel()
        )
    }
}

sealed class MainScreenNavigation(val route: String) {
    object Events : MainScreenNavigation("events")
    object EventDetails : MainScreenNavigation("eventDetails")
    object Achievements: MainScreenNavigation("achievements")
    object AchievementDetails: MainScreenNavigation("achievementDetails")
    object UserDetails: MainScreenNavigation("userDetails")
}