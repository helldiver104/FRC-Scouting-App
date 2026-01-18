package org.waltonrobotics.scoutingApp.pages.matchScouting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.waltonrobotics.scoutingApp.helpers.DropdownSelector
import org.waltonrobotics.scoutingApp.helpers.SegmentedSelector
import org.waltonrobotics.scoutingApp.helpers.TextFieldItem
import org.waltonrobotics.scoutingApp.viewmodel.MatchScoutingEvent
import org.waltonrobotics.scoutingApp.viewmodel.MatchScoutingViewModel

@Composable
fun ScoutingScreenStart(
    snackbarHostState: SnackbarHostState, vm: MatchScoutingViewModel
) {
    val uiState by vm.uiState.collectAsState()

    LaunchedEffect(vm) {
        vm.events.collect { event ->
            when (event) {
                is MatchScoutingEvent.ShowError -> snackbarHostState.showSnackbar(event.message)
                is MatchScoutingEvent.SubmitSuccess -> snackbarHostState.showSnackbar("Submit successful")
            }
        }
    }
    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        TextFieldItem(
            value = uiState.name,
            onValueChange = vm::updateName,
            label = "Name - First Last (ex. Carson Beck)"
        )

        Spacer(Modifier.height(8.dp))

        TextFieldItem(
            value = uiState.robotNumber,
            onValueChange = vm::updateRobotNumber,
            label = "Robot # (ex. 2974)",
            isNumber = true
        )

        Spacer(Modifier.height(8.dp))

        TextFieldItem(
            value = uiState.matchNumber,
            onValueChange = vm::updateMatchNumber,
            label = "Match #",
            isNumber = true
        )

        Spacer(Modifier.height(16.dp))

        SegmentedSelector(
            label = "Did the robot show up?",
            options = listOf("Yes", "No"),
            selectedIndex = uiState.robotShowedUpIndex,
            onSelect = { vm.setRobotShowedUp(it) })

        Spacer(Modifier.height(16.dp))

        DropdownSelector(
            label = "What was the initial starting position of the robot?",
            options = listOf("Far Side", "Middle", "Near Side", "Close Side", "N/A"),
            selectedIndex = uiState.startPositionIndex,
            onSelect = { vm.setStartPosition(it) })

        // TODO autofill stuff
    }
}
