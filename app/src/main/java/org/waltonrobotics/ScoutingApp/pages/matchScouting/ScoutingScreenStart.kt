package org.waltonrobotics.ScoutingApp.pages.matchScouting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.waltonrobotics.ScoutingApp.helpers.DropdownSelector
import org.waltonrobotics.ScoutingApp.helpers.SegmentedSelector
import org.waltonrobotics.ScoutingApp.helpers.TextFieldItem
import org.waltonrobotics.ScoutingApp.viewmodel.MatchScoutingEvent
import org.waltonrobotics.ScoutingApp.viewmodel.MatchScoutingViewModel

@Composable
fun ScoutingScreenStart(vm: MatchScoutingViewModel = viewModel()) {
    val uiState by vm.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(vm) {
        vm.events.collect { event ->
            when (event) {
                is MatchScoutingEvent.ShowError -> snackbarHostState.showSnackbar(event.message)
                is MatchScoutingEvent.SubmitSuccess -> snackbarHostState.showSnackbar("Submit successful")
            }
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).padding(16.dp)) {
            TextFieldItem(
                value = uiState.name,
                onValueChange = vm::updateName,
                label = "Name - First Last (ex. Carson Beck)"
            )

            TextFieldItem(
                value = uiState.robotNumber,
                onValueChange = vm::updateRobotNumber,
                label = "Robot # (ex. 2974)"
            )

            TextFieldItem(
                value = uiState.matchNumber,
                onValueChange = vm::updateMatchNumber,
                label = "Match #"
            )

            SegmentedSelector(
                label = "Did the robot show up?",
                options = listOf("Yes", "No"),
                selectedIndex = uiState.robotShowedUpIndex,
                onSelect = { vm.setRobotShowedUp(it) }
            )
            DropdownSelector (
                label = "What was the inital starting position of the robot?",
                options = listOf("Far Side", "Middle", "Near Side", "Close Side", "N/A"),
                selectedIndex = uiState.startPositionIndex,
                onSelect = { vm.setStartPosition(it) }
            )
        }
    }
}