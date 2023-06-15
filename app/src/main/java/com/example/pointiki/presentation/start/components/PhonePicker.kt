package com.example.pointiki.presentation.start.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.pointiki.models.CountryCode
import com.example.pointiki.presentation.utils.theme.PointikiTheme
import com.example.pointiki.utils.countryCodes

@Composable
fun PhonePicker(
    countryCodes: List<CountryCode>,
    onCountryChoose: (CountryCode) -> Unit,
    onTextChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var phoneWithoutCode by remember { mutableStateOf("") }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        CountryCodePicker(
            countryCodes = countryCodes,
            onChoose = onCountryChoose,
        )

        PhoneInputField(
            text = phoneWithoutCode,
            onTextChange = {
                phoneWithoutCode = it
                onTextChange(it)
            }
        )
    }
}

@Composable
private fun CountryCodePicker(
    countryCodes: List<CountryCode>,
    onChoose: (CountryCode) -> Unit,
){
    var expanded by remember { mutableStateOf(false) }
    var selectedCode by remember { mutableStateOf(countryCodes[0]) }

    Box {
        TextButton(
            onClick = { expanded = true },
        ) {
            Text(
                text = "${selectedCode.country} ${selectedCode.code}",
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                tint = MaterialTheme.colorScheme.onSurface,
                contentDescription = null
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            for (item in countryCodes) {
                DropdownMenuItem(
                    onClick = {
                        selectedCode = item
                        expanded = false
                        onChoose(selectedCode)
                    },
                    text = {
                        Text(text = "${item.country} ${item.code}")
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PhoneInputField(
    text: String,
    onTextChange: (String) -> Unit
) {
    TextField(
        value = text,
        textStyle = TextStyle(
            fontSize = 24.sp
        ),
        onValueChange = {
            onTextChange(it)
        },
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color.Transparent,
            focusedIndicatorColor = Color.Black,
            unfocusedIndicatorColor = Color.Black
        ),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
    )
}

@Preview
@Composable
private fun PhonePicker_Preview() {
    PointikiTheme {
        PhonePicker(countryCodes, {}, {})
    }
}
