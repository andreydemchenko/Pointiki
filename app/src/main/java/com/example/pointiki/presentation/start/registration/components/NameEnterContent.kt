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
import com.example.pointiki.R
import com.example.pointiki.presentation.start.components.BackButton
import com.example.pointiki.presentation.start.components.InputField
import com.example.pointiki.presentation.start.components.MainButton
import com.example.pointiki.presentation.start.components.SmallText
import com.example.pointiki.presentation.start.components.Title
import com.example.pointiki.presentation.utils.theme.PointikiTheme
import com.example.pointiki.presentation.utils.theme.PrimaryColor

@Composable
fun NameEnterContent(
    name: String,
    shouldContinue: Boolean,
    onNameChange: (String) -> Unit,
    onContinue: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler(onBack = onBackClick)
    Surface(color = PrimaryColor) {
        Box(modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 28.dp)
        ) {
            Column {
                BackButton(onClick = onBackClick)
                Spacer(modifier = Modifier.height(45.dp))

                Title(text = stringResource(id = R.string.enter_name_title))
                SmallText(text = stringResource(id = R.string.enter_name_warning))
                Spacer(Modifier.height(73.dp))

                InputField(
                    value = name,
                    onValueChange = onNameChange,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = stringResource(id = R.string.enter_name_hint)
                )
            }

            MainButton(
                onClick = onContinue,
                text = stringResource(id = R.string.btn_continue),
                isEnabled = shouldContinue,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 28.dp)
            )
        }
    }
}

@Preview
@Composable
private fun NameEnterContent_Preview() {
    PointikiTheme {
        NameEnterContent(
            name = "Kira",
            onNameChange = {},
            onContinue = {},
            onBackClick = {},
            shouldContinue = true
        )
    }
}
