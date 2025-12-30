package org.waltonrobotics.ScoutingApp.helpers

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.waltonrobotics.ScoutingApp.R

@Composable
fun HybridCounter(
    label: String,
    value: String, // Take the String from state
    onValueChange: (String) -> Unit
) {
    // This local state allows the text field to be empty while typing
    var localText by remember(value) { mutableStateOf(value) }

    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(text = label, style = MaterialTheme.typography.titleMedium)

        Row(verticalAlignment = Alignment.CenterVertically) {
            // Decrement Button
            IconButton(
                onClick = {
                    val currentInt = value.toIntOrNull() ?: 0
                    onValueChange((currentInt - 1).coerceAtLeast(0).toString())
                },
                modifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp))
            ) {
                Icon(painterResource(R.drawable.remove), contentDescription = null)
            }

            OutlinedTextField(
                value = localText,
                onValueChange = { input ->
                    val filtered = input.filter { it.isDigit() }
                    localText = filtered // Update local UI immediately
                    onValueChange(filtered) // Send to ViewModel (even if empty)
                },
                modifier = Modifier.weight(1f).padding(horizontal = 8.dp),
                textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            // Increment Button
            IconButton(
                onClick = {
                    val currentInt = value.toIntOrNull() ?: 0
                    onValueChange((currentInt + 1).toString())
                },
                modifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp))
            ) {
                Icon(painterResource(R.drawable.add), contentDescription = null)
            }
        }
    }
}