package org.waltonrobotics.ScoutingApp.pages.matchScouting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.waltonrobotics.ScoutingApp.helpers.CounterItem
import org.waltonrobotics.ScoutingApp.helpers.SegmentedSelector
import org.waltonrobotics.ScoutingApp.viewmodel.MatchScoutingViewModel

@Composable
fun ScoutingScreenTeleop(viewModel: MatchScoutingViewModel) {
    Column(Modifier.padding(top = 24.dp, start = 16.dp, end = 16.dp)) {
        CounterItem("Processor TOTAL", viewModel.teleopProcessor)
        CounterItem("NET TOTAL", viewModel.teleopNet)
        CounterItem("L1 TOTAL", viewModel.teleopL1)
        CounterItem("L2 TOTAL", viewModel.teleopL2)
        CounterItem("L3 TOTAL", viewModel.teleopL3)
        CounterItem("L4 TOTAL", viewModel.teleopL4)
        SegmentedSelector("Climb BARGE?", listOf("Yes", "No"), viewModel.climbBarge)
        SegmentedSelector(
            "Climb DEEP/SHALLOW/N/A?",
            listOf("DEEP", "SHALLOW", "N/A"),
            viewModel.climbCage
        )
        SegmentedSelector("Park under BARGE?", listOf("Yes", "No"), viewModel.parkedBarge)
    }
}
