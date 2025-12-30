package org.waltonrobotics.ScoutingApp.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.waltonrobotics.ScoutingApp.viewmodel.MatchScoutingViewModel

@Composable
fun AccountScreen(
    viewModel: MatchScoutingViewModel, // Pass the scouting VM here
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Account / Admin Screen")
    }
}