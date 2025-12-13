package org.waltonrobotics.ScoutingApp.pages.otherScouting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.waltonrobotics.ScoutingApp.helpers.NumericalFieldItem
import org.waltonrobotics.ScoutingApp.helpers.SegmentedSelector
import org.waltonrobotics.ScoutingApp.helpers.TextFieldItem
import org.waltonrobotics.ScoutingApp.viewmodel.PitScoutingViewModel

@Composable
fun PitScoutingForm() {
    val viewModel: PitScoutingViewModel = viewModel()
    Column(
        Modifier
            .fillMaxHeight()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("Pit Scouting Form")
        TextFieldItem(viewModel.name, "Scouter's Name")
        // TODO add timestamps
        NumericalFieldItem(viewModel.teamNumber, "Team Number?")
        TextFieldItem(
            viewModel.driveExperience,
            "How much experience does your drive team have 1-5? (1 being bad, 5 being good)"
        )
        TextFieldItem(
            viewModel.autonsAvailable,
            "What autons do they have? (Leave, scoring, where do they score, their path, etc."
        )
        TextFieldItem(
            viewModel.scoringFocus,
            "Do they focus on scoring in the Coral, the Barge, or the Processor"
        )
        NumericalFieldItem(viewModel.piecesPerMatch, "Pieces scored per match (in total)?")
        SegmentedSelector("Playstyle?", listOf("N", "Y"), viewModel.playstyle)
        TextFieldItem(viewModel.otherStrategies, "Any other notable strategies or points to note?")
        NumericalFieldItem(viewModel.robotWeight, "Robot weight")
        //TODO ADD IMAGE INPUT
        Spacer(Modifier.height(16.dp))

        Button(onClick = { /* Submit logic */ }) { Text("SUBMIT") }
    }
}