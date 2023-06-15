package com.example.pointiki.presentation.utils.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Dp

@Composable
fun BlackAndWhiteImage(
    painter: Painter,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    size: Dp
) {
    val grayscaleMatrix = ColorMatrix(
        floatArrayOf(
            0.33f, 0.33f, 0.33f, 0f, 0f,
            0.33f, 0.33f, 0.33f, 0f, 0f,
            0.33f, 0.33f, 0.33f, 0f, 0f,
            0f, 0f, 0f, 1f, 0f
        )
    )

    val colorFilter = ColorFilter.colorMatrix(grayscaleMatrix)

    Image(
        painter = painter,
        contentDescription = contentDescription,
        modifier = modifier
            .size(size),
        colorFilter = colorFilter
    )
}