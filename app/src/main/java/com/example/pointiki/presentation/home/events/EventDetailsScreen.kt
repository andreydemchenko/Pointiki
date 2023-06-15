package com.example.pointiki.presentation.home.events

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.pointiki.R
import com.example.pointiki.models.EventModel
import com.example.pointiki.presentation.home.HomeViewModel
import com.example.pointiki.presentation.leaderboard.PointikiView
import com.example.pointiki.presentation.utils.theme.PointikiTheme
import com.example.pointiki.presentation.utils.theme.SecondaryColor
import com.example.pointiki.presentation.utils.theme.SecondaryLightColor
import com.example.pointiki.presentation.utils.theme.andikaFont
import com.example.pointiki.presentation.utils.theme.luckiestGuyFont
import com.example.pointiki.utils.extensions.toDurationString
import java.util.Date
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailsScreen(
    navController: NavController,
    viewModel: HomeViewModel,
    event: EventModel
) {
    Scaffold(containerColor = SecondaryLightColor) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Button(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape),
                    contentPadding = PaddingValues(0.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SecondaryColor),
                    onClick = {
                        navController.popBackStack()
                        viewModel.setSelectedEvent(null)
                    }) {

                    Image(
                        modifier = Modifier
                            .width(40.dp)
                            .height(32.dp),
                        painter = painterResource(id = R.drawable.back_button_icon),
                        contentDescription = null
                    )
                }
                Text(
                    modifier = Modifier.padding(start = 16.dp),
                    text = event.title,
                    fontSize = 30.sp,
                    fontFamily = luckiestGuyFont,
                    color = Color.White,
                )
                Spacer(modifier = Modifier.weight(1f))
            }
            Image(
                contentScale = ContentScale.Fit,
                painter = rememberAsyncImagePainter(event.imageUrl),
                contentDescription = null
            )
            Row {
                Text(
                    text =
                    event.duration.toDurationString(),
                    fontSize = 16.sp,
                    fontFamily = luckiestGuyFont,
                    color = Color.LightGray
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text =
                    "${viewModel.formatDate(event.date)} at ${viewModel.formatTime(event.date)}",
                    fontSize = 16.sp,
                    fontFamily = luckiestGuyFont,
                    color = Color.LightGray
                )
            }
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = event.description,
                    fontSize = 22.sp,
                    fontFamily = andikaFont,
                    color = Color.Black
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Row(
                modifier = Modifier.padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "If you visit, you'll get",
                    fontSize = 18.sp,
                    fontFamily = luckiestGuyFont,
                    color = Color.White
                )
                Text(
                    text = "${event.points}",
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

@Preview(showBackground = true)
@Composable
fun EventDetailsScreenPreview() {
    PointikiTheme {
        EventDetailsScreen(
            navController = rememberNavController(),
            viewModel = hiltViewModel(),
            event =
            EventModel(
                UUID.randomUUID(),
                "Name",
                "Some description",
                150,
                130,
                Date(),
                ""
            )
        )
    }
}