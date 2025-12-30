package org.waltonrobotics.ScoutingApp.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.launch
import org.waltonrobotics.ScoutingApp.R
import org.waltonrobotics.ScoutingApp.appNavigation.AppScreen
import org.waltonrobotics.ScoutingApp.schedule.Match
import org.waltonrobotics.ScoutingApp.viewmodel.MatchScoutingViewModel

enum class AlliancePosition(val label: String, val isRed: Boolean) {
    RED_1("Red 1", true), RED_2("Red 2", true), RED_3("Red 3", true), BLUE_1(
        "Blue 1", false
    ),
    BLUE_2("Blue 2", false), BLUE_3("Blue 3", false), NONE("None", false)
}

@Composable
fun MyNextMatchCard(
    currentMatch: Int,
    matches: List<Match>,
    myPosition: AlliancePosition,
    onStartScouting: (String, String) -> Unit,
    onError: (String) -> Unit
) {
    val matchData = matches.find { it.matchNumber == currentMatch.toString() }
    val assignedRobot = when (myPosition) {
        AlliancePosition.RED_1 -> matchData?.redAlliance?.getOrNull(0)
        AlliancePosition.RED_2 -> matchData?.redAlliance?.getOrNull(1)
        AlliancePosition.RED_3 -> matchData?.redAlliance?.getOrNull(2)
        AlliancePosition.BLUE_1 -> matchData?.blueAlliance?.getOrNull(0)
        AlliancePosition.BLUE_2 -> matchData?.blueAlliance?.getOrNull(1)
        AlliancePosition.BLUE_3 -> matchData?.blueAlliance?.getOrNull(2)
        AlliancePosition.NONE -> null
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(
            // Subtle color tint based on alliance
            containerColor = when {
                myPosition == AlliancePosition.NONE -> MaterialTheme.colorScheme.surfaceVariant
                myPosition.isRed -> Color(0xFFFFEBEE) // Very light red
                else -> Color(0xFFE3F2FD) // Very light blue
            }
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "UP NEXT: MATCH $currentMatch",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = if (assignedRobot != null) "Robot $assignedRobot" else "No Assignment",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primaryContainer
                    )
                    Text(
                        text = "Position: ${myPosition.label}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onError
                    )
                }
                Icon(
                    painter = painterResource(id = R.drawable.robot),
                    contentDescription = null,
                    modifier = Modifier.size(40.dp),
                    tint = if (myPosition.isRed) Color.Red else if (myPosition != AlliancePosition.NONE) Color.Blue else Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (myPosition == AlliancePosition.NONE) onError("Set position in Settings!")
                    else if (matchData == null || assignedRobot == null) onError("Match $currentMatch data missing!")
                    else onStartScouting(currentMatch.toString(), assignedRobot)
                }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(8.dp)
            ) {
                Text("START ASSIGNED SCOUTING")
            }
        }
    }
}

@Composable
fun MainScreen(
    navController: NavController,
    scoutingVm: MatchScoutingViewModel,
    dummyMatches: List<Match>,
    dummyPosition: AlliancePosition,
) {
    val systemUiController = rememberSystemUiController()

    SideEffect {
        systemUiController.setSystemBarsColor(
            color = Color.Transparent, darkIcons = false
        )
    }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        contentWindowInsets = WindowInsets.systemBars
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState()), horizontalAlignment = Alignment.Start
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Welcome back!",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Ready for the next match?",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.secondary
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Primary Action Card
            MyNextMatchCard(
                currentMatch = 12,
                matches = dummyMatches,
                myPosition = dummyPosition,
                onStartScouting = { match, robot ->
                    scoutingVm.prepNewMatch(match, robot)
                    navController.navigate(AppScreen.ScoutingScreen.route)
                },
                onError = { message ->
                    scope.launch { snackbarHostState.showSnackbar(message) }
                })

            Spacer(modifier = Modifier.height(32.dp))
            Text("SCOUT/MORE INFO", style = MaterialTheme.typography.labelSmall)
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            val menuButtons = listOf(
                Triple("Match Scouting", R.drawable.robot, AppScreen.ScoutingScreen.route),
                Triple("Pit Scouting", R.drawable.pitscouting, AppScreen.PitScoutingForm.route),
                Triple("Cycle Time", R.drawable.timer, AppScreen.CycleTimeForm.route),
                Triple("FAQ", R.drawable.questionmark, AppScreen.FAQScreen.route)
            )

            menuButtons.forEach { (label, icon, route) ->
                OutlinedButton(
                    onClick = {
                        navController.navigate(route) {
                            launchSingleTop = true
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        painterResource(icon),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(Modifier.width(12.dp))
                    Text(label)
                    Spacer(Modifier.weight(1f))
                    Icon(
                        painterResource(R.drawable.rightarrow),
                        contentDescription = "right arrow",
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}