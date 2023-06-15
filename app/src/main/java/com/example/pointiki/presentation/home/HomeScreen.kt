package com.example.pointiki.presentation.home

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.pointiki.R
import com.example.pointiki.models.Achievement
import com.example.pointiki.presentation.home.events.EventCard
import com.example.pointiki.presentation.leaderboard.PointikiView
import com.example.pointiki.presentation.main.MainScreenNavigation
import com.example.pointiki.presentation.utils.components.BlackAndWhiteImage
import com.example.pointiki.presentation.utils.theme.AchievementColor
import com.example.pointiki.presentation.utils.theme.OrangeColor
import com.example.pointiki.presentation.utils.theme.PointikiTheme
import com.example.pointiki.presentation.utils.theme.PrimaryColor
import com.example.pointiki.presentation.utils.theme.SecondaryColor
import com.example.pointiki.presentation.utils.theme.SecondaryLightColor
import com.example.pointiki.presentation.utils.theme.YellowColor
import com.example.pointiki.presentation.utils.theme.andikaFont
import com.example.pointiki.presentation.utils.theme.luckiestGuyFont
import com.example.pointiki.utils.UIState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel
) {
    var shouldAnimateDigits by remember { mutableStateOf(false) }
    var shouldAnimateAchievements by remember { mutableStateOf(false) }
    var shouldAnimateCubIcons by remember { mutableStateOf(false) }

    val userDataState by viewModel.userDataState.collectAsState()
    val user by viewModel.userData.collectAsState()
    val eventsDataState by viewModel.eventsDataState.collectAsState()
    val achievementsDataState by viewModel.achievementsDataState.collectAsState()
    val achievements = viewModel.achievements.collectAsState()
    val nextAchievement = viewModel.nextAchievement.collectAsState()

    Scaffold(containerColor = PrimaryColor) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            when (userDataState) {
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
                    user?.let { user ->
                        val pointsCount by remember { mutableStateOf(user.points) }

                        val digits = pointsCount.toString().map { it.toString().toInt() }

                        Row(modifier = Modifier.padding(top = 16.dp)) {
                            Image(
                                rememberAsyncImagePainter(user.imageUrl),
                                contentDescription = "Boy Image",

                                modifier = Modifier
                                    .size(120.dp)
                                    .clip(RoundedCornerShape(30.dp))
                                    .clickable { }
                                //.border(2.dp, SecondaryColor, RoundedCornerShape(30.dp)),
                            )

                            Spacer(modifier = Modifier.weight(1f))
                            Column {
                                Spacer(modifier = Modifier.height(16.dp))

                                Row(
                                    modifier = Modifier.padding(start = 12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    LaunchedEffect(Unit) {
                                        delay(1000)
                                        shouldAnimateDigits = true
                                    }
                                    AnimatedDigits(
                                        digits = digits,
                                        shouldAnimate = shouldAnimateDigits
                                    )
                                    LaunchedEffect(Unit) {
                                        delay(500L)
                                        shouldAnimateCubIcons = true
                                    }
                                    CubesAnimation(shouldAnimateCubIcons)
                                }
                                Text(
                                    text = user.name,
                                    color = SecondaryColor,
                                    fontSize = 28.sp,
                                    fontFamily = luckiestGuyFont
                                )
                            }
                            Spacer(modifier = Modifier.weight(1f))
                        }

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp),
                            shape = RoundedCornerShape(30.dp),
                            colors = CardDefaults.cardColors(containerColor = SecondaryLightColor),
                            elevation = CardDefaults.cardElevation(12.dp)
                        ) {
                            Column(
                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                var shouldShowPointsValue by remember { mutableStateOf(false) }
                                nextAchievement.value?.let { achievement ->
                                    Box(
                                        modifier = Modifier
                                            .width(240.dp)
                                            .height(100.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        if (shouldShowPointsValue) {
                                            Row {
                                                Spacer(modifier = Modifier.weight(1f))
                                                Card(
                                                    shape = RoundedCornerShape(20.dp),
                                                    modifier = Modifier
                                                        .widthIn(min = 60.dp)
                                                        .height(40.dp)
                                                        .padding(
                                                            top = 10.dp,
                                                            bottom = 4.dp,
                                                            end = 12.dp
                                                        )
                                                        .shadow(
                                                            20.dp,
                                                            RoundedCornerShape(20.dp),
                                                            spotColor = Color.White
                                                        ),
                                                    colors = CardDefaults.cardColors(
                                                        Color.LightGray.copy(
                                                            alpha = 0.9f
                                                        )
                                                    )
                                                ) {
                                                    Row {
                                                        Text(
                                                            text = "+ ${achievement.points}",
                                                            fontSize = 18.sp,
                                                            fontFamily = luckiestGuyFont,
                                                            color = Color.Gray,
                                                            modifier = Modifier.padding(
                                                                start = 20.dp,
                                                                end = 4.dp,
                                                                top = 2.dp
                                                            )
                                                        )
                                                        PointikiView()
                                                    }
                                                }
                                            }
                                        }
                                        BlackAndWhiteImage(
                                            painter = rememberAsyncImagePainter(achievement.imageUrl),
                                            contentDescription = null,
                                            modifier = Modifier.size(100.dp),
                                            size = 100.dp
                                        )
                                    }
                                    val progress = remember(user.progresses) {
                                        mutableStateOf(user.progresses.find { it.id == achievement.id }?.progress?.toFloat() ?: 0f)
                                    }
                                    val commitment = mutableStateOf(achievement.commitment.toFloat())

                                    Spacer(modifier = Modifier.weight(1f))
                                    FireProgressView(progress = progress, total = commitment)

                                    LaunchedEffect(Unit) {
                                        delay(500L)
                                        shouldShowPointsValue = true
                                    }
                                }
                            }
                        }
                        LaunchedEffect(Unit) {
                            delay(1000)
                            shouldAnimateAchievements = true
                        }
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp),
                            shape = RoundedCornerShape(30.dp),
                            colors = CardDefaults.cardColors(containerColor = SecondaryLightColor),
                            elevation = CardDefaults.cardElevation(12.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(horizontal = 12.dp)
                                    .padding(top = 12.dp)
                            ) {
                                Text(
                                    text = "Achievements".uppercase(),
                                    fontFamily = luckiestGuyFont,
                                    fontSize = 20.sp,
                                    color = SecondaryColor
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                Button(
                                    modifier = Modifier
                                        .width(140.dp)
                                        .height(40.dp)
                                        .offset(x = 20.dp, y = (-10).dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                                    onClick = { navController.navigate(MainScreenNavigation.Achievements.route) }
                                ) {
                                    Text(
                                        modifier = Modifier.fillMaxSize(),
                                        text = "View all".uppercase(),
                                        fontFamily = luckiestGuyFont,
                                        fontSize = 20.sp,
                                        color = YellowColor,
                                        maxLines = 1
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.weight(1f))
                            if (achievementsDataState is UIState.Success<*> && achievements.value.isNotEmpty()) {
                                AchievementsHomeRow(navController, achievements.value, viewModel)
                            }
                            Spacer(modifier = Modifier.weight(1f))
                        }

                        Card(
                            modifier = Modifier
                                .fillMaxWidth(),
                            //.heightIn(min = 380.dp, max = 540.dp),
                            shape = RoundedCornerShape(30.dp),
                            colors = CardDefaults.cardColors(containerColor = SecondaryLightColor),
                            elevation = CardDefaults.cardElevation(12.dp)
                        ) {
                            when (eventsDataState) {
                                is UIState.Loading -> {
                                    Spacer(modifier = Modifier.weight(1f))
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(40.dp),
                                        color = SecondaryColor,
                                        strokeWidth = 3.dp
                                    )
                                    Spacer(modifier = Modifier.weight(1f))
                                }

                                is UIState.Success -> {
                                    Row(
                                        modifier = Modifier
                                            .padding(horizontal = 12.dp)
                                            .padding(top = 12.dp)
                                    ) {
                                        Text(
                                            text = "Upcoming events".uppercase(),
                                            fontFamily = luckiestGuyFont,
                                            fontSize = 20.sp,
                                            color = SecondaryColor
                                        )
                                        Spacer(modifier = Modifier.weight(1f))
                                        Button(
                                            modifier = Modifier
                                                .width(140.dp)
                                                .height(40.dp)
                                                .offset(x = 20.dp, y = (-10).dp),
                                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                                            onClick = { navController.navigate(MainScreenNavigation.Events.route) }
                                        ) {
                                            Text(
                                                modifier = Modifier.fillMaxSize(),
                                                text = "View all".uppercase(),
                                                fontFamily = luckiestGuyFont,
                                                fontSize = 20.sp,
                                                color = YellowColor,
                                                maxLines = 1
                                            )
                                        }
                                    }
                                    //Spacer(modifier = Modifier.weight(1f))
                                    val firstTwoEvents by viewModel.getFirstTwoEvents()
                                        .collectAsState(initial = emptyList())
                                    Column(
                                        modifier = Modifier.padding(12.dp),
                                        verticalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        firstTwoEvents.forEach { event ->
                                            EventCard(
                                                navController = navController,
                                                event = event,
                                                viewModel = viewModel,
                                                includeDayInTime = true
                                            )
                                        }
                                    }
                                }

                                is UIState.Error -> {
                                    // Show error state
                                    Text(text = "Error: ${(userDataState as UIState.Error).exception.message}")
                                }

                                else -> {}
                            }
                        }
                        Spacer(modifier = Modifier.height(50.dp))
                    }
                }

                is UIState.Error -> {
                    // Show error state
                    Text(text = "Error: ${(userDataState as UIState.Error).exception.message}")
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
fun AnimatedDigits(
    digits: List<Int>,
    shouldAnimate: Boolean,
) {
    val coroutineScope = rememberCoroutineScope()
    val digitOffsets = remember { mutableStateListOf(*Array(digits.size) { -300f }) }

    digits.forEachIndexed { index, _ ->
        val animationState = animateFloatAsState(
            targetValue = if (shouldAnimate) 0f else -300f,
            animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing)
        )

        SideEffect {
            if (shouldAnimate) {
                coroutineScope.launch {
                    delay((200 * index).toLong())
                    digitOffsets[index] = animationState.value
                }
            }
        }
    }

    Row {
        digits.forEachIndexed { index, digit ->
            Text(
                text = digit.toString(),
                fontSize = 80.sp,
                fontFamily = luckiestGuyFont,
                color = OrangeColor,
                modifier = Modifier.offset(y = digitOffsets[index].dp)
            )
        }
    }
}


@Composable
fun CubesAnimation(
    shouldAnimate: Boolean
) {
    val cube1Offset by animateOffsetAsState(
        if (shouldAnimate) Offset.Zero else Offset(0f, -300f),
        animationSpec = tween(
            durationMillis = 500,
            delayMillis = 500
        )
    )

    val cube2Offset by animateOffsetAsState(
        if (shouldAnimate) Offset.Zero else Offset(0f, -300f),
        animationSpec = tween(
            durationMillis = 500,
            delayMillis = 1250
        )
    )

    val cube3Offset by animateOffsetAsState(
        if (shouldAnimate) Offset.Zero else Offset(0f, -300f),
        animationSpec = tween(
            durationMillis = 500,
            delayMillis = 1000
        )
    )

    val cube4Offset by animateOffsetAsState(
        if (shouldAnimate) Offset.Zero else Offset(0f, -300f),
        animationSpec = tween(
            durationMillis = 500,
            delayMillis = 750
        )
    )

    val cubeIconPainter = rememberAsyncImagePainter(model = R.drawable.cube_icon)

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = cubeIconPainter,
            contentDescription = "Cube Icon",
            modifier = Modifier
                .size(20.dp)
                .offset {
                    IntOffset(
                        cube1Offset.x.roundToInt() + 18,
                        cube1Offset.y.roundToInt() - 13
                    )
                }
        )

        Image(
            painter = cubeIconPainter,
            contentDescription = "Cube Icon",
            modifier = Modifier
                .size(20.dp)
                .offset {
                    IntOffset(
                        cube2Offset.x.roundToInt() + 18,
                        cube2Offset.y.roundToInt() - 30
                    )
                }
        )

        Image(
            painter = cubeIconPainter,
            contentDescription = "Cube Icon",
            modifier = Modifier
                .size(20.dp)
                .offset { IntOffset(cube3Offset.x.roundToInt(), cube3Offset.y.roundToInt()) }
        )

        Image(
            painter = cubeIconPainter,
            contentDescription = "Cube Icon",
            modifier = Modifier
                .size(20.dp)
                .offset { IntOffset(cube4Offset.x.roundToInt() + 32, cube4Offset.y.roundToInt()) }
        )
    }
}

