package org.waltonrobotics.scoutingApp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.waltonrobotics.scoutingApp.appNavigation.AppNavHost
import org.waltonrobotics.scoutingApp.appNavigation.AppScreen
import org.waltonrobotics.scoutingApp.appNavigation.BottomNavBar
import org.waltonrobotics.scoutingApp.viewmodel.MatchScoutingViewModel
import org.waltonrobotics.scoutingApp.viewmodel.PitScoutingViewModel
import org.waltonrobotics.scoutingApp.viewmodels.AuthViewModel
import org.waltonrobotics.scoutingApp.viewmodels.CycleTimeViewModel
import org.waltonrobotics.scoutingApp.viewmodels.ScheduleViewModel
import org.waltonrobotics.scoutingApp.viewmodels.ScouterViewModel

@Composable
fun App(navController: NavHostController = rememberNavController()) {
    val authVm: AuthViewModel = viewModel()
    val scoutingVm: MatchScoutingViewModel = viewModel()
    val scheduleVm: ScheduleViewModel = viewModel()
    val pitScoutingVm: PitScoutingViewModel = viewModel()
    val cycleScoutingVm: CycleTimeViewModel = viewModel()
    val scouterScheduleVm: ScouterViewModel = viewModel()
    val snackbarHostState = remember { SnackbarHostState() }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val notBottomBarScreens = listOf(AppScreen.ScoutingScreen.route, AppScreen.PitScoutingForm.route, AppScreen.CycleTimeForm.route, AppScreen.LoginScreen.route)

    Scaffold(
        bottomBar = {
            if (currentRoute !in notBottomBarScreens) {
                BottomNavBar(navController)
            }
        },
        snackbarHost = { snackbarHostState },
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.surface
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding)
        ){
            AppNavHost(
                navController = navController,
                snackbarState = snackbarHostState,
                scoutingVm = scoutingVm,
                pitScoutingVm = pitScoutingVm,
                cycleScoutingVm = cycleScoutingVm,
                scheduleVm = scheduleVm,
                scouterSchedulevm = scouterScheduleVm,
                modifier = Modifier.fillMaxSize(),
                authVm = authVm,
                contentPadding = innerPadding,
            )
        }

    }
}