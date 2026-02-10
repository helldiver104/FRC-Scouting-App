package org.waltonrobotics.scoutingApp.pages.matchScouting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import org.waltonrobotics.scoutingApp.appNavigation.AppScreen
import org.waltonrobotics.scoutingApp.helpers.DropdownSelector
import org.waltonrobotics.scoutingApp.helpers.SegmentedSelector
import org.waltonrobotics.scoutingApp.helpers.TextFieldItem
import org.waltonrobotics.scoutingApp.viewmodel.MatchScoutingViewModel

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
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        SegmentedSelector(
            label = "Climb?",
            options = listOf("Yes", "No"),
            selectedIndex = uiState.climbEndIndex,
            onSelect = vm::setClimb
        )

        DropdownSelector(
            label = "Climb Height?",
            options = listOf("L1", "L2", "L3", "Attempted", "N/A"),
            selectedIndex = uiState.climbHeightIndex,
            onSelect = vm::setClimbHeight
        )

        DropdownSelector(
            label = "Time to climb",
            options = listOf("5", "10", "15", "N/A"),
            selectedIndex = uiState.climbTimeIndex,
            onSelect = vm::setClimbTime
        )

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

        // --- Performance Section ---
        DropdownSelector(
            label = "Shoot on the move",
            options = listOf("1", "2", "3", "4", "5"),
            selectedIndex = uiState.shootOnMoveIndex,
            onSelect = vm::setShootOnMove
        )

        DropdownSelector(
            label = "Intake on the move",
            options = listOf("1", "2", "3", "4", "5"),
            selectedIndex = uiState.intakeOnMoveIndex,
            onSelect = vm::setIntakeOnMove
        )

        DropdownSelector(
            label = "Intake speed",
            options = listOf("1", "2", "3", "4", "5"),
            selectedIndex = uiState.intakeSpeedIndex,
            onSelect = vm::setIntakeSpeed
        )

        DropdownSelector(
            label = "Defense ability",
            options = listOf("1", "2", "3", "4", "5"),
            selectedIndex = uiState.defenseAbilityIndex,
            onSelect = vm::setDefenseAbility
        )

        DropdownSelector(
            label = "Drivers",
            options = listOf("1", "2", "3", "4", "5"),
            selectedIndex = uiState.driverSkillIndex,
            onSelect = vm::setDriverSkill
        )

        // --- Final Notes ---
        TextFieldItem(
            value = uiState.otherComments,
            onValueChange = vm::updateOtherComments,
            label = "Comments? (passing, etc.)"
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
                    .height(56.dp),
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
                    .height(56.dp),
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
                    vm.resetForm()
                    navController.navigate(AppScreen.MainScreen.route) {
                        popUpTo(AppScreen.MainScreen.route) { inclusive = true }
                    }
                }) {
                    Text("Discard", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirm = false }) {
                    Text("Keep editing", color = MaterialTheme.colorScheme.onSurface)
                }
            }
        )
    }
}