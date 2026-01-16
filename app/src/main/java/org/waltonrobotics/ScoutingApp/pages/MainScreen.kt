package org.waltonrobotics.ScoutingApp.pages

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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import org.waltonrobotics.ScoutingApp.viewmodels.AuthViewModel
import org.waltonrobotics.ScoutingApp.viewmodels.ScheduleViewModel
import org.waltonrobotics.ScoutingApp.viewmodels.ScouterViewModel

enum class AlliancePosition(val label: String, val isRed: Boolean, val isStrat: Boolean = false) {
    RED_1("Red 1", true),
    RED_2("Red 2", true),
    RED_3("Red 3", true),
    BLUE_1("Blue 1", false),
    BLUE_2("Blue 2", false),
    BLUE_3("Blue 3", false),
    APP_MANAGEMENT("App Management", false, true),
    COMMS("Communications", false, true),
    PIT_CREW("Pit Crew", false, true),
    CANBOT("CanBot", false, true),
    PIT_PRESENTATION("Pit Presentation", false, true),
    TWENTY_NINE_SEVENTY_FOUR("2974", false, true),
    NONE("None", false)
}

@Composable
fun MyNextMatchCard(
    currentMatch: Int,
    matches: List<Match>,
    myPosition: AlliancePosition,
    onStartScouting: (String, String) -> Unit,
    onError: (String) -> Unit
) {
    val isNotScouting = myPosition == AlliancePosition.NONE
    val isStaff = myPosition.isStrat
    val matchData = matches.find { it.matchNumber == currentMatch.toString() }

    val assignedRobot = remember(matchData, myPosition) {
        val alliance = if (myPosition.isRed) matchData?.redAlliance else matchData?.blueAlliance
        val index = myPosition.label.lastOrNull()?.digitToIntOrNull()?.minus(1) ?: -1
        alliance?.getOrNull(index)
    }

    val contentColor = if (isNotScouting) Color.White else Color.Black
    val containerColor = when {
        isNotScouting -> MaterialTheme.colorScheme.surfaceVariant
        myPosition.isRed -> Color(0xFFFFEBEE)
        else -> Color(0xFFE3F2FD)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor)
    ) {
        Column(Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    if (isStaff) {

                        OtherPositionView(
                            myPosition.label
                        )
                    } else {
                        ScouterInfoView(
                            currentMatch = currentMatch,
                            assignedRobot = assignedRobot,
                            myPosition = myPosition,
                            isOnBreak = isNotScouting,
                            contentColor = contentColor
                        )
                    }
                }

                Icon(
                    painter = painterResource(if (isNotScouting) R.drawable.person else R.drawable.robot),
                    contentDescription = null,
                    modifier = Modifier.size(40.dp),
                    tint = when {
                        isNotScouting -> Color.Gray
                        myPosition.isRed -> Color.Red
                        else -> Color.Blue
                    }
                )
            }

            if (!isStaff && !isNotScouting) {
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        if (matchData == null || assignedRobot == null) {
                            onError("No data for Match $currentMatch")
                        } else {
                            onStartScouting(currentMatch.toString(), assignedRobot)
                        }
                    }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(
                        containerColor = if (myPosition.isRed) Color(0xFFD32F2F) else Color(
                            0xFF1976D2
                        )
                    )
                ) {
                    Text("START SCOUTING ${assignedRobot ?: ""}", color = Color.White)
                }
            }
        }
    }
}

@Composable
private fun OtherPositionView(
    position: String
) {
    Text(
        text = "CURRENT JOB: $position",
        style = MaterialTheme.typography.labelLarge,
        color = Color.Black
    )
}

