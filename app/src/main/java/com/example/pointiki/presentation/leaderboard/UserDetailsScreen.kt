package com.example.pointiki.presentation.leaderboard

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.pointiki.R
import com.example.pointiki.models.Achievement
import com.example.pointiki.models.PointEntry
import com.example.pointiki.models.UserModel
import com.example.pointiki.presentation.home.AchievementsHomeRow
import com.example.pointiki.presentation.home.HomeViewModel
import com.example.pointiki.presentation.main.MainScreenNavigation
import com.example.pointiki.presentation.utils.theme.AchievementColor
import com.example.pointiki.presentation.utils.theme.PointikiTheme
import com.example.pointiki.presentation.utils.theme.SecondaryColor
import com.example.pointiki.presentation.utils.theme.SecondaryLightColor
import com.example.pointiki.presentation.utils.theme.andikaFont
import com.example.pointiki.presentation.utils.theme.luckiestGuyFont
import com.example.pointiki.utils.UIState
import kotlinx.coroutines.delay
import java.util.Date
import java.util.UUID

@Composable
fun UserDetailsScreen(navController: NavController, user: UserModel, viewModel: LeaderboardViewModel) {
    val achievements = viewModel.achievements.collectAsState()
    val achievementsDataState by viewModel.achievementsDataState.collectAsState()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.4f))
            .pointerInput(Unit) {
                detectTapGestures {
                    navController.popBackStack()
                    viewModel.setSelectedUser(null)
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .width(300.dp)
                .height(330.dp)
                .pointerInput(Unit) { detectTapGestures {} },
            shape = RoundedCornerShape(30.dp),
            elevation = CardDefaults.cardElevation(50.dp),
            colors = CardDefaults.cardColors(SecondaryColor)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.CenterHorizontally),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Row {
                    Image(
                        painter = rememberAsyncImagePainter(user.imageUrl),
                        contentDescription = null,
                        modifier = Modifier
                            .size(120.dp)
                            .clip(RoundedCornerShape(20.dp)),
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Column {
                        Text(
                            text = user.name,
                            fontFamily = luckiestGuyFont,
                            fontSize = 24.sp,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            text = "All earned points",
                            fontFamily = andikaFont,
                            fontSize = 14.sp,
                            color = Color.LightGray
                        )
                        val allPoints = user.pointsHistory.sumOf { it.points }
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                modifier = Modifier.padding(end = 2.dp),
                                text = "$allPoints",
                                fontFamily = luckiestGuyFont,
                                fontSize = 30.sp,
                                color = Color.White
                            )
                            PointikiView()
                        }
                    }
                }
                Text(
                    text = "Achievements".uppercase(),
                    fontFamily = luckiestGuyFont,
                    fontSize = 18.sp,
                    color = SecondaryLightColor
                )
                when(achievementsDataState) {
                    is UIState.Loading -> {
                        Row {
                            Spacer(modifier = Modifier.weight(1f))
                            CircularProgressIndicator(
                                modifier = Modifier.size(40.dp),
                                color = SecondaryLightColor,
                                strokeWidth = 3.dp
                            )
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                    is UIState.Success -> {
                        if (achievements.value.isNotEmpty()) {
                            AchievementsUserDetailsRow(navController, achievements.value, viewModel)
                        } else {
                            Row {
                                Spacer(modifier = Modifier.weight(1f))
                                Text(
                                    modifier = Modifier.align(Alignment.CenterVertically),
                                    text = "User don't have any achievements",
                                    fontSize = 16.sp,
                                    fontFamily = andikaFont,
                                    color = SecondaryLightColor
                                )
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                    is UIState.Error -> {
                        Text(text = "Error: ${(achievementsDataState as UIState.Error).exception.message}")
                    }
                    else -> {
                        val textVisible = remember { mutableStateOf(false) }
                        LaunchedEffect(Unit) {
                            delay(1000)
                            textVisible.value = true
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        if (textVisible.value) {
                            Text(
                                text = "Check your internet connection",
                                fontSize = 30.sp,
                                fontFamily = andikaFont,
                                color = Color.Red
                            )
                        }
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun AchievementsUserDetailsRow(
    navController: NavController,
    achievements: List<Achievement>,
    viewModel: LeaderboardViewModel
) {
    var shouldAnimateAchievements by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(1000L)
        shouldAnimateAchievements = true
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        for (index in 0 until minOf(achievements.size, 5)) {
            val achievement = achievements[achievements.size - 1 - index]

            val offsetX by animateDpAsState(
                targetValue = if (shouldAnimateAchievements) 0.dp else 400.dp,
                animationSpec = tween(
                    durationMillis = 500,
                    delayMillis = (index * 100),
                    easing = LinearEasing
                )
            )

            Card(
                modifier = Modifier
                    .padding(8.dp)
                    .size(80.dp)
                    .offset(x = offsetX)
                    .clickable {
                        //viewModel.setSelectedAchievement(achievement)
                        //navController.navigate(MainScreenNavigation.AchievementDetails.route)
                    },
                colors = CardDefaults.cardColors(AchievementColor),
                elevation = CardDefaults.cardElevation(12.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(achievement.imageUrl),
                        contentDescription = "Achievement ${index + 1}",
                        modifier = Modifier
                            .size(70.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UserDetailsPreview() {
    PointikiTheme {
        UserDetailsScreen(
            navController = rememberNavController(),
            user = UserModel(
                UUID.randomUUID(),
                "",
                "Kolyan",
                "",
                100,
                listOf(
                    PointEntry(12, Date()),
                    PointEntry(13, Date()),
                    PointEntry(25, Date())
                ),
                listOf(),
                listOf()
            ),
            hiltViewModel<LeaderboardViewModel>()
        )
    }
}
