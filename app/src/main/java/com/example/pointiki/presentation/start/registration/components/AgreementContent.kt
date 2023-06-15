package com.example.pointiki.presentation.start.registration.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pointiki.R
import com.example.pointiki.presentation.start.components.BackButton
import com.example.pointiki.presentation.start.components.MainButton
import com.example.pointiki.presentation.utils.theme.PointikiTheme
import com.example.pointiki.presentation.utils.theme.PrimaryColor
import com.example.pointiki.presentation.utils.theme.PrimaryGradient

@Composable
fun AgreementContent(
    modifier: Modifier = Modifier,
    onContinue: () -> Unit,
    onBackClick: () -> Unit,
) {
    BackHandler(onBack = onBackClick)
    Surface(color = PrimaryColor) {
        Box(modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 28.dp)) {
            Column {
                BackButton(onClick = onBackClick)
                Spacer(Modifier.height(30.dp))

                HeaderSection()
                Spacer(Modifier.height(40.dp))

                RulesSection()
            }
            
            MainButton(
                onClick = onContinue,
                text = stringResource(id = R.string.btn_agree),
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(vertical = 28.dp)
            )
        }
    }
}

@Composable
private fun RulesSection() {
    val paddingValue = 34.dp
    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        Rule(
            rule = stringResource(id = R.string.rule_1),
            explanation = stringResource(id = R.string.explanation_1)
        )
        Spacer(Modifier.height(paddingValue))
        Rule(
            rule = stringResource(id = R.string.rule_2),
            explanation = stringResource(id = R.string.explanation_2)
        )
        Spacer(Modifier.height(paddingValue))
        Rule(
            rule = stringResource(id = R.string.rule_3),
            explanation = stringResource(id = R.string.explanation_3)
        )
        Spacer(Modifier.height(paddingValue))
        Rule(
            rule = stringResource(id = R.string.rule_4),
            explanation = stringResource(id = R.string.explanation_4)
        )
        Spacer(Modifier.height(100.dp))
    }
}

@Composable
private fun Rule(
    rule: String,
    explanation: String,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            painter = painterResource(id = R.drawable.checkmark),
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
        Spacer(Modifier.width(7.dp))
        Text(
            text = rule,
            fontSize = 17.sp,
            fontFamily = FontFamily(Font(R.font.inter_semibold)),
            color = Color.Black
        )
    }
    Text(
        text = explanation,
        fontSize = 13.sp,
        fontFamily = FontFamily(Font(R.font.inter)),
        color = Color.Black
    )
}

@Composable
private fun HeaderSection() {
    Text(
        text = stringResource(id = R.string.agreement_title),
        fontSize = 23.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Black
    )
    Spacer(Modifier.height(5.dp))
    Text(
        text = stringResource(id = R.string.agreement_warning),
        fontSize = 17.sp,
        fontFamily = FontFamily(Font(R.font.inter)),
        color = Color.Black
    )
}

@Preview
@Composable
fun AgreementContent_Preview() {
    PointikiTheme {
        AgreementContent(
            onContinue = {},
            onBackClick = {}
        )
    }
}
