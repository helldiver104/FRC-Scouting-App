package org.waltonrobotics.ScoutingApp.pages.otherScouting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.waltonrobotics.ScoutingApp.helpers.TextFieldItem
import org.waltonrobotics.ScoutingApp.viewmodels.CycleTimeEvent
import org.waltonrobotics.ScoutingApp.viewmodels.CycleTimeViewModel

@Composable
fun CycleTimeForm(vm: CycleTimeViewModel = viewModel()) {
    val uiState by vm.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(vm) {
        vm.events.collect { event ->
            when (event) {
                is CycleTimeEvent.ShowError -> snackbarHostState.showSnackbar(event.message)
                is CycleTimeEvent.SubmitSuccess -> snackbarHostState.showSnackbar("Submit successful")
            }
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { innerPadding ->
        Column(modifier = Modifier
            .padding(innerPadding)
            .padding(16.dp)) {
            TextFieldItem(
                value = uiState.name,
                onValueChange = vm::updateName,
                label = "Name - First, Last (Ex. Derek Spevak)"
            )

            TextFieldItem(
                value = uiState.robotNumber,
                onValueChange = vm::updateRobotNumber,
                label = "Robot # (Ex. 2974)"
            )

            TextFieldItem(
                value = uiState.matchNumber,
                onValueChange = vm::updateMatchNumber,
                label = "Match # (Ex. 7)"
            )

            TextFieldItem(
                value = uiState.cycleTime,
                onValueChange = vm::updateCycleTime,
                label="Cycle time (Time it takes between picking up a piece and scoring)"
            )

            TextFieldItem(
                value = uiState.otherComments,
                onValueChange = vm::updateOtherComments,
                label = "Other comments (were the cycle times inconsistent?)"
                )
            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = { vm.submit() }) {
                Text("Submit")
            }
        }
    }
}