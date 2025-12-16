package org.waltonrobotics.ScoutingApp.appNavigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.waltonrobotics.ScoutingApp.pages.AccountScreen
import org.waltonrobotics.ScoutingApp.pages.FAQScreen
import org.waltonrobotics.ScoutingApp.pages.MainScreen
import org.waltonrobotics.ScoutingApp.pages.ScheduleScreen
import org.waltonrobotics.ScoutingApp.pages.matchScouting.ScoutingScreen
import org.waltonrobotics.ScoutingApp.pages.otherScouting.CycleTimeForm
import org.waltonrobotics.ScoutingApp.pages.otherScouting.PitScoutingForm


//--------------------------------
// NAVIGATION!!!!!!! bird.jpg
//--------------------------------
@Composable
fun AppNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController,
        startDestination = AppScreen.MainScreen.route, // Using sealed class here
        modifier = modifier
    ) {
        composable(AppScreen.MainScreen.route) { MainScreen(navController) }
        composable(AppScreen.ScoutingScreen.route) { ScoutingScreen() }
        composable(AppScreen.ScheduleScreen.route) { ScheduleScreen() }
        composable(AppScreen.AccountScreen.route) { AccountScreen() }
        composable(AppScreen.PitScoutingForm.route) { PitScoutingForm(navController) }
        composable(AppScreen.CycleTimeForm.route) { CycleTimeForm(navController) }
        composable(AppScreen.FAQScreen.route) { FAQScreen() }
    }
}