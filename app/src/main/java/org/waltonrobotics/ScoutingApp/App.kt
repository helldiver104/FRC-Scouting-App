package org.waltonrobotics.ScoutingApp

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import org.waltonrobotics.ScoutingApp.appNavigation.AppNavHost
import org.waltonrobotics.ScoutingApp.appNavigation.BottomNavBar

@Composable
fun App() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavBar(navController) }
    ) { innerPadding ->
        AppNavHost(
            navController = navController,
            modifier = Modifier
                .padding(innerPadding)
        )
    }
}