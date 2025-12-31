package org.waltonrobotics.ScoutingApp.pages.matchScouting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import org.waltonrobotics.ScoutingApp.appNavigation.AppScreen
import org.waltonrobotics.ScoutingApp.helpers.DropdownSelector
import org.waltonrobotics.ScoutingApp.helpers.HybridCounter
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
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(16.dp) // Auto-spaces all items
    ) {
        Spacer(Modifier.height(8.dp))

        Text(
            "Closing Comments",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        // --- Climbing & Parking Section ---
        SegmentedSelector(
            label = "Climb BARGE?",
            options = listOf("Yes", "No"),
            selectedIndex = uiState.climbBargeIndex,
            onSelect = vm::setClimbBarge
        )

        DropdownSelector (
            label = "Climb Cage (DEEP/SHALLOW/N/A)?",
            options = listOf("DEEP", "SHALLOW", "N/A"),
            selectedIndex = uiState.climbCageIndex,
            onSelect = vm::setClimbCage
        )

        SegmentedSelector(
            label = "Park under BARGE?",
            options = listOf("Yes", "No"),
            selectedIndex = uiState.parkedBargeIndex,
            onSelect = vm::setParkedBarge
        )

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

        // --- Performance Section ---
        DropdownSelector(
            label = "Robot Playstyle",
            options = listOf("Scorer", "Passer", "Defense", "Hybrid", "N/A"),
            selectedIndex = uiState.playstyleIndex,
            onSelect = vm::setPlaystyle
        )

        // Replaced with HybridCounter to fix the "0" deletion issue
        HybridCounter(
            label = "Penalties",
            value = uiState.penalties,
            onValueChange = vm::updatePenalties
        )

        DropdownSelector(
            label = "Movement Skill (1-5)",
            options = listOf("1", "2", "3", "4", "5"),
            selectedIndex = uiState.movementSkillIndex,
            onSelect = vm::setMovementSkill
        )

        SegmentedSelector(
            label = "ALGAE/CORAL stuck?",
            options = listOf("Yes", "No"),
            selectedIndex = uiState.stuckPiecesIndex,
            onSelect = vm::setStuckPieces
        )

        // --- Final Notes ---
        TextFieldItem(
            value = uiState.otherComments,
            onValueChange = vm::updateOtherComments,
            label = "Any other comments?"
        )

        // --- Action Buttons ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = vm::submit,
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp), // Matching height
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text("Submit", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = { showConfirm = true },
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp), // Matching height
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer
                )
            ) {
                Text("Cancel", fontSize = 16.sp)
            }
        }
    }

    if (showConfirm) {
        AlertDialog(
            onDismissRequest = { showConfirm = false },
            title = { Text("Discard changes?") },
            text = { Text("Your changes will be lost.") },
            confirmButton = {
                TextButton(onClick = {
                    showConfirm = false
                    navController.navigate(AppScreen.MainScreen.route) {
                        popUpTo(AppScreen.MainScreen.route) { inclusive = true }
                    }
                }) {
                    Text("Discard", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirm = false }) {
                    Text("Keep editing", color = Color.White)
                }
            }
        )
    }
}