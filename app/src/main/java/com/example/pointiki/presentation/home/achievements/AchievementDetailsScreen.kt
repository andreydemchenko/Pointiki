package com.example.pointiki.presentation.home.achievements

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.pointiki.R
import com.example.pointiki.models.Achievement
import com.example.pointiki.models.AchievementType
import com.example.pointiki.presentation.leaderboard.PointikiView
import com.example.pointiki.presentation.utils.theme.PointikiTheme
import com.example.pointiki.presentation.utils.theme.SecondaryColor
import com.example.pointiki.presentation.utils.theme.SecondaryLightColor
import com.example.pointiki.presentation.utils.theme.luckiestGuyFont
import java.util.UUID

@Composable
fun AchievementDetailsScreen(
    navController: NavController,
    achievement: Achievement
) {
//    val topCenterColor = Color(0xFF6DC8EC)
//    val bottomLeftColor = Color(0xFF2B2F9F)
//    val bottomRightColor = Color(0xFFD86AC8)
//
//    val gradientTopToBottom = Brush.verticalGradient(
//        colors = listOf(Color(0xFF6DC8EC), Color(0xFF2B2F9F))
//    )
//
//    val gradientTopLeftToBottomRight = Brush.linearGradient(
//        colors = listOf(Color(0xFF6DC8EC), Color(0xFFD86AC8)),
//        start = Offset(0f, 0f),
//        end = Offset(0f, Float.POSITIVE_INFINITY)
//    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.4f))
            .pointerInput(Unit) {
                detectTapGestures { navController.popBackStack() }
            },
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .width(300.dp)
                .height(400.dp)
                .pointerInput(Unit) { detectTapGestures {} },
            shape = RoundedCornerShape(30.dp),
            elevation = CardDefaults.cardElevation(50.dp),
            colors = CardDefaults.cardColors(SecondaryColor)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.CenterHorizontally),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(achievement.imageUrl),
                    contentDescription = null,
                    modifier = Modifier.size(220.dp),
                )
                Text(
                    text = achievement.name,
                    fontFamily = luckiestGuyFont,
                    fontSize = 24.sp,
                    color = Color.White
                )
                if (achievement.type == AchievementType.VISITS) {
                    val annotatedString = buildAnnotatedString {
                        withStyle(style = SpanStyle(textDecoration = TextDecoration.LineThrough)) {
                            append("visit ${achievement.commitment} events")
                        }
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(R.drawable.checkmark_done),
                            contentDescription = null,
                            modifier = Modifier.size(26.dp),
                        )
                        Text(
                            text = annotatedString,
                            fontFamily = luckiestGuyFont,
                            fontSize = 18.sp,
                            color = Color.LightGray
                        )
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "You've got ${achievement.points}",
                        fontFamily = luckiestGuyFont,
                        fontSize = 18.sp,
                        color = SecondaryLightColor
                    )
                    PointikiView()
                }
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AchievementDetailsPreview() {
    PointikiTheme {
        AchievementDetailsScreen(
            navController = rememberNavController(),
            achievement =
            Achievement(
                UUID.randomUUID(),
                "https://firebasestorage.googleapis.com/v0/b/pointiki.appspot.com/o/achievementsIcons%2Fachievement4.png?alt=media&token=1d3030aa-7b05-440c-9d0f-a2a579b40a4b&_gl=1*13awu48*_ga*MTAzNzM2OTU5LjE2ODU5NDI0NjA.*_ga_CW55HF8NVT*MTY4NjMwMzAyNy4xNC4xLjE2ODYzMDM1MjkuMC4wLjA.",
                "Tenacious Tracker",
                70,
                30,
                AchievementType.VISITS,
                4
            )
        )
    }
}