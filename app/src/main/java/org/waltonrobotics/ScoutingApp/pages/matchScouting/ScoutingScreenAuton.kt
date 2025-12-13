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
fun ScoutingScreenAuton(viewModel: MatchScoutingViewModel) {
    Column(Modifier.padding(top = 24.dp, start = 16.dp, end = 16.dp)) {
        SegmentedSelector("Left starting position?", listOf("Yes", "No"), viewModel.autonLeftStart)
        CounterItem("Processor Score", viewModel.autonProcessor)
        CounterItem("NET Score", viewModel.autonNet)
        CounterItem("L1 Score", viewModel.autonL1)
        CounterItem("L2 Score", viewModel.autonL2)
        CounterItem("L3 Score", viewModel.autonL3)
        CounterItem("L4 Score", viewModel.autonL4)
    }
}