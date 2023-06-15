package com.example.pointiki.presentation.start.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.pointiki.R
import com.example.pointiki.presentation.utils.theme.PointikiTheme

@Composable
fun BackButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TextButton(
        onClick = onClick,
        colors = ButtonDefaults.textButtonColors(
            contentColor = Color.Black
        ),
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(id = R.drawable.arrow_back),
            contentDescription = null
        )
        Text(
            text = stringResource(id = R.string.btn_back),
            fontSize = 24.sp
        )
    }
}

@Composable
fun BackWithSkipButtons(onBackClick: () -> Unit, onSkip: () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        BackButton(onClick = onBackClick)
        SkipButton(
            onClick = onSkip,
            modifier = Modifier
        )
    }
}

@Composable
fun SkipButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TextButton(
        onClick = onClick,
        colors = ButtonDefaults.textButtonColors(
            contentColor = Color(0xFF585858)
        ),
        modifier = modifier
    ) {
        Text(
            text = stringResource(id = R.string.btn_skip),
            fontSize = 24.sp
        )
    }
}

@Preview
@Composable
private fun SkipButton_Preview() {
    PointikiTheme {
        SkipButton({})
    }
}

@Preview
@Composable
private fun BackButtonPreview() {
    PointikiTheme {
        BackButton(
            onClick = {}
        )
    }
}
