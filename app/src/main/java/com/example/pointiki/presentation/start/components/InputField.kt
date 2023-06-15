package com.example.pointiki.presentation.start.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.pointiki.R
import com.example.pointiki.presentation.utils.theme.PointikiTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        textStyle = TextStyle(
            fontFamily = FontFamily(Font(R.font.inter)),
            fontSize = 23.sp,
        ),
        placeholder = {
            Text(
                text = placeholder,
                fontFamily = FontFamily(Font(R.font.inter)),
                fontSize = 23.sp,
                color = Color(0xFF343434).copy(alpha = 0.26f)
            )
        },
        singleLine = true,
        keyboardOptions = keyboardOptions,
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color.Transparent,
            focusedIndicatorColor = Color.Black,
            unfocusedIndicatorColor = Color.Black
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun InputField_Entered_Preview() {
    PointikiTheme {
        InputField(value = "123456789", onValueChange = {}, placeholder = "")
    }
}

@Preview(showBackground = true)
@Composable
private fun InputField_Placeholder_Preview() {
    PointikiTheme {
        InputField(value = "", onValueChange = {}, placeholder = "1234567890")
    }
}
