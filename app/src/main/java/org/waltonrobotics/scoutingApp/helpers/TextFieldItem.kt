package org.waltonrobotics.scoutingApp.helpers

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TextFieldItem(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isNumber: Boolean = false // Add this parameter
) {
    OutlinedTextField(
        value = value,
        onValueChange = { newValue ->
            // If isNumber is true, we prevent non-digit characters from being typed
            if (isNumber) {
                if (newValue.all { it.isDigit() || it == '.' } || newValue.isEmpty()) {
                    onValueChange(newValue)
                }
            } else {
                onValueChange(newValue)
            }
        },
        label = { Text(label, fontSize = 14.sp) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        // This line pulls up the number pad instead of the full keyboard
        keyboardOptions = KeyboardOptions(
            keyboardType = if (isNumber) KeyboardType.Decimal else KeyboardType.Text
        ),
        singleLine = true
    )
}