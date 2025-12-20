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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.waltonrobotics.ScoutingApp.R
import org.waltonrobotics.ScoutingApp.appNavigation.AppScreen
import org.waltonrobotics.ScoutingApp.helpers.csv.CsvPickerButton
import org.waltonrobotics.ScoutingApp.helpers.csv.readMatchCsv
import org.waltonrobotics.ScoutingApp.schedule.Match


//--------------------------------
// SCHEDULE
//--------------------------------
data class ScheduleTab(
    val label: String,
    val iconRes: Int,
    val route: String,
    val contentDescription: String
)

@Composable
fun ScheduleNav(
    scheduleNavController: NavHostController,
    selectedTab: MutableState<Int>,
    useIcons: Boolean = true
) {
    val tabs = listOf(

        ScheduleTab(
            "Comp Schedule",
            R.drawable.calendar_clock,
            AppScreen.Schedule.CompSchedule.route,
            "comp schedule screen"
        ),
        ScheduleTab(
            "Our Schedule",
            R.drawable.calendar_check,
            AppScreen.Schedule.OurSchedule.route,
            "Our Schedule"
        )
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        val currentRoute =
            scheduleNavController.currentBackStackEntryAsState().value?.destination?.route
        NavigationBar (
            containerColor = androidx.compose.ui.graphics.Color.Transparent
        ){
            tabs.forEach { tab ->
                NavigationBarItem(
                    selected = currentRoute == tab.route,
                    onClick = {
                        scheduleNavController.navigate(tab.route) {
                            popUpTo(AppScreen.MainScreen.route) {
                                saveState = true
                            }
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
}

@Composable
fun ScheduleScreenNavHost(
    navController: NavHostController,
    matches: List<Match>
) {
    NavHost(navController, startDestination = AppScreen.Schedule.CompSchedule.route) {
        composable(AppScreen.Schedule.CompSchedule.route) {
            CompSchedule(matches)
        }
        composable(AppScreen.Schedule.OurSchedule.route) {
            OurSchedule(matches)
        }
    }
}

@Composable
fun ScheduleScreen() {
    val scheduleNavController = rememberNavController()
    val selectedTab = remember { mutableIntStateOf(0) }
    val context = LocalContext.current

    var matches by remember { mutableStateOf<List<Match>>(emptyList()) }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        CsvPickerButton { uri ->
            matches = readMatchCsv(context, uri)
        }

        Spacer(Modifier.height(16.dp))

        ScheduleNav(scheduleNavController, selectedTab)
        ScheduleScreenNavHost(scheduleNavController, matches)
    }
}