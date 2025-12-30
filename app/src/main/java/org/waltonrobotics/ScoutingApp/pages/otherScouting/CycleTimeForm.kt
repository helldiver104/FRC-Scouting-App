package org.waltonrobotics.ScoutingApp.pages.otherScouting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.navigation.NavController
import org.waltonrobotics.ScoutingApp.appNavigation.AppScreen
import org.waltonrobotics.ScoutingApp.helpers.TextFieldItem
import org.waltonrobotics.ScoutingApp.viewmodels.CycleTimeEvent
import org.waltonrobotics.ScoutingApp.viewmodels.CycleTimeViewModel

@Composable
fun CycleTimeForm(
    navController: NavController,
    vm: CycleTimeViewModel
) {
    val uiState by vm.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollState = rememberScrollState()
    var showConfirm by remember { mutableStateOf(false) }

    LaunchedEffect(vm) {
        vm.events.collect { event ->
            when (event) {
                is CycleTimeEvent.ShowError -> snackbarHostState.showSnackbar(event.message)
                is CycleTimeEvent.SubmitSuccess -> snackbarHostState.showSnackbar("Submit successful")
            }
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(scrollState)
        ) {
            Text("Cycle Time Scouting Form", fontSize = 25.sp)
            TextFieldItem(uiState.name, vm::updateName, "Name - First, Last")
            Spacer(Modifier.height(10.dp))
            TextFieldItem(uiState.robotNumber, vm::updateRobotNumber, "Robot #")
            Spacer(Modifier.height(10.dp))
            TextFieldItem(uiState.matchNumber, vm::updateMatchNumber, "Match #")
            Spacer(Modifier.height(10.dp))
            TextFieldItem(uiState.cycleTime, vm::updateCycleTime, "Cycle time")
            Spacer(Modifier.height(10.dp))
            TextFieldItem(uiState.otherComments, vm::updateOtherComments, "Other comments")
            Spacer(Modifier.height(20.dp))

            Row {
                Button(
                    onClick = vm::submit,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Submit")
                }
                Spacer(Modifier.width(8.dp))
                Button(
                    onClick = { showConfirm = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.onErrorContainer
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cancel")
                }
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
