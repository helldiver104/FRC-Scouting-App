package org.waltonrobotics.ScoutingApp

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.waltonrobotics.ScoutingApp.appNavigation.AppNavHost
import org.waltonrobotics.ScoutingApp.appNavigation.AppScreen
import org.waltonrobotics.ScoutingApp.appNavigation.BottomNavBar
import org.waltonrobotics.ScoutingApp.viewmodel.MatchScoutingViewModel
import org.waltonrobotics.ScoutingApp.viewmodel.PitScoutingViewModel
import org.waltonrobotics.ScoutingApp.viewmodels.CycleTimeViewModel
import org.waltonrobotics.ScoutingApp.viewmodels.ScheduleViewModel

@Composable
fun App(navController: NavHostController = rememberNavController()) {
    // Shared ViewModels created at the top level
    val scoutingVm: MatchScoutingViewModel = viewModel()
    val scheduleVm: ScheduleViewModel = viewModel()
    val pitScoutingVm: PitScoutingViewModel = viewModel()
    val cycleScoutingVm: CycleTimeViewModel = viewModel()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            // Only show the bar on your primary tabs
            // This hides it during active Match or Pit scouting
            val bottomBarRoutes = listOf(
                AppScreen.MainScreen.route,
                AppScreen.ScheduleScreen.route,
                AppScreen.Pit.PitScreen.route,
                AppScreen.AccountScreen.route
            )

            if (currentRoute in bottomBarRoutes) {
                BottomNavBar(navController)
            }
        }
    ) { innerPadding ->
        // Use the innerPadding to prevent content from going under the nav bar
        AppNavHost(
            navController = navController,
            scoutingVm = scoutingVm,
            pitScoutingVm = pitScoutingVm,
            cycleScoutingVm = cycleScoutingVm,
            scheduleVm = scheduleVm,
            modifier = Modifier.padding(innerPadding)
        )
    }
}