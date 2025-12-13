package org.waltonrobotics.ScoutingApp.pages.matchScouting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.waltonrobotics.ScoutingApp.NumericalFieldItem
import org.waltonrobotics.ScoutingApp.SegmentedSelector
import org.waltonrobotics.ScoutingApp.TextFieldItem
import org.waltonrobotics.ScoutingApp.viewmodel.MatchScoutingViewModel

@Composable
fun ScoutingScreenStart(viewModel: MatchScoutingViewModel) {
    Column(
        Modifier
            .fillMaxHeight()
            .padding(top = 24.dp, start = 16.dp, end = 16.dp)
    ) {
        // TODO ADD TIMESTAMP
        TextFieldItem(viewModel.name, "Name - First Last (Ex. Carson Beck)")
        NumericalFieldItem(viewModel.robotNumber, "Robot # (Ex. 2974)")
        NumericalFieldItem(viewModel.matchNumber, "Match #")
        SegmentedSelector("Did the robot show up?", listOf("Yes", "No"), viewModel.robotShowedUp)
        SegmentedSelector(
            "Starting position?",
            listOf("Far Side", "Middle", "Close Side", "N/A"),
            viewModel.startPosition
        )
    }
}