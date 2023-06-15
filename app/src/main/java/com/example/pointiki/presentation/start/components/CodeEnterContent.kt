package com.example.pointiki.presentation.start.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pointiki.R
import com.example.pointiki.presentation.utils.theme.PointikiTheme
import com.example.pointiki.presentation.utils.theme.PrimaryColor

@ExperimentalMaterial3Api
@Composable
fun CodeEnterContent(
    number: String,
    code: String,
    updateCode: (String) -> Unit,
    onResend: () -> Unit,
    onContinue: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    shouldContinue: Boolean
) {
    BackHandler(onBack = onBackClick)
    Surface {
        Box(
            modifier
                .fillMaxSize()
                .padding(horizontal = 28.dp)
        ) {
            Column {
                BackButton(onClick = onBackClick)
                Spacer(Modifier.height(45.dp))

                Title(text = stringResource(id = R.string.code_enter_title))
                Spacer(Modifier.height(10.dp))
                NumberWithResendButton(number = number, onResendClick = onResend)
                Spacer(Modifier.height(13.dp))

                TextField(
                    value = code,
                    onValueChange = updateCode,
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    textStyle = TextStyle(
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Light,
                        color = Color(0xFF161616),
                        letterSpacing = 25.sp
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.Transparent
                    )
                )
            }


            MainButton(
                onClick = onContinue,
                text = stringResource(id = R.string.btn_continue),
                isEnabled = shouldContinue,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(vertical = 28.dp)
            )
        }
    }
}

@Composable
private fun NumberWithResendButton(
    number: String,
    onResendClick: () -> Unit
) {
    Row {
        Text(
            text = number,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f)
        )
        Spacer(Modifier.width(23.dp))
        Text(
            text = stringResource(id = R.string.btn_resend),
            fontSize = 16.sp,
            modifier = Modifier.clickable {
                onResendClick()
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun CodeEnterContent_Preview() {
    PointikiTheme {
        CodeEnterContent(
            number = "+79996915101",
            code = "123456",
            updateCode = {},
            onResend = {},
            onContinue = {},
            onBackClick = {},
            shouldContinue = true,
        )
    }
}