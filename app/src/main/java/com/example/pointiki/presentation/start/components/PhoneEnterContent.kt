package com.example.pointiki.presentation.start.components

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
import com.example.pointiki.models.CountryCode
import com.example.pointiki.presentation.utils.theme.PointikiTheme
import com.example.pointiki.presentation.utils.theme.PrimaryColor
import com.example.pointiki.utils.countryCodes

@Composable
fun PhoneEnterContent(
    modifier: Modifier = Modifier,
    shouldContinue: Boolean,
    onBackClick: () -> Unit,
    updatePhone: (String) -> Unit,
    updateCountryCode: (CountryCode) -> Unit,
    onContinue: () -> Unit,
) {
    BackHandler(onBack = onBackClick)
    Surface(color = PrimaryColor) {
        Box(
            modifier
                .fillMaxSize()
                .padding(horizontal = 28.dp)) {
            Column {
                BackButton(onClick = onBackClick)
                Spacer(Modifier.height(45.dp))

                Title(text = stringResource(id = R.string.phone_enter_title))
                Spacer(Modifier.height(40.dp))

                PhonePicker(
                    countryCodes = countryCodes,
                    onCountryChoose = updateCountryCode,
                    onTextChange = updatePhone
                )
                Spacer(Modifier.height(50.dp))

                SmallText(text = stringResource(id = R.string.phone_enter_description))
            }

            MainButton(
                onClick = { onContinue() },
                isEnabled = shouldContinue,
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
private fun PhoneEnterContent_Preview() {
    PointikiTheme {
        PhoneEnterContent(
            onBackClick = {},
            onContinue = {},
            updateCountryCode = {},
            updatePhone = {},
            shouldContinue = true
        )
    }
}
