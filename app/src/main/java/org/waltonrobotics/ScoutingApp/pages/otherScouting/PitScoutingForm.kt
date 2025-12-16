package org.waltonrobotics.ScoutingApp.pages.otherScouting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import org.waltonrobotics.ScoutingApp.appNavigation.AppScreen
import org.waltonrobotics.ScoutingApp.helpers.SegmentedSelector
import org.waltonrobotics.ScoutingApp.helpers.TextFieldItem
import org.waltonrobotics.ScoutingApp.viewmodel.PitScoutingEvent
import org.waltonrobotics.ScoutingApp.viewmodel.PitScoutingViewModel


@Composable
fun PitScoutingForm(
    navController: NavController,
    vm: PitScoutingViewModel = viewModel()
) {
    val uiState by vm.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollState = rememberScrollState()

    LaunchedEffect(vm) {
        vm.events.collect { event ->
            when (event) {
                is PitScoutingEvent.ShowError ->
                    snackbarHostState.showSnackbar(event.message)
                is PitScoutingEvent.SubmitSuccess ->
                    snackbarHostState.showSnackbar("Submit successful")
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(scrollState)
        ) {
            Text("Pit Scouting Form", fontSize = 25.sp)
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
                label = "How much experience does the drive team have 1-5? (1 being bad, 5 being good)",
                options = listOf("1", "2", "3", "4", "5"),
                selectedIndex = uiState.driverExperienceIndex,
                onSelect = vm::setDriverExperience
            )

            TextFieldItem(
                value = uiState.autonsAvailable,
                onValueChange = vm::updateAvailableAutons,
                label = "What autons do they have?"
            )

            TextFieldItem(
                value = uiState.scoringFocus,
                onValueChange = vm::updateScoringFocus,
                label = "Do they focus on scoring in the Coral, the Barge, or the Processor"
            )

            TextFieldItem(
                value = uiState.piecesPerMatch,
                onValueChange = vm::updatePiecesPerMatch,
                label = "Pieces scored per match (in total)?"
            )

            SegmentedSelector(
                label = "Playstyle?",
                options = listOf("Y", "N"),
                selectedIndex = uiState.playStyleIndex,
                onSelect = vm::setPlaystyle
            )

            TextFieldItem(
                value = uiState.otherStrategies,
                onValueChange = vm::updateOtherStrategies,
                label = "Any other strategies or other points to note?"
            )

            TextFieldItem(
                value = uiState.robotWeight,
                onValueChange = vm::updateWeight,
                label = "Weight of Robot?"
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row {
                Button(onClick = vm::submit) {
                    Text("Submit")
                }
                var showConfirm by remember { mutableStateOf(false) }

                Button(
                    onClick = { showConfirm = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text("Cancel/Go back")
                }

                if (showConfirm) {
                    AlertDialog(
                        onDismissRequest = { showConfirm = false },
                        title = { Text("Discard changes?") },
                        text = { Text("Your changes will be lost.") },
                        confirmButton = {
                            Button(
                                onClick = {
                                    showConfirm = false
                                    navController.navigate(AppScreen.MainScreen.route) {
                                        popUpTo(AppScreen.MainScreen.route) { inclusive = true }
                                        launchSingleTop = true
                                    }
                                }
                            ) {
                                Text("Discard")
                            }
                        },
                        dismissButton = {
                            Button(onClick = { showConfirm = false }) {
                                Text("Keep editing")
                            }
                        }
                    )
                }

            }
        }
    }
}