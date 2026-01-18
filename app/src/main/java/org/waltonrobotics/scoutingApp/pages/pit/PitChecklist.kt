package org.waltonrobotics.scoutingApp.pages.pit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PitChecklist(){
    // i don't know the pit checklist either, please send...
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(16.dp)
    ) {
        Text(
            "Pit Checklist"
        )
        ChecklistItem("bonk")
        ChecklistItem("pit")
    }
}