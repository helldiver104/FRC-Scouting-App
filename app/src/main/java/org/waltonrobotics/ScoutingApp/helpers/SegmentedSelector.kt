package org.waltonrobotics.ScoutingApp.helpers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
@Composable
fun SegmentedSelector(
    label: String,
    options: List<String>,
    selectedIndex: Int?,
    onSelect: (Int) -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium, // Larger, clearer label
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp), // Fixed height for consistent touch targets
            horizontalArrangement = Arrangement.spacedBy(2.dp) // Tight spacing for "Segmented" look
        ) {
            options.forEachIndexed { index, text ->
                val isSelected = selectedIndex == index

                Button(
                    onClick = { onSelect(index) },
                    shape = when (index) {
                        0 -> RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp) // Left edge
                        options.lastIndex -> RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp) // Right edge
                        else -> RectangleShape // Middle items are flat
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSelected)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = if (isSelected)
                            MaterialTheme.colorScheme.onPrimary
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(0.dp) // Maximize text space
                ) {
                    Text(
                        text = text,
                        style = MaterialTheme.typography.labelLarge,
                        maxLines = 1
                    )
                }
            }
        }
    }
}