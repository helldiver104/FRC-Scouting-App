package org.waltonrobotics.ScoutingApp.pages.scheduleScreens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.waltonrobotics.ScoutingApp.R
import org.waltonrobotics.ScoutingApp.appNavigation.AppScreen
import org.waltonrobotics.ScoutingApp.schedule.Match
import org.waltonrobotics.ScoutingApp.viewmodels.ScheduleViewModel

//--------------------------------
// TOP TABS
//--------------------------------
data class ScheduleTab(
    val label: String, val iconRes: Int, val route: String
)

@Composable
fun ScheduleNav(
    navController: NavHostController
) {
    val tabs = listOf(
        ScheduleTab(
            "Comp Schedule", R.drawable.calendar_clock, AppScreen.Schedule.CompSchedule.route
        ), ScheduleTab(
            "Our Schedule", R.drawable.calendar_check, AppScreen.Schedule.OurSchedule.route
        )
    )

    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    NavigationBar(containerColor = Color.Transparent) {
        tabs.forEach { tab ->
            NavigationBarItem(selected = currentRoute == tab.route, onClick = {
                navController.navigate(tab.route) {
                    launchSingleTop = true
                    restoreState = true
                }
            }, icon = {
                Icon(
                    painter = painterResource(tab.iconRes), contentDescription = tab.label
                )
            }, label = { Text(tab.label) })
        }
    }
}

//--------------------------------
// NAV HOST
//--------------------------------
@Composable
fun ScheduleScreenNavHost(
    contentPadding: PaddingValues,
    navController: NavHostController, matches: List<Match>
) {
    NavHost(
        navController = navController, startDestination = AppScreen.Schedule.CompSchedule.route
    ) {
        composable(AppScreen.Schedule.CompSchedule.route) {
            CompSchedule(
                contentPadding = contentPadding,
                matches = matches
            )
        }
        composable(AppScreen.Schedule.OurSchedule.route) {
            OurSchedule(
                contentPadding = contentPadding,
                matches = matches
            )
        }
    }
}

//--------------------------------
// MAIN SCREEN
//--------------------------------
@Composable
fun ScheduleScreen(
    contentPadding: PaddingValues,
    viewModel: ScheduleViewModel,
    snackbarState: SnackbarHostState
) {
    val matches by viewModel.matches.collectAsState()

    val scheduleNavController = rememberNavController()

    LaunchedEffect(Unit) {
        viewModel.events.collect { message ->
            snackbarState.showSnackbar(message)
        }
    }
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {

        ScheduleNav(scheduleNavController)

        Spacer(Modifier.height(8.dp))


        Spacer(Modifier.height(8.dp))
        ScheduleScreenNavHost(
            contentPadding = contentPadding,
            navController = scheduleNavController, matches = matches
        )
    }
}
