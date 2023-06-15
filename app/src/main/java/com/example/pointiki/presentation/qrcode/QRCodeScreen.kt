package com.example.pointiki.presentation.qrcode

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.pointiki.presentation.leaderboard.PointikiView
import com.example.pointiki.presentation.utils.theme.PointikiTheme
import com.example.pointiki.presentation.utils.theme.PrimaryColor
import com.example.pointiki.presentation.utils.theme.SecondaryColor
import com.example.pointiki.presentation.utils.theme.andikaFont
import com.example.pointiki.presentation.utils.theme.luckiestGuyFont

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QRCodeScreen() {
    val viewModel: QRCodeViewModel = hiltViewModel()
    val actionState by viewModel.actionStateListener.collectAsState()
    var shouldVisibleButton by remember { mutableStateOf(false) }

    viewModel.startScanning()

    Scaffold(containerColor = PrimaryColor) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            when (actionState) {
                is ActionState.ScanningCode -> {
                    ShowLoadingIndicator("Scanning")
                }
                is ActionState.EventVisitLoading -> {
                    ShowLoadingIndicator("Loading event")
                }
                is ActionState.EventLoading -> {
                    ShowLoadingIndicator("Loading event data")
                }
                is ActionState.CheckIfUserRegisteredStatusLoading -> {
                    ShowLoadingIndicator("Checking if already registered")
                }
                is ActionState.ParticipantSettingLoading -> {
                    ShowLoadingIndicator("Registering")
                }
                is ActionState.SettingPointsAndVisitLoading -> {
                    ShowLoadingIndicator("Setting points")
                }
                is ActionState.UUIDCheckFailed -> {
                    shouldVisibleButton = true
                    ShowError(message = "Scan only valid qr code")
                }
                is ActionState.CheckIfTimeIsOverdueStatusFailed -> {
                    shouldVisibleButton = true
                    ShowError(message = "The event is already passed")
                }
                is ActionState.CheckIfUserRegisteredStatusFailed -> {
                    shouldVisibleButton = false
                    ShowError(message = "You've already registered")
                }
                is ActionState.EventVisitLoadFailed -> {
                    shouldVisibleButton = true
                    ShowError(message = "Failed to load data")
                }
                is ActionState.EventLoadFailed -> {
                    shouldVisibleButton = true
                    ShowError(message = "Failed to load visit data")
                }
                is ActionState.ParticipantSettingFailed -> {
                    shouldVisibleButton = true
                    ShowError(message = "Failed to register user")
                }
                is ActionState.SettingPointsAndVisitFailed -> {
                    shouldVisibleButton = true
                    ShowError(message = "Failed to set points")
                }
                is ActionState.Error -> {
                    val error = (actionState as ActionState.Error).error
                    ShowError("Error: $error")
                    shouldVisibleButton = true
                }
                is ActionState.SettingPointsAndVisitComplete -> {
                    val event = (actionState as ActionState.SettingPointsAndVisitComplete).eventModel
                    Column(
                        modifier = Modifier
                            .padding(12.dp)
                            .align(Alignment.CenterHorizontally),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            text = "Successfully registered to \n${event.title}",
                            color = SecondaryColor,
                            fontSize = 20.sp,
                            fontFamily = andikaFont
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "You've got +${event.points}",
                                color = Color.Black,
                                fontSize = 16.sp,
                                fontFamily = luckiestGuyFont
                            )
                            PointikiView()
                        }
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
                else -> {
                    shouldVisibleButton = false
                }
            }

            if (shouldVisibleButton) {
                Spacer(modifier = Modifier.weight(1f))
                Button(onClick = { viewModel.startScanning() }) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(SecondaryColor)
                            .padding(horizontal = 4.dp)
                    ) {
                        Text(
                            text = "Scan again",
                            fontSize = 16.sp,
                            fontFamily = andikaFont,
                            color = Color.White
                        )
                    }
                }
                Spacer(modifier = Modifier.height(60.dp))
            }
        }
    }
}

@Composable
fun ShowLoadingIndicator(message: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.weight(1f))
        CircularProgressIndicator(
            modifier = Modifier.size(60.dp),
            color = SecondaryColor,
            strokeWidth = 3.dp
        )
        Text(
            text = message,
            color = SecondaryColor,
            fontSize = 20.sp,
            fontFamily = andikaFont
        )
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun ShowError(message: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = message,
            color = SecondaryColor,
            fontSize = 20.sp,
            fontFamily = andikaFont
        )
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Preview(showBackground = true)
@Composable
fun QRCodeScreenPreview() {
    PointikiTheme {
        QRCodeScreen()
    }
}