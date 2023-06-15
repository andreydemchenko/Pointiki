package com.example.pointiki.presentation.leaderboard

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.pointiki.R
import com.example.pointiki.models.UserModel
import com.example.pointiki.presentation.main.MainScreenNavigation
import com.example.pointiki.presentation.utils.theme.OrangeColor
import com.example.pointiki.presentation.utils.theme.PointikiTheme
import com.example.pointiki.presentation.utils.theme.PrimaryColor
import com.example.pointiki.presentation.utils.theme.SecondaryColor
import com.example.pointiki.presentation.utils.theme.SecondaryLightColor
import com.example.pointiki.presentation.utils.theme.andikaFont
import com.example.pointiki.presentation.utils.theme.luckiestGuyFont
import com.example.pointiki.utils.UIState
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaderboardScreen(navController: NavController, viewModel: LeaderboardViewModel) {
    val sortTime by viewModel.sortTime.collectAsState()
    val sortedUsers by viewModel.sortedUsers.collectAsState()
    val dataState by viewModel.usersDataState.collectAsState()

    Scaffold(containerColor = PrimaryColor) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                modifier = Modifier.padding(top = 12.dp),
                text = "Leaderboard".uppercase(),
                fontFamily = luckiestGuyFont,
                fontSize = 30.sp,
                color = SecondaryColor
            )

            when (dataState) {
                is UIState.Loading -> {
                    Spacer(modifier = Modifier.weight(1f))
                    CircularProgressIndicator(
                        modifier = Modifier.size(60.dp),
                        color = SecondaryColor,
                        strokeWidth = 3.dp
                    )
                    Spacer(modifier = Modifier.weight(1f))
                }

                is UIState.Success -> {
                    LeaderboardSortButtons(viewModel, sortTime)

                    LazyColumn(modifier = Modifier.padding(vertical = 40.dp)) {
                        itemsIndexed(sortedUsers) { index, user ->
                            LeaderboardUserItemView(viewModel, navController, user, index + 1)
                        }
                    }
                }

                is UIState.Error -> {
                    Spacer(modifier = Modifier.weight(1f))
                    Text(text = "Error: ${(dataState as UIState.Error).exception.message}")
                    Spacer(modifier = Modifier.weight(1f))
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
                            color = Color.Black
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun LeaderboardSortButtons(viewModel: LeaderboardViewModel, sortTime: LeaderboardSortTime) {
    Card(
        modifier = Modifier
            .width(300.dp)
            .height(40.dp)
            .shadow(20.dp, shape = RoundedCornerShape(30.dp), ambientColor = SecondaryColor),
        shape = RoundedCornerShape(30.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.7f))
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            modifier = Modifier.padding(horizontal = 4.dp)
        ) {
            LeaderboardSortTime.values.forEach { period ->
                val isSortTime = sortTime == period
                val animationSpec = tween<Color>(durationMillis = 600, easing = FastOutSlowInEasing)
                val backgroundColor by animateColorAsState(if (isSortTime) SecondaryColor else Color.Transparent, animationSpec)
                val textColor by animateColorAsState(if (isSortTime) Color.White else SecondaryColor, animationSpec)

                Button(
                    onClick = { viewModel.setSortTime(period) },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    contentPadding = PaddingValues(0.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(30.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(backgroundColor)
                            .padding(horizontal = 4.dp)
                    ) {
                        Text(
                            text = period.value,
                            fontSize = 16.sp,
                            fontFamily = andikaFont,
                            color = textColor
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LeaderboardUserItemView(viewModel: LeaderboardViewModel, navController: NavController, item: UserModel, number: Int) {
    val numberText = remember { mutableStateOf("$number") }

    Row(
        modifier = Modifier
            .padding(2.dp)
            .padding(start = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = numberText.value,
            fontSize = 25.sp,
            fontFamily = luckiestGuyFont,
            color = SecondaryColor,
            modifier = Modifier.width(40.dp)
        )
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 4.dp)
                .padding(vertical = 4.dp)
                .clickable {
                    viewModel.setSelectedUser(item)
                    navController.navigate(MainScreenNavigation.UserDetails.route)
                },
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(SecondaryLightColor.copy(alpha = 0.2f))
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(item.imageUrl),
                    contentDescription = "User Icon",
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                )
                Text(
                    text = item.name,
                    fontSize = 18.sp,
                    fontFamily = luckiestGuyFont,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.weight(1f))
                Card(
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .widthIn(min = 80.dp)
                        .padding(top = 10.dp, bottom = 4.dp, end = 12.dp)
                        .shadow(20.dp, RoundedCornerShape(20.dp), spotColor = Color.Yellow),
                    colors = CardDefaults.cardColors(OrangeColor.copy(alpha = 0.9f))
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "${item.points}",
                            fontSize = 20.sp,
                            fontFamily = luckiestGuyFont,
                            color = Color.White,
                            modifier = Modifier.padding(start = 10.dp, end = 4.dp)
                        )
                        PointikiView()
                    }
                }
            }
        }
    }
}

@Composable
fun PointikiView() {
    val cubeIconPainter = rememberAsyncImagePainter(model = R.drawable.cube_icon)
    Box(modifier = Modifier.size(30.dp)) {
        Image(
            painter = cubeIconPainter,
            contentDescription = "Cube Icon",
            modifier = Modifier
                .size(10.dp)
                .offset(y = 5.dp, x = 4.dp)
        )
        Image(
            painter = cubeIconPainter,
            contentDescription = "Cube Icon",
            modifier = Modifier
                .size(10.dp)
                .offset(y = 12.dp)
                .shadow(4.dp)
        )
        Image(
            painter = cubeIconPainter,
            contentDescription = "Cube Icon",
            modifier = Modifier
                .size(10.dp)
                .offset(x = 8.dp, y = 12.dp)
                .shadow(4.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LeaderboardScreenPreview() {
    PointikiTheme {
        LeaderboardScreen(navController = rememberNavController(), viewModel = hiltViewModel())
    }
}