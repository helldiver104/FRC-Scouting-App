package org.waltonrobotics.ScoutingApp.pages.matchScouting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import org.waltonrobotics.ScoutingApp.appNavigation.AppScreen
import org.waltonrobotics.ScoutingApp.helpers.CounterItem
import org.waltonrobotics.ScoutingApp.helpers.DropdownSelector
import org.waltonrobotics.ScoutingApp.helpers.SegmentedSelector
import org.waltonrobotics.ScoutingApp.helpers.TextFieldItem
import org.waltonrobotics.ScoutingApp.viewmodel.MatchScoutingViewModel

@Composable
fun ScoutingScreenClosingComments(
    navController: NavController,
    vm: MatchScoutingViewModel
) {
    val uiState by vm.uiState.collectAsState()
    var showConfirm by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(top = 24.dp, start = 16.dp, end = 16.dp)) {
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
        DropdownSelector(
            label = "Robot playstyle",
            listOf("Scorer", "Passer", "Defense", "Hybrid", "N/A"),
            selectedIndex = uiState.playstyleIndex,
            onSelect = { vm.setPlaystyle(it) })
        CounterItem(
            label = "Penalties",
            value = uiState.penalties,
            onIncrement = { vm.incrementPenalties() },
            onDecrement = { vm.decrementPenalties() })
        DropdownSelector(
            "Movement Skill 1-5",
            listOf("1", "2", "3", "4", "5"),
            selectedIndex = uiState.movementSkillIndex,
            onSelect = { vm.setMovementSkill(it) })
        SegmentedSelector(
            "ALGAE/CORAL stuck?",
            listOf("Yes", "No"),
            uiState.stuckPiecesIndex,
            onSelect = { vm.setStuckPieces(it) })
        TextFieldItem(
            value = uiState.otherComments,
            onValueChange = vm::updateOtherComments,
            label = "Any other comments?"
        )
        Spacer(Modifier.height(12.dp))
        Row {
            Button(onClick = vm::submit, modifier = Modifier.weight(1f)) {
                Text("Submit")
            }
            Spacer(Modifier.width(8.dp))
            Button(
                onClick = { showConfirm = true },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer
                )
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
                    Button(onClick = {
                        showConfirm = false
                        navController.navigate(AppScreen.MainScreen.route) {
                            popUpTo(AppScreen.MainScreen.route) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    }) {
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
