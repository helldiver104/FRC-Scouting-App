package org.waltonrobotics.scoutingApp.helpers

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.waltonrobotics.scoutingApp.R

@Composable
fun HybridCounter(
    label: String,
    value: String, // Assume this comes in as "0" initially
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    // If the value is "0", we show an empty string so the placeholder appears
    var localText by remember(value) {
        mutableStateOf(if (value == "0") "" else value)
    }

    Column(modifier = modifier.padding(vertical = 4.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(start = 4.dp, bottom = 2.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Decrement
            CounterAction(icon = R.drawable.remove, contentDescription = "Decrease") {
                val current = value.toIntOrNull() ?: 0
                val next = (current - 1).coerceAtLeast(0).toString()
                onValueChange(next)
            }

            OutlinedTextField(
                value = localText,
                onValueChange = { input ->
                    // 1. Only allow digits
                    val filtered = input.filter { it.isDigit() }

                    // 2. Update local UI state
                    localText = filtered

                    // 3. Report back to ViewModel (if empty, report "0")
                    onValueChange(filtered.ifEmpty { "0" })
                },
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp)
                    .padding(horizontal = 8.dp),
                // The "Ghost" Zero
                placeholder = {
                    Text(
                        text = "0",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                    )
                },
                textStyle = LocalTextStyle.current.copy(
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            // Increment
            CounterAction(icon = R.drawable.add, contentDescription = "Increase") {
                val current = value.toIntOrNull() ?: 0
                val next = (current + 1).toString()
                onValueChange(next)
            }
        }
    }
}

@Composable
private fun CounterAction(
    icon: Int,
    contentDescription: String,
    onClick: () -> Unit
) {
    FilledIconButton(
        onClick = onClick,
        modifier = Modifier.size(48.dp),
        shape = RoundedCornerShape(12.dp),
        colors = IconButtonDefaults.filledIconButtonColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = contentDescription,
            modifier = Modifier.size(24.dp)
        )
    }
}