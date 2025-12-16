package org.waltonrobotics.ScoutingApp.pages.matchScouting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.waltonrobotics.ScoutingApp.helpers.CounterItem
import org.waltonrobotics.ScoutingApp.helpers.SegmentedSelector
import org.waltonrobotics.ScoutingApp.helpers.TextFieldItem
import org.waltonrobotics.ScoutingApp.viewmodel.MatchScoutingViewModel

@Composable
fun ScoutingScreenClosingComments(vm: MatchScoutingViewModel = viewModel()) {
    val uiState by vm.uiState.collectAsState()

    Column(modifier = Modifier.padding(top = 24.dp, start = 16.dp, end = 16.dp)) {
        SegmentedSelector(
            label = "Robot playstyle",
            listOf("Scorer", "Passer", "Defense", "Hybrid", "N/A"),
            selectedIndex = uiState.playstyleIndex,
            onSelect = { vm.setPlaystyle(it) }
        )
        CounterItem(
            label = "Penalties",
            value = uiState.penalties,
            onIncrement = { vm.incrementPenalties() },
            onDecrement = { vm.decrementPenalties() }
        )
        SegmentedSelector(
            "Movement Skill 1-5",
            listOf("1", "2", "3", "4", "5"),
            selectedIndex = uiState.movementSkillIndex,
            onSelect = { vm.setMovementSkill(it) }
        )
        SegmentedSelector(
            "ALGAE/CORAL stuck?",
            listOf("Yes", "No"),
            uiState.stuckPiecesIndex,
            onSelect = { vm.setStuckPieces(it) }
        )
        TextFieldItem(
            value = uiState.otherComments,
            onValueChange = vm::updateOtherComments,
            label = "Any other comments?"
        )


        Spacer(Modifier.height(16.dp))
        Button(onClick = { /* Submit logic */ }) { Text("SUBMIT") }

    }
}
