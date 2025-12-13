package org.waltonrobotics.ScoutingApp.pages.matchScouting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import org.waltonrobotics.ScoutingApp.MatchScoutingTopNav
import org.waltonrobotics.ScoutingApp.ScoutingScreenNavHost
import org.waltonrobotics.ScoutingApp.viewmodel.MatchScoutingViewModel

@Composable
fun ScoutingScreen() {
    val viewModel: MatchScoutingViewModel = viewModel()
    val scoutingNavController = rememberNavController()
    val selectedTab = remember { mutableIntStateOf(0) }

    Column(Modifier.fillMaxSize()) {
        MatchScoutingTopNav(scoutingNavController, selectedTab)
        ScoutingScreenNavHost(viewModel, scoutingNavController)
    }
}
