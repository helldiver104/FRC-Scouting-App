package org.waltonrobotics.ScoutingApp.appNavigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.waltonrobotics.ScoutingApp.pages.AccountScreen
import org.waltonrobotics.ScoutingApp.pages.AlliancePosition
import org.waltonrobotics.ScoutingApp.pages.FAQScreen
import org.waltonrobotics.ScoutingApp.pages.MainScreen
import org.waltonrobotics.ScoutingApp.pages.matchScouting.ScoutingScreen
import org.waltonrobotics.ScoutingApp.pages.otherScouting.CycleTimeForm
import org.waltonrobotics.ScoutingApp.pages.otherScouting.PitScoutingForm
import org.waltonrobotics.ScoutingApp.pages.pit.PitScreen
import org.waltonrobotics.ScoutingApp.pages.scheduleScreens.ScheduleScreen
import org.waltonrobotics.ScoutingApp.schedule.Match
import org.waltonrobotics.ScoutingApp.viewmodel.MatchScoutingViewModel
import org.waltonrobotics.ScoutingApp.viewmodel.PitScoutingViewModel
import org.waltonrobotics.ScoutingApp.viewmodels.CycleTimeViewModel
import org.waltonrobotics.ScoutingApp.viewmodels.ScheduleViewModel

//--------------------------------
// NAVIGATION!!!!!!! bird.png
//--------------------------------
@Composable
fun AppNavHost(
    navController: NavHostController,
    scoutingVm: MatchScoutingViewModel,
    pitScoutingVm: PitScoutingViewModel,
    cycleScoutingVm: CycleTimeViewModel,
    scheduleVm: ScheduleViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = AppScreen.MainScreen.route,
        modifier = modifier
    ) {
        // --- HOME / DASHBOARD ---
        composable(AppScreen.MainScreen.route) {
            val dummyMatches = listOf(
                Match(matchNumber = "12", redAlliance = listOf("2974", "9999", "8888"), blueAlliance = listOf("1111", "2222", "3333"))
            )

            MainScreen(
                navController = navController,
                scoutingVm = scoutingVm,
                dummyMatches = dummyMatches,
                // Change to AlliancePosition.NONE to test your snackbar error
                dummyPosition = AlliancePosition.RED_1
            )
        }
        composable(AppScreen.PitScoutingForm.route) {
            PitScoutingForm(navController, pitScoutingVm)
        }

        composable(AppScreen.CycleTimeForm.route) {
            CycleTimeForm(navController, cycleScoutingVm)
        }

        composable(AppScreen.FAQScreen.route) {
            FAQScreen(navController)
        }
        // --- SCHEDULE ---
        composable(AppScreen.ScheduleScreen.route) {
            ScheduleScreen(viewModel = scheduleVm)
        }

        // --- PIT SCOUTING ---
        composable(AppScreen.Pit.PitScreen.route) {
            PitScreen() // Replace with your actual Pit screen
        }

        // --- ACCOUNT ---
        composable(AppScreen.AccountScreen.route) {
            AccountScreen(viewModel = scoutingVm)
        }

        // --- MATCH SCOUTING FLOW ---
        // (Note: This is NOT in the bottom bar, accessed via "Start Scouting")
        composable(AppScreen.ScoutingScreen.route) {
            ScoutingScreen(
                viewModel = scoutingVm,
                navController = navController
            )
        }
    }
}