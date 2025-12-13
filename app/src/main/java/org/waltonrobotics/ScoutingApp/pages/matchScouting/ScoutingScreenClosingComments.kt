package org.waltonrobotics.ScoutingApp.pages.matchScouting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.waltonrobotics.ScoutingApp.CounterItem
import org.waltonrobotics.ScoutingApp.SegmentedSelector
import org.waltonrobotics.ScoutingApp.TextFieldItem
import org.waltonrobotics.ScoutingApp.viewmodel.MatchScoutingViewModel

@Composable
fun ScoutingScreenClosingComments(viewModel: MatchScoutingViewModel) {
    Column(Modifier.padding(top = 24.dp, start = 16.dp, end = 16.dp)) {
        SegmentedSelector(
            "Playstyle",
            listOf("Scorer", "Passer", "Defense", "Hybrid", "N/A"),
            viewModel.playstyle
        )
        CounterItem("Penalties", viewModel.penalties)
        SegmentedSelector(
            "Movement Skill 1-5",
            listOf("1", "2", "3", "4", "5"),
            viewModel.movementSkill
        )
        SegmentedSelector("ALGAE/CORAL stuck?", listOf("Yes", "No"), viewModel.stuckPieces)
        TextFieldItem(viewModel.otherComments, "Other Comments")
        Spacer(Modifier.height(16.dp))
        Button(onClick = { /* Submit logic */ }) { Text("SUBMIT") }
    }
}