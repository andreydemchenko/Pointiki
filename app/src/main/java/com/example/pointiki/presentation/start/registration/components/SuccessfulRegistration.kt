package com.example.pointiki.presentation.start.registration.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pointiki.R
import com.example.pointiki.presentation.start.components.BackButton
import com.example.pointiki.presentation.start.components.MainButton
import com.example.pointiki.presentation.start.components.Title
import com.example.pointiki.presentation.utils.theme.PointikiTheme
import com.example.pointiki.presentation.utils.theme.PrimaryColor


@Composable
fun SuccessfulRegistration(
    modifier: Modifier = Modifier,
    onContinue: () -> Unit,
    onBackClick: () -> Unit,
) {
    BackHandler(onBack = onBackClick)
    Surface(color = PrimaryColor) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 28.dp)
        ) {
            Column {
                BackButton(onClick = onBackClick)
                Spacer(modifier = Modifier.height(45.dp))
                Title(
                    text = stringResource(id = R.string.successful_registration),
                    fontSize = 40.sp
                )
            }

            MainButton(
                onClick = onContinue,
                text = stringResource(id = R.string.btn_continue),
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(vertical = 28.dp)
            )
        }
    }
}

@Preview
@Composable
fun SuccessfulRegistration_Preview() {
    PointikiTheme {
        SuccessfulRegistration(
            onContinue = {},
            onBackClick = {}
        )
    }
}
