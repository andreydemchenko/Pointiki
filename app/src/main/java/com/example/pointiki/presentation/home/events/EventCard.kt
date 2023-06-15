package com.example.pointiki.presentation.home.events

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.pointiki.models.EventModel
import com.example.pointiki.presentation.home.HomeViewModel
import com.example.pointiki.presentation.main.MainScreenNavigation
import com.example.pointiki.presentation.utils.theme.SecondaryColor
import com.example.pointiki.presentation.utils.theme.luckiestGuyFont

@Composable
fun EventCard(
    navController: NavController,
    event: EventModel,
    viewModel: HomeViewModel,
    includeDayInTime: Boolean
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.LightGray),
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clickable {
                viewModel.setSelectedEvent(event)
                navController.navigate(MainScreenNavigation.EventDetails.route)
            },
        border = BorderStroke(2.dp, SecondaryColor),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 4.dp),
                contentScale = ContentScale.Fit,
                painter = rememberAsyncImagePainter(event.imageUrl),
                contentDescription = null
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = event.title,
                    fontSize = 18.sp,
                    fontFamily = luckiestGuyFont,
                    color = Color.White,
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = if (includeDayInTime)
                        "${viewModel.formatDate(event.date)} at ${viewModel.formatTime(event.date)}"
                    else viewModel.formatTime(event.date),
                    fontSize = 16.sp,
                    fontFamily = luckiestGuyFont,
                    color = Color.Black
                )
            }
        }
    }
}