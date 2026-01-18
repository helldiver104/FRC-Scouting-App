package org.waltonrobotics.scoutingApp.pages.matchScouting

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
import org.waltonrobotics.scoutingApp.helpers.CounterItem
import org.waltonrobotics.scoutingApp.viewmodel.MatchScoutingViewModel

@Composable
fun ScoutingScreenTeleop(vm: MatchScoutingViewModel) {
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


    }
}