@Composable
fun FireProgressView(
    progress: MutableState<Float>,
    total: MutableState<Float>
) {
    var changingProgress by remember { mutableStateOf(0f) }
    LaunchedEffect(Unit) {
        delay(1000L)
        while (changingProgress < progress.value) {
            delay(100L)
            changingProgress += 1
        }
    }
    val animatedProgress by animateFloatAsState(
        targetValue = changingProgress / total.value,
        animationSpec = tween(
            durationMillis = 1000,
            easing = LinearEasing
        )
    )

    val animatedProgressText by animateIntAsState(
        targetValue = changingProgress.toInt(),
        animationSpec = tween(
            durationMillis = ((changingProgress * 100).toInt())
        )
    )

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(16.dp),
        contentAlignment = Alignment.TopStart
    ) {
        val width = constraints.maxWidth

        // Base progress bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(35.dp)
                .clip(RoundedCornerShape(30.dp))
                .background(Color.LightGray)
        )

        // Gradient progress
        Box(
            modifier = Modifier
                .width(with(LocalDensity.current) { (width * animatedProgress).toDp() })
                .height(35.dp)
                .clip(RoundedCornerShape(30.dp))
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color.Red,
                            Color.Yellow,
                            OrangeColor
                        )
                    )
                )
        )

        // Fire Icon
        if (animatedProgress != 0f) {
            Image(
                painter = rememberAsyncImagePainter(model = R.drawable.fire_icon),
                contentDescription = "Fire Icon",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .width(110.dp)
                    .height(80.dp)
                    .offset(
                        x = with(LocalDensity.current) { ((width * animatedProgress) - 260 / 2).toDp() },
                        y = (-10).dp
                    )
            )
        }

