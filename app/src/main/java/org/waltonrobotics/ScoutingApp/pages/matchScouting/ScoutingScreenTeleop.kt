package org.waltonrobotics.ScoutingApp.pages.matchScouting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.waltonrobotics.ScoutingApp.helpers.CounterItem
import org.waltonrobotics.ScoutingApp.helpers.DropdownSelector
import org.waltonrobotics.ScoutingApp.helpers.SegmentedSelector
import org.waltonrobotics.ScoutingApp.viewmodel.MatchScoutingViewModel

@Composable
fun ScoutingScreenTeleop(vm: MatchScoutingViewModel = viewModel()) {
    val uiState by vm.uiState.collectAsState()
    val scrollState = rememberScrollState()

    Column(modifier = Modifier
        .verticalScroll(scrollState)
        .padding(top = 24.dp, start = 16.dp, end = 16.dp)
    ) {
        CounterItem(
            label = "Processor TOTAL",
            value = uiState.teleopProcessor,
            onIncrement = { vm.incrementTeleopProcessor() },
            onDecrement = { vm.decrementTeleopProcessor() }
        )

        CounterItem(
            label = "NET TOTAL",
            value = uiState.teleopNet,
            onIncrement = { vm.incrementTeleopNet() },
            onDecrement = { vm.decrementTeleopNet() }
        )

        CounterItem(
            label = "L1 TOTAL",
            value = uiState.teleopL1,
            onIncrement = { vm.incrementTeleopL1() },
            onDecrement = { vm.decrementTeleopL1() }
        )

        CounterItem(
            label = "L2 TOTAL",
            value = uiState.teleopL2,
            onIncrement = { vm.incrementTeleopL2() },
            onDecrement = { vm.decrementTeleopL2() }
        )

        CounterItem(
            label = "L3 TOTAL",
            value = uiState.teleopL3,
            onIncrement = { vm.incrementTeleopL3() },
            onDecrement = { vm.decrementTeleopL3() }
        )

        CounterItem(
            label = "L4 TOTAL",
            value = uiState.teleopL4,
            onIncrement = { vm.incrementTeleopL4() },
            onDecrement = { vm.decrementTeleopL4() }
        )

        Spacer(modifier = Modifier.height(8.dp))

        SegmentedSelector(
            label = "Climb BARGE?",
            options = listOf("Yes", "No"),
            selectedIndex = uiState.climbBargeIndex,
            onSelect = { vm.setClimbBarge(it) }
        )

        DropdownSelector (
            label = "Climb DEEP/SHALLOW/N/A?",
            options = listOf("DEEP", "SHALLOW", "N/A"),
            selectedIndex = uiState.climbCageIndex,
            onSelect = { vm.setClimbCage(it) }
        )

        SegmentedSelector(
            label = "Park under BARGE?",
            options = listOf("Yes", "No"),
            selectedIndex = uiState.parkedBargeIndex,
            onSelect = { vm.setParkedBarge(it) }
        )
    }
}