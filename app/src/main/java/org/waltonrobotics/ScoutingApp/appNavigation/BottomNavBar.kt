package org.waltonrobotics.ScoutingApp.appNavigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import org.waltonrobotics.ScoutingApp.R

data class BottomNavItem(val label: String, val iconRes: Int, val route: String)

@Composable
fun BottomNavBar(navController: NavHostController) {
    val items = listOf(
        BottomNavItem("Home", R.drawable.home, AppScreen.MainScreen.route),
        BottomNavItem("Schedule", R.drawable.calendar_clock, AppScreen.ScheduleScreen.route),
        BottomNavItem("Pit", R.drawable.drill, AppScreen.Pit.PitScreen.route),
        BottomNavItem("Account", R.drawable.person, AppScreen.AccountScreen.route)
    )

    val currentRoute =
        navController.currentBackStackEntryAsState().value?.destination?.route

    NavigationBar (
        containerColor = androidx.compose.ui.graphics.Color.Transparent
    ){
        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(AppScreen.MainScreen.route) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(item.iconRes),
                        contentDescription = item.label
                    )
                },
                label = { Text(item.label) }
            )
        }
    }
}