package org.waltonrobotics.ScoutingApp.appNavigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.waltonrobotics.ScoutingApp.pages.AccountScreen
import org.waltonrobotics.ScoutingApp.pages.FAQScreen
import org.waltonrobotics.ScoutingApp.pages.MainScreen
import org.waltonrobotics.ScoutingApp.pages.matchScouting.ScoutingScreen
import org.waltonrobotics.ScoutingApp.pages.otherScouting.CycleTimeForm
import org.waltonrobotics.ScoutingApp.pages.otherScouting.PitScoutingForm
import org.waltonrobotics.ScoutingApp.pages.pit.PitScreen
import org.waltonrobotics.ScoutingApp.pages.scheduleScreens.ScheduleScreen

//--------------------------------
// NAVIGATION!!!!!!! bird.png
//--------------------------------
@Composable
fun AppNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController,
        startDestination = AppScreen.MainScreen.route,
        modifier = modifier
    ) {
        composable(AppScreen.MainScreen.route) { MainScreen(navController) }
        composable(AppScreen.ScoutingScreen.route) { ScoutingScreen(navController) }
        composable(AppScreen.ScheduleScreen.route) { ScheduleScreen() }
        composable(AppScreen.Pit.PitScreen.route){ PitScreen() }
        composable(AppScreen.AccountScreen.route) { AccountScreen() }
        composable(AppScreen.PitScoutingForm.route) { PitScoutingForm(navController) }
        composable(AppScreen.CycleTimeForm.route) { CycleTimeForm(navController) }
        composable(AppScreen.FAQScreen.route) { FAQScreen(navController) }
    }
}