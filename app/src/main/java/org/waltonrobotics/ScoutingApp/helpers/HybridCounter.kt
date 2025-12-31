package org.waltonrobotics.ScoutingApp.helpers

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.unit.dp
import org.waltonrobotics.ScoutingApp.R

@Composable
fun HybridCounter(
    label: String,
    value: String, // This is uiState.matchNumber (a String)
    onValueChange: (String) -> Unit
) {
    // This 'remember' block ensures the local text box stays
    // in sync with the ViewModel (e.g., when + or - is pressed)
    var localText by remember(value) { mutableStateOf(value) }

    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(text = label, style = MaterialTheme.typography.titleMedium)

        Row(verticalAlignment = Alignment.CenterVertically) {
            // Decrement
            IconButton(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primary),
                onClick = {
                    val currentInt = value.toIntOrNull() ?: 0
                    onValueChange((currentInt - 1).coerceAtLeast(0).toString())
                }
            ) {
                Icon(painterResource(R.drawable.remove), null)
            }

            OutlinedTextField(
                value = localText,
                onValueChange = { newText ->
                    val filtered = newText.filter { it.isDigit() }

                    // IMPORTANT: Update local state first
                    localText = filtered

                    // Send the string EXACTLY as it is (even if empty "")
                    // to the ViewModel.
                    onValueChange(filtered)
                },
                modifier = Modifier.weight(1f).padding(horizontal = 8.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )

            // Increment
            IconButton(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primary),
                onClick = {
                    val currentInt = value.toIntOrNull() ?: 0
                    onValueChange((currentInt + 1).toString())
                }

            ) {
                Icon(painterResource(R.drawable.add), null)
            }
        }
    }
}