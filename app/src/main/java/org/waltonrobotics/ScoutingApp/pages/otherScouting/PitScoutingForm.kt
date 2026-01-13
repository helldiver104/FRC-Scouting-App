package org.waltonrobotics.ScoutingApp.pages.otherScouting

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextFieldDefaults.contentPadding
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
import org.waltonrobotics.ScoutingApp.helpers.HybridCounter
import org.waltonrobotics.ScoutingApp.helpers.ImagePickerItem
import org.waltonrobotics.ScoutingApp.helpers.SegmentedSelector
import org.waltonrobotics.ScoutingApp.helpers.TextFieldItem
import org.waltonrobotics.ScoutingApp.viewmodel.PitScoutingEvent
import org.waltonrobotics.ScoutingApp.viewmodel.PitScoutingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PitScoutingForm(
    snackbarHostState: SnackbarHostState,
    navController: NavController, vm: PitScoutingViewModel
) {
    val uiState by vm.uiState.collectAsState()
    val scrollState = rememberScrollState()
    var showConfirm by remember { mutableStateOf(false) }

    BackHandler { showConfirm = true }

    LaunchedEffect(vm) {
        vm.events.collect { event ->
            when (event) {
                is PitScoutingEvent.ShowError -> snackbarHostState.showSnackbar(event.message)
                is PitScoutingEvent.SubmitSuccess -> {
                    snackbarHostState.showSnackbar("Submit successful")
                    navController.popBackStack()
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .verticalScroll(scrollState), verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(Modifier.height(8.dp))
        Text("Pit Scouting Form", fontSize = 25.sp, fontWeight = FontWeight.Bold)

        // --- Scouter & Team ---
        TextFieldItem(uiState.name, vm::updateName, "Scouter's Name")
        TextFieldItem(uiState.robotNumber, vm::updateRobotNumber, "Team Number?")

        // --- DRIVE EXP (Now Hybrid) ---
        HybridCounter(
            label = "Driver Experience (1-5)",
            // Ensure this is accessing the Int from your state
            value = uiState.driverExperience.toString(), onValueChange = { newValue ->
                // Convert the String back to Int for the ViewModel
                val intValue = newValue.toIntOrNull() ?: 1
                vm.setDriverExperience(intValue)
            })

        // --- Autons & Focus ---
        TextFieldItem(
            uiState.autonsAvailable, vm::updateAvailableAutons, "What autons do they have?"
        )
        TextFieldItem(
            uiState.scoringFocus,
            vm::updateScoringFocus,
            "Do they focus on scoring in the Coral, the Barge, or the Processor"
        )

        // --- Pieces (Hybrid) ---
        HybridCounter(
            label = "Pieces scored per match (in total)?",
            value = (uiState.piecesPerMatch.toIntOrNull() ?: 0).toString(),
            onValueChange = { vm.updatePiecesPerMatch(it) })

        // --- Playstyle & Weight ---
        SegmentedSelector(
            label = "Playstyle?",
            options = listOf("Y", "N"),
            selectedIndex = uiState.playStyleIndex,
            onSelect = vm::setPlaystyle
        )

        TextFieldItem(
            uiState.otherStrategies,
            vm::updateOtherStrategies,
            "Any other strategies or other points to note?"
        )
        TextFieldItem(uiState.robotWeight, vm::updateWeight, "Weight of Robot?")
        ImagePickerItem(
            label = "Robot Photo",
            imageUri = uiState.robotPhotoUri,
            onImagePicked = vm::updatePhoto // Make sure this is in your ViewModel
        )
        Spacer(Modifier.height(24.dp))

        // --- Actions ---
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
                TextButton(
                    onClick = {
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