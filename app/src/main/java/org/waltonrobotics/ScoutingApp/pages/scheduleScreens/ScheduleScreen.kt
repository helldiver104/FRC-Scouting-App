package org.waltonrobotics.ScoutingApp.pages.scheduleScreens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.waltonrobotics.ScoutingApp.R
import org.waltonrobotics.ScoutingApp.appNavigation.AppScreen
import org.waltonrobotics.ScoutingApp.helpers.csv.CsvPickerButton
import org.waltonrobotics.ScoutingApp.schedule.Match
import org.waltonrobotics.ScoutingApp.viewmodels.ScheduleViewModel

//--------------------------------
// TOP TABS
//--------------------------------
data class ScheduleTab(
    val label: String,
    val iconRes: Int,
    val route: String
)

@Composable
fun ScheduleNav(
    navController: NavHostController
) {
    val tabs = listOf(
        ScheduleTab(
            "Comp Schedule",
            R.drawable.calendar_clock,
            AppScreen.Schedule.CompSchedule.route
        ),
        ScheduleTab(
            "Our Schedule",
            R.drawable.calendar_check,
            AppScreen.Schedule.OurSchedule.route
        )
    )

    val currentRoute =
        navController.currentBackStackEntryAsState().value?.destination?.route

    NavigationBar(containerColor = Color.Transparent) {
        tabs.forEach { tab ->
            NavigationBarItem(
                selected = currentRoute == tab.route,
                onClick = {
                    navController.navigate(tab.route) {
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(tab.iconRes),
                        contentDescription = tab.label
                    )
                },
                label = { Text(tab.label) }
            )
        }
    }
}

//--------------------------------
// NAV HOST
//--------------------------------
@Composable
fun ScheduleScreenNavHost(
    navController: NavHostController,
    matches: List<Match>
) {
    NavHost(
        navController = navController,
        startDestination = AppScreen.Schedule.CompSchedule.route
    ) {
        composable(AppScreen.Schedule.CompSchedule.route) {
            CompSchedule(matches)
        }
        composable(AppScreen.Schedule.OurSchedule.route) {
            OurSchedule(matches)
        }
    }
}

//--------------------------------
// MAIN SCREEN
//--------------------------------
@Composable
fun ScheduleScreen(
    vm: ScheduleViewModel = viewModel()
) {
    val context = LocalContext.current
    val matches by vm.matches.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val scheduleNavController = rememberNavController()

    LaunchedEffect(Unit) {
        vm.events.collect { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            ScheduleNav(scheduleNavController)

            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CsvPickerButton { uri ->
                    vm.loadMatchesFromCsv(context, uri)
                }
            }

            Spacer(Modifier.height(8.dp))

            ScheduleScreenNavHost(
                navController = scheduleNavController,
                matches = matches
            )
        }
    }
}
