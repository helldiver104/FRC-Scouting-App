package org.waltonrobotics.ScoutingApp.helpers

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun NumericalFieldItem(state: MutableState<String>, label: String) {
    OutlinedTextField(
        value = state.value,
        onValueChange = { if (it.isEmpty() || it.all { ch -> ch.isDigit() }) state.value = it },
        label = { Text(label) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    )
}