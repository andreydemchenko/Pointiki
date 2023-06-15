package com.example.pointiki.presentation.start.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pointiki.presentation.utils.theme.PointikiTheme
import com.example.pointiki.presentation.utils.theme.PrimaryGradient

@Composable
fun MainButton(
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    text: String,
    onClick: () -> Unit,
) {
    GradientButton(
        onClick = onClick,
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
            .height(61.dp),
        text = text,
        gradient = Brush.linearGradient(PrimaryGradient),
        isEnabled = isEnabled
    )
}

@Composable
fun GradientButton(
    text: String,
    gradient: Brush,
    isEnabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val actualGradient = if (isEnabled) gradient
        else Brush.linearGradient(listOf(Color(0xFFCECECE), Color(0xFF878787)))

    Button(
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = Color.White,
            disabledContentColor = Color.White
        ),
        enabled = isEnabled,
        contentPadding = PaddingValues(),
        onClick = { onClick() },
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(actualGradient),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                fontSize = 21.sp,
                fontWeight = FontWeight(600)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MainButton_Preview() {
    PointikiTheme {
        MainButton(
            onClick = {},
            text = "Text",
            modifier = Modifier.fillMaxWidth(),
            isEnabled = false
        )
    }
}