//        // Achievement Icon
//        Image(
//            painter = painterResource(id = R.drawable.achievement6),
//            contentDescription = "Achievement Icon",
//            contentScale = ContentScale.FillBounds,
//            modifier = Modifier
//                .size(50.dp)
//                .offset(
//                    x = with(LocalDensity.current) { (width - 100).toDp() },
//                    y = (-5).dp
//                )
//        )

        // Progress Text
        Text(
            text = "$animatedProgressText / ${total.value.toInt()}",
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = (-10).dp),
            color = SecondaryColor,
            fontSize = 16.sp,
            fontFamily = luckiestGuyFont,
            textAlign = TextAlign.Center
        )
    }
}


@Composable
fun AchievementsHomeRow(
    navController: NavController,
    achievements: List<Achievement>,
    viewModel: HomeViewModel
) {
    var shouldAnimateAchievements by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(1000L)
        shouldAnimateAchievements = true
    }

    Row(
        modifier = Modifier.padding(horizontal = 12.dp),
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
                        viewModel.setSelectedAchievement(achievement)
                        navController.navigate(MainScreenNavigation.AchievementDetails.route)
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
fun HomeScreenPreview() {
    PointikiTheme {
        HomeScreen(navController = rememberNavController(), viewModel = hiltViewModel())
    }
}