@Composable
private fun ScouterInfoView(
    currentMatch: Int,
    assignedRobot: String?,
    myPosition: AlliancePosition,
    isOnBreak: Boolean,
    contentColor: Color
) {
    Text(
        text = "UP NEXT: MATCH $currentMatch",
        style = MaterialTheme.typography.labelLarge,
        color = contentColor
    )
    Text(
        text = when {
            isOnBreak -> "break time!"
            assignedRobot != null -> "Robot $assignedRobot"
            else -> "Match Data Missing"
        },
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.Bold,
        color = contentColor
    )
    Text(
        text = if (isOnBreak) "Enjoy your rest!" else "Position: ${myPosition.label}",
        style = MaterialTheme.typography.bodyMedium,
        color = contentColor
    )
}

@Composable
fun MainScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    scheduleVm: ScheduleViewModel,
    scoutingVm: MatchScoutingViewModel,
    scouterScheduleVm: ScouterViewModel,
    authVm: AuthViewModel
) {
    val scope = rememberCoroutineScope()
    val userEmail = authVm.currentUser?.email
    val scouters by scouterScheduleVm.scouters.collectAsState()
    val matches by scheduleVm.matches.collectAsState()

    var currentMatch by rememberSaveable { mutableIntStateOf(1) }

    val scouterName = remember(userEmail, scouters) {
        scouterScheduleVm.getNameByEmail(userEmail)
    }

    val assignment = remember(scouterName, currentMatch, scouters) {
        scouterScheduleVm.getAssignmentForMatch(scouterName, currentMatch)
    }

    val myPosition = remember(assignment) {
        val clean = assignment?.trim()?.lowercase() ?: ""
        AlliancePosition.entries.find {
            it.label.lowercase() == clean || it.name.lowercase() == clean
        } ?: AlliancePosition.NONE
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(Modifier.height(16.dp))

        Text(
            text = "Hi, $scouterName",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(24.dp))

        MyNextMatchCard(
            currentMatch = currentMatch,
            matches = matches,
            myPosition = myPosition,
            onStartScouting = { matchNumber, robotNumber ->
                scoutingVm.prepNewMatch(matchNumber, robotNumber)
                scoutingVm.updateName(scouterName)
                navController.navigate(AppScreen.ScoutingScreen.route)
            },
            onError = { message ->
                scope.launch { snackbarHostState.showSnackbar(message) }
            }
        )

        Spacer(Modifier.height(16.dp))

        MatchPager(
            currentMatch = currentMatch, onMatchChange = { currentMatch = it })

        Spacer(Modifier.height(32.dp))

        Text("Quick Tools", style = MaterialTheme.typography.labelLarge)
        Spacer(Modifier.height(8.dp))

        ToolButton("Match Scouting", R.drawable.controller) {
            navController.navigate(AppScreen.ScoutingScreen.route)
        }
        ToolButton("Pit Scouting", R.drawable.pitscouting) {
            navController.navigate(AppScreen.PitScoutingForm.route)
        }
        ToolButton("Cycle Timer", R.drawable.timer) {
            navController.navigate(AppScreen.CycleTimeForm.route)
        }
        ToolButton("FAQ", R.drawable.questionmark) {
            navController.navigate(AppScreen.FAQScreen.route)
        }

        Spacer(Modifier.height(24.dp))
    }
}

@Composable
fun MatchPager(currentMatch: Int, onMatchChange: (Int) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { if (currentMatch > 1) onMatchChange(currentMatch - 1) }) {
                Icon(painterResource(R.drawable.leftarrow), "Previous")
            }
            Text(
                text = "Match $currentMatch",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            IconButton(onClick = { onMatchChange(currentMatch + 1) }) {
                Icon(painterResource(R.drawable.rightarrow), "Next")
            }
        }
    }
}

@Composable
fun ToolButton(label: String, iconRes: Int, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Icon(painterResource(iconRes), null, Modifier.size(20.dp))
        Spacer(Modifier.width(12.dp))
        Text(label)
        Spacer(Modifier.weight(1f))
        Icon(painterResource(R.drawable.rightarrow), null, Modifier.size(20.dp))
    }
}