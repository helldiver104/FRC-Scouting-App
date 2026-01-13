package org.waltonrobotics.ScoutingApp.appNavigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.waltonrobotics.ScoutingApp.pages.AccountScreen
import org.waltonrobotics.ScoutingApp.pages.FAQScreen
import org.waltonrobotics.ScoutingApp.pages.MainScreen
import org.waltonrobotics.ScoutingApp.pages.auth.LoginScreen
import org.waltonrobotics.ScoutingApp.pages.matchScouting.ScoutingScreen
import org.waltonrobotics.ScoutingApp.pages.otherScouting.CycleTimeForm
import org.waltonrobotics.ScoutingApp.pages.otherScouting.PitScoutingForm
import org.waltonrobotics.ScoutingApp.pages.pit.PitScreen
import org.waltonrobotics.ScoutingApp.pages.scheduleScreens.ScheduleScreen
import org.waltonrobotics.ScoutingApp.viewmodel.MatchScoutingViewModel
import org.waltonrobotics.ScoutingApp.viewmodel.PitScoutingViewModel
import org.waltonrobotics.ScoutingApp.viewmodels.AuthViewModel
import org.waltonrobotics.ScoutingApp.viewmodels.CycleTimeViewModel
import org.waltonrobotics.ScoutingApp.viewmodels.ScheduleViewModel
import org.waltonrobotics.ScoutingApp.viewmodels.ScouterViewModel

//--------------------------------
// NAVIGATION!!!!!!! bird.png
//--------------------------------
@Composable
fun AppNavHost(
    navController: NavHostController,
    snackbarState: SnackbarHostState,
    scoutingVm: MatchScoutingViewModel,
    pitScoutingVm: PitScoutingViewModel,
    cycleScoutingVm: CycleTimeViewModel,
    scouterSchedulevm: ScouterViewModel,
    scheduleVm: ScheduleViewModel,
    modifier: Modifier,
    contentPadding: PaddingValues
) {

    val authVm: AuthViewModel = viewModel()

    val startDest = if (authVm.currentUser == null) AppScreen.LoginScreen.route else AppScreen.MainScreen.route
    NavHost(
        navController = navController,
        startDestination = startDest
    ) {
        composable(AppScreen.LoginScreen.route) {
            LoginScreen(
                authVm = authVm,
                onLoginSuccess = {
                    navController.navigate(AppScreen.MainScreen.route) {
                        popUpTo(AppScreen.LoginScreen.route) { inclusive = true }
                    }
                }
            )
        }
        composable(AppScreen.MainScreen.route) {
            MainScreen(
                navController = navController,
                scoutingVm = scoutingVm,
                scouterScheduleVm = scouterSchedulevm,
                scheduleVm = scheduleVm,
                authVm = authVm,
                snackbarHostState = snackbarState
            )
        }
        composable(AppScreen.PitScoutingForm.route) {
            PitScoutingForm(
                snackbarHostState = snackbarState,
                navController,
                pitScoutingVm
            )
        }

        composable(AppScreen.CycleTimeForm.route) {
            CycleTimeForm(
                snackbarHostState = snackbarState,
                navController,
                cycleScoutingVm
            )
        }

        composable(AppScreen.FAQScreen.route) {
            FAQScreen(navController)
        }
        // --- SCHEDULE ---
        composable(AppScreen.ScheduleScreen.route) {
            ScheduleScreen(
                snackbarState = snackbarState,
                viewModel = scheduleVm,
                contentPadding = contentPadding
            )
        }

        // --- PIT SCOUTING ---
        composable(AppScreen.Pit.PitScreen.route) {
            PitScreen()
        }

        // --- ACCOUNT ---
        composable(AppScreen.AccountScreen.route) {
            AccountScreen(viewModel = authVm)
        }

        // --- MATCH SCOUTING ---
        composable(AppScreen.ScoutingScreen.route) {
            ScoutingScreen(
                viewModel = scoutingVm,
                navController = navController,
                snackbarHostState = snackbarState,
                modifier = modifier
            )
        }
    }
}