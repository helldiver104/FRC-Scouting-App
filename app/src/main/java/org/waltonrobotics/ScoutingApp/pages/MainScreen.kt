package org.waltonrobotics.ScoutingApp.pages

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import org.waltonrobotics.ScoutingApp.R
import org.waltonrobotics.ScoutingApp.appNavigation.AppScreen
import org.waltonrobotics.ScoutingApp.schedule.Match
import org.waltonrobotics.ScoutingApp.viewmodel.MatchScoutingViewModel
import org.waltonrobotics.ScoutingApp.viewmodels.ScheduleViewModel
import org.waltonrobotics.ScoutingApp.viewmodels.ScouterViewModel

enum class AlliancePosition(val label: String, val isRed: Boolean) {
    RED_1("Red 1", true), RED_2("Red 2", true), RED_3("Red 3", true), BLUE_1(
        "Blue 1",
        false
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
    val isOnBreak = myPosition == AlliancePosition.NONE
    // Inside MyNextMatchCard
    val matchData = matches.find { it.matchNumber == currentMatch.toString() }

// Use getOrNull to prevent IndexOutOfBounds crashes
    val assignedRobot = when (myPosition) {
        AlliancePosition.RED_1 -> matchData?.redAlliance?.getOrNull(0)
        AlliancePosition.RED_2 -> matchData?.redAlliance?.getOrNull(1)
        AlliancePosition.RED_3 -> matchData?.redAlliance?.getOrNull(2)
        AlliancePosition.BLUE_1 -> matchData?.blueAlliance?.getOrNull(0)
        AlliancePosition.BLUE_2 -> matchData?.blueAlliance?.getOrNull(1)
        AlliancePosition.BLUE_3 -> matchData?.blueAlliance?.getOrNull(2)
        else -> null
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = when {
                isOnBreak -> MaterialTheme.colorScheme.surfaceVariant
                myPosition.isRed -> Color(0xFFFFEBEE)
                else -> Color(0xFFE3F2FD)
            }
        )
    ) {
        Column(Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "UP NEXT: MATCH $currentMatch",
                        style = MaterialTheme.typography.labelLarge,
                        color = if (isOnBreak) Color.White
                        else Color.Black
                    )
                    Text(
                        text = if (isOnBreak) "break time!"
                        else if (assignedRobot != null) "Robot $assignedRobot"
                        else "Match Data Missing",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = if (isOnBreak) Color.White
                        else Color.Black
                    )
                    Text(
                        text = if (isOnBreak) "Enjoy your rest!" else "Position: ${myPosition.label}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (isOnBreak) Color.White
                        else Color.Black
                    )
                }
                Icon(
                    painter = painterResource(id = if (isOnBreak) R.drawable.person else R.drawable.robot),
                    contentDescription = null,
                    modifier = Modifier.size(40.dp),
                    tint = if (isOnBreak) Color.Gray else if (myPosition.isRed) Color.Red else Color.Blue
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (isOnBreak) onError("You are off the clock!")
                    else if (matchData == null || assignedRobot == null) onError("No data for Match $currentMatch")
                    else onStartScouting(currentMatch.toString(), assignedRobot)
                },
                enabled = !isOnBreak,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                        containerColor = when {
                            isOnBreak -> MaterialTheme.colorScheme.surfaceVariant
                            myPosition.isRed -> MaterialTheme.colorScheme.errorContainer
                            else -> MaterialTheme.colorScheme.primary
                        },
            )
            ) {
                Text(
                    if (isOnBreak) "" else "START ASSIGNED SCOUTING",
                    color = if (isOnBreak) Color.White else Color.White
                )
                // TODO autofill match number / robot number
            }

        }
    }
}

@Composable
fun MainScreen(
    navController: NavController,
    scheduleVm: ScheduleViewModel,
    scoutingVm: MatchScoutingViewModel,
    scouterScheduleVm: ScouterViewModel
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val matches by scheduleVm.matches.collectAsState()
    val scouters by scouterScheduleVm.scouters.collectAsState()

    var currentMatch by rememberSaveable { mutableIntStateOf(1) }
    var scouterName by rememberSaveable { mutableStateOf("Minh Nguyen") }

    val csvLauncher = rememberLauncherForActivityResult(
        contract = androidx.activity.result.contract.ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { scouterScheduleVm.importCsv(context, it) }
    }

    val assignment = remember(scouters, scouterName, currentMatch) {
        scouterScheduleVm.getAssignmentForMatch(scouterName, currentMatch)
    }

    val myPosition = remember(assignment) {
        if (assignment.isNullOrBlank()) {
            AlliancePosition.NONE
        } else {
            val clean = assignment.replace(" ", "").lowercase()
            AlliancePosition.entries.find {
                it.label.replace(" ", "").lowercase() == clean
            } ?: AlliancePosition.NONE
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(Modifier.height(24.dp))

            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "Hi, ${scouterName.ifEmpty { "Scouter" }}",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                FilledTonalIconButton(onClick = { csvLauncher.launch("*/*") }) {
                    Icon(painterResource(R.drawable.add), "Import")
                }
            }

            Spacer(Modifier.height(16.dp))


            MyNextMatchCard(
                currentMatch = currentMatch,
                matches = matches,
                myPosition = myPosition,
                onStartScouting = { match, robot ->
                    scoutingVm.prepNewMatch(match, robot)
                    navController.navigate(AppScreen.ScoutingScreen.route)
                },
                onError = { scope.launch { snackbarHostState.showSnackbar(it) } }
            )

            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
                TextButton(onClick = { if (currentMatch > 1) currentMatch-- }) { Text("Previous") }
                Text("Match $currentMatch", fontWeight = FontWeight.Bold)
                TextButton(onClick = { currentMatch++ }) { Text("Next") }
            }

            Spacer(Modifier.height(24.dp))

            val menuButtons = listOf(
                Triple("Match Scouting", R.drawable.robot, "scouting_screen"),
                Triple("Pit Scouting", R.drawable.pitscouting, "pit_form"),
                Triple("Cycle Time", R.drawable.timer, "cycle_form"),
                Triple("FAQ", R.drawable.questionmark, "faq_screen")
            )

            menuButtons.forEach { (label, icon, route) ->
                OutlinedButton(
                    onClick = { navController.navigate(route) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(painterResource(icon), null, Modifier.size(20.dp))
                    Spacer(Modifier.width(12.dp))
                    Text(label)
                    Spacer(Modifier.weight(1f))
                    Icon(painterResource(R.drawable.rightarrow), null, Modifier.size(20.dp))
                }
            }
        }
    }
}