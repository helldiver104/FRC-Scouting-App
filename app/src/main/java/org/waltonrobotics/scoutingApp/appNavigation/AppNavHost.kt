package org.waltonrobotics.scoutingApp.appNavigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.waltonrobotics.scoutingApp.pages.AccountScreen
import org.waltonrobotics.scoutingApp.pages.FAQScreen
import org.waltonrobotics.scoutingApp.pages.MainScreen
import org.waltonrobotics.scoutingApp.pages.auth.LoginScreen
import org.waltonrobotics.scoutingApp.pages.matchScouting.ScoutingScreen
import org.waltonrobotics.scoutingApp.pages.otherScouting.CycleTimeForm
import org.waltonrobotics.scoutingApp.pages.otherScouting.PitScoutingForm
import org.waltonrobotics.scoutingApp.pages.scheduleScreens.ScheduleScreen
import org.waltonrobotics.scoutingApp.viewmodel.MatchScoutingViewModel
import org.waltonrobotics.scoutingApp.viewmodel.PitScoutingViewModel
import org.waltonrobotics.scoutingApp.viewmodels.AuthViewModel
import org.waltonrobotics.scoutingApp.viewmodels.CycleTimeViewModel
import org.waltonrobotics.scoutingApp.viewmodels.ScheduleViewModel
import org.waltonrobotics.scoutingApp.viewmodels.ScouterViewModel

//--------------------------------
// NAVIGATION!!!!!!! bird.png
//--------------------------------
@Composable
fun AppNavHost(
    navController: NavHostController,
    snackbarState: SnackbarHostState,
    authVm: AuthViewModel,
    scoutingVm: MatchScoutingViewModel,
    pitScoutingVm: PitScoutingViewModel,
    cycleScoutingVm: CycleTimeViewModel,
    scouterSchedulevm: ScouterViewModel,
    scheduleVm: ScheduleViewModel,
    modifier: Modifier,
) {


    val startDest =
        if (authVm.currentUser == null) AppScreen.LoginScreen.route else AppScreen.MainScreen.route
    val email = authVm.currentUser?.email.toString()
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
            )
        }

//        // --- PIT SCOUTING ---
//        composable(AppScreen.Pit.PitScreen.route) {
//            PitScreen()
//        }

        // --- ACCOUNT ---
        composable(AppScreen.AccountScreen.route) {
            AccountScreen(
                email = authVm.currentUser?.email.toString(),
                name = scouterSchedulevm.getNameByEmail(email),
                viewmodel = authVm,
                scouterSchedulevm.isSudo(authVm.currentUser?.email.toString())
            )
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