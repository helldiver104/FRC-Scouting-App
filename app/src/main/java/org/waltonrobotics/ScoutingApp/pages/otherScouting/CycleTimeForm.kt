package org.waltonrobotics.ScoutingApp.pages.otherScouting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.waltonrobotics.ScoutingApp.helpers.NumericalFieldItem
import org.waltonrobotics.ScoutingApp.helpers.TextFieldItem
import org.waltonrobotics.ScoutingApp.viewmodel.CycleTimeViewModel

@Composable
fun CycleTimeForm() {
    val viewModel: CycleTimeViewModel = viewModel()
    Column(Modifier.padding(16.dp)) {
        Text("Cycle Time Form")
        //TODO add timestamps
        TextFieldItem(viewModel.name, "Name - First, Last (Ex. Derek Spevak)")
        NumericalFieldItem(viewModel.robotNumber, "Robot # (Ex. 2974)")
        NumericalFieldItem(viewModel.matchNumber, "Match #")
        TextFieldItem(viewModel.cycleTime, "Cycle Time")
        TextFieldItem(
            viewModel.otherComments,
            "Other comments (Were their cycle times inconsistent)?"
        )
        Spacer(Modifier.height(16.dp))
        Button(onClick = { /* Submit logic */ }) { Text("SUBMIT") }
    }
}