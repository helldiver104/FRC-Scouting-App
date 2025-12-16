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
import org.waltonrobotics.ScoutingApp.helpers.SegmentedSelector
import org.waltonrobotics.ScoutingApp.helpers.TextFieldItem
import org.waltonrobotics.ScoutingApp.viewmodel.PitScoutingEvent
import org.waltonrobotics.ScoutingApp.viewmodel.PitScoutingViewModel


@Composable
fun PitScoutingForm(vm: PitScoutingViewModel = viewModel()) {
    val uiState by vm.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(vm) {
        vm.events.collect { event ->
            when (event) {
                is PitScoutingEvent.ShowError -> snackbarHostState.showSnackbar(event.message)
                is PitScoutingEvent.SubmitSuccess -> snackbarHostState.showSnackbar("Submit successful")
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) {
        innerPadding ->
        Column(modifier = Modifier
            .padding(innerPadding)
            .padding(horizontal=16.dp)
        ) {
            TextFieldItem(
                value = uiState.name,
                onValueChange = vm::updateName,
                label = "Scouter's Name"
            )

            TextFieldItem(
                value = uiState.robotNumber,
                onValueChange = vm::updateRobotNumber,
                label = "Team Number?"
            )

            SegmentedSelector(
                label = "How much experience does your drive team have 1-5? (1 being bad, 5 being good)",
                options = listOf("1", "2", "3", "4", "5"),
                selectedIndex = uiState.driverExperienceIndex,
                onSelect = { vm.setDriverExperience(it) }
            )

            TextFieldItem(
                value = uiState.autonsAvailable,
                onValueChange = vm::updateAvailableAutons,
                label = "What autons do you have? (Leave, scoring, where do they score, their path, etc."
            )
            TextFieldItem(
                value = uiState.scoringFocus,
                onValueChange = vm::updateScoringFocus,
                label="Do you focus on scoring in the Coral, the Barge, or the Processor"
            )
            TextFieldItem(
                value = uiState.piecesPerMatch,
                onValueChange = vm::updatePiecesPerMatch,
                label="Pieces scored per match (in total)?"
            )
            SegmentedSelector(
                label = "Playstyle?",
                options = listOf("Y", "N"),
                selectedIndex = uiState.playStyleIndex,
                onSelect = { vm.setPlaystyle(it) }
            )
            TextFieldItem(
                value = uiState.otherStrategies,
                onValueChange = vm::updateOtherStrategies,
                label="Any other strategies or other points to note?"
            )
            TextFieldItem(
                value = uiState.robotWeight,
                onValueChange = vm::updateWeight,
                label="Weight of Robot?"
            )
            // TODO ADD IMAGE

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = { vm.submit() }) {
                Text("Submit")
            }
        }
    }
}