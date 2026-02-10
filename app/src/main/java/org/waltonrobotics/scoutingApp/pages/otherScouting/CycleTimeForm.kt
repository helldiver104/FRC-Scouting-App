package org.waltonrobotics.scoutingApp.pages.otherScouting

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import org.waltonrobotics.scoutingApp.helpers.HybridCounter
import org.waltonrobotics.scoutingApp.helpers.TextFieldItem
import org.waltonrobotics.scoutingApp.viewmodels.CycleTimeEvent
import org.waltonrobotics.scoutingApp.viewmodels.CycleTimeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CycleTimeForm(
    snackbarHostState: SnackbarHostState,
    navController: NavController, vm: CycleTimeViewModel
) {
    val uiState by vm.uiState.collectAsState()
    val scrollState = rememberScrollState()
    var showConfirm by remember { mutableStateOf(false) }

    BackHandler { showConfirm = true }

    LaunchedEffect(vm) {
        vm.events.collect { event ->
            when (event) {
                is CycleTimeEvent.ShowError -> snackbarHostState.showSnackbar(event.message)
                is CycleTimeEvent.SubmitSuccess -> {
                    snackbarHostState.showSnackbar("Submit successful")
                    navController.popBackStack()
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(Modifier.height(8.dp))
        Text("Cycle Time Scouting Form", fontSize = 25.sp, fontWeight = FontWeight.Bold)

        TextFieldItem(uiState.name, vm::updateName, "Name - First, Last")

        TextFieldItem(uiState.robotNumber, vm::updateRobotNumber, "Robot #", isNumber = true)

        HybridCounter(
            label = "Match #",
            value = (uiState.matchNumber.toIntOrNull() ?: 0).toString(),
            onValueChange = { newValue ->
                vm.updateMatchNumber(newValue)
            })

        HorizontalDivider()

        TextFieldItem(
            value = uiState.cycleTime,
            onValueChange = vm::updateCycleTime,
            label = "Cycle time",
            isNumber = true
        )

        TextFieldItem(uiState.otherComments, vm::updateOtherComments, "Other comments")

        Spacer(Modifier.height(24.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = vm::submit,
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Submit")
            }
            Spacer(Modifier.width(12.dp))
            OutlinedButton(
                onClick = { showConfirm = true },
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Cancel")
            }
        }
        Spacer(Modifier.height(40.dp))
    }

    if (showConfirm) {
        AlertDialog(
            onDismissRequest = { showConfirm = false },
            title = { Text("Discard changes?") },
            text = { Text("Your changes will be lost.") },
            confirmButton = {
                TextButton(onClick = {
                    showConfirm = false
                    navController.popBackStack()
                }) {
                    Text("Discard", color = MaterialTheme.colorScheme.error)
                }
            },

            dismissButton = {
                TextButton(onClick = { showConfirm = false }) {
                    Text("Keep editing", color = Color.White)
                }
            })
    }
}