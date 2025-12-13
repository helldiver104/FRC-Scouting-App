package org.waltonrobotics.ScoutingApp.helpers

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SegmentedSelector(label: String, options: List<String>, selectedState: MutableState<Int>) {
    Text(label)
    Row(modifier = Modifier.padding(vertical = 4.dp)) {
        options.forEachIndexed { index, text ->
            Button(
                onClick = { selectedState.value = index },
                colors = ButtonDefaults.buttonColors(
                    containerColor =
                        if (selectedState.value == index)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.secondary
                ),
                modifier = Modifier.padding(end = 4.dp)
            ) {
                Text(text, fontSize = 12.sp)
            }
        }
    }
}