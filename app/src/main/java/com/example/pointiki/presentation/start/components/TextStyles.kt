package com.example.pointiki.presentation.start.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.example.pointiki.R
import com.example.pointiki.presentation.utils.theme.PointikiTheme

@Composable
fun Title(
    text: String,
    fontSize: TextUnit = 40.sp,
    color: Color = Color.Black
) {
    Text(
        text = text,
        fontSize = fontSize,
        fontWeight = FontWeight.Bold,
        color = color
    )
}

@Composable
fun SmallText(
    text: String,
    color: Color = Color(0xFF343434)
) {
    Text(
        text = text,
        fontSize = 14.sp,
        fontFamily = FontFamily(fonts = listOf(Font(R.font.inter))),
        color = color
    )
}

@Preview
@Composable
private fun Title_Preview() {
    PointikiTheme {
        Title(text = "Test title")
    }
}

@Preview
@Composable
private fun SmallText_Preview() {
    PointikiTheme {
        SmallText(text = "Test title")
    }
}
