package org.waltonrobotics.ScoutingApp.pages.matchScouting

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
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
data class ScoutingTab(
    val label: String, val iconRes: Int, val route: String, val contentDescription: String
)

@Composable
fun MatchScoutingTopNav(
    scoutingNavController: NavHostController,
    selectedTab: MutableState<Int>,
    useIcons: Boolean = true
) {
    val tabs = listOf(
        ScoutingTab("Start", R.drawable.start, AppScreen.Scouting.Start.route, "Pre-match screen"),
        ScoutingTab("Auton", R.drawable.robot, AppScreen.Scouting.Auton.route, "Auton screen"),
        ScoutingTab(
            "Teleop", R.drawable.controller, AppScreen.Scouting.Teleop.route, "Teleop screen"
        ),
        ScoutingTab(
            "Closing",
            R.drawable.stop,
            AppScreen.Scouting.ClosingComments.route,
            "Post-match screen"
        )
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
                }, colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedTab.value == index) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.inversePrimary
                )
            ) {
                if (useIcons) {
                    Icon(painterResource(tab.iconRes), tab.contentDescription)
                } else {
                    Text(tab.label)
                }
            }
        }
    }
}

@Composable
fun ScoutingScreenNavHost(
    snackbarHostState: SnackbarHostState,
    viewModel: MatchScoutingViewModel,
    navController: NavHostController, specialNavController: NavController
) {
    NavHost(navController, startDestination = AppScreen.Scouting.Start.route) {
        composable(AppScreen.Scouting.Start.route) { ScoutingScreenStart(snackbarHostState = snackbarHostState, viewModel) }
        composable(AppScreen.Scouting.Auton.route) { ScoutingScreenAuton(viewModel) }
        composable(AppScreen.Scouting.Teleop.route) { ScoutingScreenTeleop(viewModel) }
        composable(AppScreen.Scouting.ClosingComments.route) {
            ScoutingScreenClosingComments(specialNavController, viewModel)
        }
    }
}

@Composable
fun ScoutingScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    viewModel: MatchScoutingViewModel,
    modifier: Modifier = Modifier
) {

    val scoutingNavController = rememberNavController()
    val selectedTab = remember { mutableIntStateOf(0) }
    var showExitDialog by remember { mutableStateOf(false) }

    BackHandler(enabled = true) {
        showExitDialog = true
    }

    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = { Text("Exit Scouting?") },
            text = { Text("You will lose all unsaved data for this match.") },
            confirmButton = {
                TextButton(onClick = {
                    showExitDialog = false
                    navController.popBackStack()
                }) {
                    Text("Exit")
                }
            },
            dismissButton = {
                TextButton(onClick = { showExitDialog = false }) {
                    Text("Stay")
                }
            })
    }
    Column(Modifier.fillMaxSize()) {
        MatchScoutingTopNav(scoutingNavController, selectedTab)
        ScoutingScreenNavHost(snackbarHostState = snackbarHostState, viewModel, specialNavController = navController, navController = scoutingNavController)
    }
}
