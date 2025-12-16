package org.waltonrobotics.ScoutingApp.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import org.waltonrobotics.ScoutingApp.appNavigation.AppScreen


@Composable
fun FAQScreen(
    navController: NavController
) {
    Column(Modifier.padding(16.dp)) {
        Text("FAQ", fontSize = 25.sp)
        Spacer(Modifier.height(10.dp))
        Text("Q: What is the starting line?")
        Text("A: The black one nearest to the robots, NOT the colored ones")
        Text("Q: I keep getting notifications for wrong matches. Why?")
        Text("A: Likely updating schedule mid-competition, refresh app or wait")
        Text("Q: How do we know which robot is Red 1?")
        Text("A: Red 1 is the first number on the big screen, NOT field order")
        Text("Q: Far side / middle / processor side?")
        Text("A: Far side = further, middle = middle, processor side = near amp")
        Spacer(Modifier.height(12.dp))
        Button(
            onClick = {
                navController.navigate(AppScreen.MainScreen.route) {
                    popUpTo(AppScreen.MainScreen.route) { inclusive = true }
                    launchSingleTop = true
                }
            }
        ) {
            Text("Go back")
        }
    }
}
