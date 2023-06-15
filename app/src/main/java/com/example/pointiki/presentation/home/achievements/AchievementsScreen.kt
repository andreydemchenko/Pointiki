package com.example.pointiki.presentation.home.achievements

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.pointiki.R
import com.example.pointiki.models.Achievement
import com.example.pointiki.presentation.home.HomeViewModel
import com.example.pointiki.presentation.main.MainScreenNavigation
import com.example.pointiki.presentation.utils.components.BlackAndWhiteImage
import com.example.pointiki.presentation.utils.theme.AchievementColor
import com.example.pointiki.presentation.utils.theme.PrimaryColor
import com.example.pointiki.presentation.utils.theme.SecondaryColor
import com.example.pointiki.presentation.utils.theme.SecondaryLightColor
import com.example.pointiki.presentation.utils.theme.luckiestGuyFont

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AchievementsScreen(
    navController: NavController,
    viewModel: HomeViewModel
) {
    Scaffold(containerColor = PrimaryColor) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(SecondaryLightColor)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Button(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape),
                    contentPadding = PaddingValues(0.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SecondaryColor),
                    onClick = { navController.popBackStack() }) {

                    Image(
                        modifier = Modifier
                            .width(40.dp)
                            .height(32.dp),
                        painter = painterResource(id = R.drawable.back_button_icon),
                        contentDescription = null
                    )
                }
                Text(
                    modifier = Modifier.shadow(16.dp, spotColor = Color.White),
                    text = "Achievements".uppercase(),
                    fontFamily = luckiestGuyFont,
                    fontSize = 24.sp,
                    color = SecondaryColor,
                    maxLines = 1
                )
            }

            val achievements by viewModel.allAchievements.collectAsState()
            val userAchievements by viewModel.achievements.collectAsState()
            val chunkedAchievements = achievements.chunked(3)

            LazyColumn {
                items(chunkedAchievements) { achievements ->
                    AchievementRow(achievements, userAchievements, navController, viewModel)
                }
            }
        }
    }
}

@Composable
fun AchievementRow(achievements: List<Achievement>, userAchievements: List<Achievement>, navController: NavController, viewModel: HomeViewModel) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        for (achievement in achievements) {
            AchievementView(achievement, userAchievements, navController, viewModel)
        }
    }
}

@Composable
fun AchievementView(achievement: Achievement, userAchievements: List<Achievement>, navController: NavController, viewModel: HomeViewModel) {
    val userHasAchievement = userAchievements.map { it.id }.contains(achievement.id)
    Card(
        modifier = Modifier
            .padding(8.dp)
            .size(80.dp),
        colors = CardDefaults.cardColors(if (userHasAchievement) AchievementColor else Color.LightGray),
        elevation = CardDefaults.cardElevation(12.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (userHasAchievement) {
                Image(
                    painter = rememberAsyncImagePainter(achievement.imageUrl),
                    contentDescription = "Achievement",
                    modifier = Modifier
                        .size(70.dp)
                        .clickable {
                            viewModel.setSelectedAchievement(achievement)
                            navController.navigate(MainScreenNavigation.AchievementDetails.route)
                        }
                )
            } else {
                BlackAndWhiteImage(
                    painter = rememberAsyncImagePainter(achievement.imageUrl),
                    contentDescription = "Achievement",
                    modifier = Modifier
                        .size(70.dp),
                    size = 70.dp
                )
            }
        }
    }
}
