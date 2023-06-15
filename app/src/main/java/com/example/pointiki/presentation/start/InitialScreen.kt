package com.example.pointiki.presentation.start

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.pointiki.presentation.utils.theme.SecondaryColor

@Composable
fun InitialScreen(navController: NavController) {
    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.weight(1f))
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(30.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(SecondaryColor),
                onClick = {
                    navController.navigate(WelcomeScreenNavigation.Registration.route)
                }
            ) {
                Text(
                    text = "Register".uppercase(),
                    fontSize = 20.sp,
                    color = Color.White
                )
            }
            Button(
                colors = ButtonDefaults.buttonColors(Color.Transparent),
                onClick = {
                    navController.navigate(WelcomeScreenNavigation.Login.route)
                }
            ) {
                Text(
                    text = "Log In".uppercase(),
                    fontSize = 20.sp,
                    color = SecondaryColor
                )
            }
        }
    }
}