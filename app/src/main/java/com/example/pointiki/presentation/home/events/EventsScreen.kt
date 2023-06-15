package com.example.pointiki.presentation.home.events

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.pointiki.R
import com.example.pointiki.presentation.home.HomeViewModel
import com.example.pointiki.presentation.utils.theme.PointikiTheme
import com.example.pointiki.presentation.utils.theme.PrimaryColor
import com.example.pointiki.presentation.utils.theme.SecondaryColor
import com.example.pointiki.presentation.utils.theme.SecondaryLightColor
import com.example.pointiki.presentation.utils.theme.luckiestGuyFont

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventsScreen(
    navController: NavController,
    viewModel: HomeViewModel
) {

    val eventsGroupedByDate by viewModel.getEventsGroupedByDate().collectAsState(initial = emptyMap())

    Scaffold(containerColor = PrimaryColor) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(SecondaryLightColor)
                .padding(16.dp)
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
                    onClick = {
                        navController.popBackStack()
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
                    modifier = Modifier.shadow(16.dp, spotColor = Color.White),
                    text = "Upcoming events".uppercase(),
                    fontFamily = luckiestGuyFont,
                    fontSize = 24.sp,
                    color = SecondaryColor,
                    maxLines = 1
                )
            }
            Column(
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                eventsGroupedByDate.forEach { (date, events) ->
                    Text(
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .padding(horizontal = 8.dp),
                        text = viewModel.formatDate(date),
                        fontFamily = luckiestGuyFont,
                        fontSize = 18.sp,
                        color = SecondaryColor,
                        maxLines = 1
                    )
                    events.forEach { event ->
                        EventCard(
                            navController = navController,
                            event = event,
                            viewModel = viewModel,
                            includeDayInTime = false
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EventsScreenPreview() {
    PointikiTheme {
        EventsScreen(navController = rememberNavController(), viewModel = hiltViewModel())
    }
}