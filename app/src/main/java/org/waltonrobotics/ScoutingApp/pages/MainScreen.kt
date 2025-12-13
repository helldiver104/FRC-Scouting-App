package org.waltonrobotics.ScoutingApp.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.waltonrobotics.ScoutingApp.R
import org.waltonrobotics.ScoutingApp.appNavigation.AppScreen

@Composable
fun MainScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Hello! Click on the form you need!",
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Button(
            onClick = { navController.navigate(AppScreen.PitScoutingForm.route) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp)
        ) {
            Icon(painterResource(R.drawable.pitscouting), "pit scouting form")
            Spacer(Modifier.width(8.dp))
            Text("Pit Scouting")
        }

        Button(
            onClick = { navController.navigate(AppScreen.CycleTimeForm.route) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp)
        ) {
            Icon(painterResource(R.drawable.timer), "cycle time form")
            Spacer(Modifier.width(8.dp))
            Text("Cycle Time")
        }

        Button(
            onClick = { navController.navigate(AppScreen.ScoutingScreen.route) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp)
        ) {
            Icon(painterResource(R.drawable.robot), "match scouting form")
            Spacer(Modifier.width(8.dp))
            Text("Match Scouting")
        }

        Button(
            onClick = { navController.navigate(AppScreen.FAQScreen.route) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(painterResource(R.drawable.questionmark), "FAQs")
            Spacer(Modifier.width(8.dp))
            Text("FAQ")
        }
    }
}
