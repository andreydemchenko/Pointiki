package com.example.pointiki.presentation.start.registration.components

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.pointiki.R
import com.example.pointiki.presentation.start.components.BackWithSkipButtons
import com.example.pointiki.presentation.start.components.MainButton
import com.example.pointiki.presentation.start.components.Title
import com.example.pointiki.presentation.utils.theme.PointikiTheme
import com.example.pointiki.presentation.utils.theme.PrimaryColor
import com.example.pointiki.presentation.utils.theme.PrimaryGradient
import com.example.pointiki.presentation.utils.theme.SecondaryColor

@Composable
fun PhotoChooseContent(
    modifier: Modifier = Modifier,
    photoUri: Uri?,
    onPhotoSelected: (Uri?) -> Unit,
    onRemovePhoto: () -> Unit,
    shouldContinue: Boolean,
    onBackClick: () -> Unit,
    onSkipClick: () -> Unit,
    onContinue: () -> Unit,
) {
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia(),
        onPhotoSelected
    )

    BackHandler(onBack = onBackClick)

    Surface(color = PrimaryColor) {
        Box(modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 28.dp)
        ) {
            Column {
                BackWithSkipButtons(
                    onBackClick = onBackClick,
                    onSkip = onSkipClick
                )
                Spacer(Modifier.height(45.dp))

                Title(text = stringResource(id = R.string.photo_select_title))
                Spacer(modifier = Modifier.height(67.dp))
                PhotoSelectButton(
                    onSelect = {
                        launcher.launch(
                            PickVisualMediaRequest(
                                ActivityResultContracts.PickVisualMedia.ImageOnly
                            )
                        )
                    },
                    modifier = Modifier.align(CenterHorizontally),
                    onRemovePhoto = onRemovePhoto,
                    photoUri = photoUri
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
private fun PhotoSelectButton(
    modifier: Modifier = Modifier,
    photoUri: Uri?,
    onSelect: () -> Unit,
    onRemovePhoto: () -> Unit,
) {
    if (photoUri != null) {
        Image(
            painter = painterResource(id = R.drawable.remove_button),
            contentDescription = null,
            modifier = Modifier
                .size(50.dp)
                .offset(x = 243.dp, y = (-15).dp)
                .background(Color.White, shape = CircleShape)
                .zIndex(1f)
                .clickable { onRemovePhoto() }
        )
    }
    Box(
        contentAlignment = Center,
        modifier = modifier
            .height(333.dp)
            .width(243.dp)
            .offset(y = if (photoUri != null) (-50).dp else 0.dp)
            .clip(MaterialTheme.shapes.extraLarge)
            .border(
                border = BorderStroke(3.dp, Brush.linearGradient(PrimaryGradient)),
                shape = MaterialTheme.shapes.extraLarge
            )
            .clickable { onSelect() }
    ) {
        if (photoUri == null) {
            ChoosePhotoIcon()
        } else {
            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest
                        .Builder(LocalContext.current)
                        .data(data = photoUri)
                        .build()
                ),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
            )
        }
    }
}

@Composable
private fun ChoosePhotoIcon() {
    Icon(
        painter = painterResource(id = R.drawable.image_add),
        contentDescription = null,
        modifier = Modifier
            .graphicsLayer(alpha = 0.99f)
            .drawWithCache {
                onDrawWithContent {
                    drawContent()
                    drawRect(
                        Brush.linearGradient(PrimaryGradient),
                        blendMode = BlendMode.SrcAtop
                    )
                }
            }
    )
}

@Preview
@Composable
fun PhotoChooseContent_Preview() {
    PointikiTheme {
        PhotoChooseContent(
            onBackClick = {},
            onSkipClick = {},
            onContinue = {},
            onPhotoSelected = {},
            onRemovePhoto = {},
            shouldContinue = true,
            photoUri = Uri.EMPTY,
        )
    }
}
