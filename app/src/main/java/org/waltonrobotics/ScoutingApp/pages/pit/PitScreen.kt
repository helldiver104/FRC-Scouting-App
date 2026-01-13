package org.waltonrobotics.ScoutingApp.pages.pit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.waltonrobotics.ScoutingApp.R
import org.waltonrobotics.ScoutingApp.appNavigation.AppScreen

//--------------------------------
// PIT STUFF
//--------------------------------
data class PitTab(
    val label: String,
    val iconRes: Int,
    val route: String,
    val contentDescription: String
)

@Composable
fun PitNav(
    pitNavController: NavHostController,
    selectedTab: MutableState<Int>,
    useIcons: Boolean = true
) {
    val tabs = listOf(

        PitTab(
            "Checklist",
            R.drawable.calendar_clock,
            AppScreen.Pit.PitChecklist.route,
            "pit checklist"
        ),
        PitTab(
            "Sponsors",
            R.drawable.calendar_check,
            AppScreen.Pit.Sponsors.route,
            "our sponsors"
        )
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        val currentRoute =
            pitNavController.currentBackStackEntryAsState().value?.destination?.route
        NavigationBar (
            containerColor = androidx.compose.ui.graphics.Color.Transparent
        ){
            tabs.forEach { tab ->
                NavigationBarItem(
                    selected = currentRoute == tab.route,
                    onClick = {
                        pitNavController.navigate(tab.route) {
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
fun PitScreenNavHost(
    navController: NavHostController,
) {
    NavHost(navController, startDestination = AppScreen.Pit.PitChecklist.route) {
        composable(AppScreen.Pit.PitChecklist.route) { PitChecklist() }
        composable(AppScreen.Pit.Sponsors.route) { Sponsors() }
    }
}

@Composable
fun PitScreen() {
    // TODO MAKE A VIEWMODEL
//    val viewModel: MatchScoutingViewModel = viewModel()
    val pitNavController = rememberNavController()
    val selectedTab = remember { mutableIntStateOf(0) }

    Column(Modifier.fillMaxSize()) {
        PitNav(pitNavController, selectedTab)
        PitScreenNavHost(pitNavController)
    }
}
