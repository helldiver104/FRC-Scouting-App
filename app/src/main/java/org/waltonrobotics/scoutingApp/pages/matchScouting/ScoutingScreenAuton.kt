package org.waltonrobotics.scoutingApp.pages.matchScouting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.waltonrobotics.scoutingApp.helpers.CounterItem
import org.waltonrobotics.scoutingApp.helpers.SegmentedSelector
import org.waltonrobotics.scoutingApp.viewmodel.MatchScoutingViewModel

@Composable
fun ScoutingScreenAuton(vm: MatchScoutingViewModel) {
    val uiState by vm.uiState.collectAsState()

    Column(modifier = Modifier.padding(top = 24.dp, start = 16.dp, end = 16.dp)) {
        SegmentedSelector(
            label = "Did the robot leave the starting position?",
            options = listOf("Yes", "No"),
            selectedIndex = uiState.autonLeftStartIndex,
            onSelect = { vm.setAutonLeftStart(it) }
        )
        CounterItem(
            label = "Auton Processor",
            value = uiState.autonProcessor,
            onIncrement = { vm.incrementAutonProcessor() },
            onDecrement = { vm.decrementAutonProcessor() }
        )
        CounterItem(
            label = "Auton Net",
            value = uiState.autonNet,
            onIncrement = {vm.incrementAutonNet()},
            onDecrement = {vm.decrementAutonNet()}
        )
        CounterItem(
            label = "L1 Score",
            value = uiState.autonL1,
            onIncrement = { vm.incrementAutonL1() },
            onDecrement = { vm.decrementAutonL1() }
        )

        CounterItem(
            label = "L2 TOTAL",
            value = uiState.autonL2,
            onIncrement = { vm.incrementAutonL2() },
            onDecrement = { vm.decrementAutonL2() }
        )

        CounterItem(
            label = "L3 TOTAL",
            value = uiState.autonL3,
            onIncrement = { vm.incrementAutonL3() },
            onDecrement = { vm.decrementAutonL3() }
        )

        CounterItem(
            label = "L4 TOTAL",
            value = uiState.autonL4,
            onIncrement = { vm.incrementAutonL4() },
            onDecrement = { vm.decrementAutonL4() }
        )
    }
}