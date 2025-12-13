package org.waltonrobotics.ScoutingApp.pages.matchScouting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.waltonrobotics.ScoutingApp.R
import org.waltonrobotics.ScoutingApp.appNavigation.AppScreen
import org.waltonrobotics.ScoutingApp.viewmodel.MatchScoutingViewModel


//--------------------------------
// MATCH SCOUTING
//--------------------------------
data class ScoutingTab(val label: String, val iconRes: Int, val route: String)

@Composable
fun MatchScoutingTopNav(
    scoutingNavController: NavHostController,
    selectedTab: MutableState<Int>,
    useIcons: Boolean = true
) {
    val tabs = listOf(
        ScoutingTab("Start", R.drawable.start, AppScreen.Scouting.Start.route),
        ScoutingTab("Auton", R.drawable.robot, AppScreen.Scouting.Auton.route),
        ScoutingTab("Teleop", R.drawable.controller, AppScreen.Scouting.Teleop.route),
        ScoutingTab("Closing", R.drawable.stop, AppScreen.Scouting.ClosingComments.route)
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        tabs.forEachIndexed { index, tab ->
            Button(
                onClick = {
                    selectedTab.value = index
                    scoutingNavController.navigate(tab.route) { launchSingleTop = true }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor =
                        if (selectedTab.value == index)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.secondary
                )
            ) {
                if (useIcons) {
                    Icon(painterResource(tab.iconRes), null)
                } else {
                    Text(tab.label)
                }
            }
        }
    }
}

@Composable
fun ScoutingScreenNavHost(viewModel: MatchScoutingViewModel, navController: NavHostController) {
    NavHost(navController, startDestination = AppScreen.Scouting.Start.route) {
        composable(AppScreen.Scouting.Start.route) { ScoutingScreenStart(viewModel) }
        composable(AppScreen.Scouting.Auton.route) { ScoutingScreenAuton(viewModel) }
        composable(AppScreen.Scouting.Teleop.route) { ScoutingScreenTeleop(viewModel) }
        composable(AppScreen.Scouting.ClosingComments.route) {
            ScoutingScreenClosingComments(
                viewModel
            )
        }
    }
}

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
