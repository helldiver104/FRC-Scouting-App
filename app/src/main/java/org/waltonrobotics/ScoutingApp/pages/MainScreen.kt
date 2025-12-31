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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import org.waltonrobotics.ScoutingApp.schedule.Match
import org.waltonrobotics.ScoutingApp.viewmodel.MatchScoutingViewModel
import org.waltonrobotics.ScoutingApp.viewmodels.ScheduleViewModel
import org.waltonrobotics.ScoutingApp.viewmodels.ScouterViewModel

enum class AlliancePosition(val label: String, val isRed: Boolean) {
    RED_1("Red 1", true), RED_2("Red 2", true), RED_3("Red 3", true),
    BLUE_1("Blue 1", false), BLUE_2("Blue 2", false), BLUE_3("Blue 3", false),
    NONE("None", false)
}

data class ScouterAssignment(
    val scouterName: String,
    val assignments: List<String>
)

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
            containerColor = when {
                myPosition == AlliancePosition.NONE -> MaterialTheme.colorScheme.surfaceVariant
                myPosition.isRed -> Color(0xFFFFEBEE)
                else -> Color(0xFFE3F2FD)
            }
        ),
        elevation = CardDefaults.cardElevation(2.dp)
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
                        color = Color.Black
                    )
                    Text(
                        text = "Position: ${myPosition.label}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = when {
                            myPosition.isRed -> Color(0xFFB71C1C)
                            myPosition != AlliancePosition.NONE -> Color(0xFF0D47A1)
                            else -> MaterialTheme.colorScheme.onSurfaceVariant
                        }
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
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("START ASSIGNED SCOUTING")
            }
        }
    }
}
@Composable
fun ScouterSelectionDialog(
    names: List<String>,
    onDismiss: () -> Unit,
    onNameSelected: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val filteredNames = names.filter { it.contains(searchQuery, ignoreCase = true) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Find Your Name") },
        text = {
            Column {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Search...") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(Modifier.height(8.dp))
                LazyColumn(modifier = Modifier.heightIn(max = 300.dp)) {
                    items(filteredNames) { name ->
                        TextButton(
                            onClick = { onNameSelected(name) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(name, modifier = Modifier.fillMaxWidth())
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
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
    val allNames by scouterScheduleVm.allScouterNames.collectAsState()

    var currentMatch by rememberSaveable { mutableIntStateOf(1) }
    var scouterName by rememberSaveable { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }

    val csvLauncher = rememberLauncherForActivityResult(
        contract = androidx.activity.result.contract.ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { scouterScheduleVm.importCsv(context, it) }
    }

    val assignment = scouterScheduleVm.getAssignmentForMatch(scouterName, currentMatch)

    val myPosition = remember(assignment) {
        if (assignment.isNullOrBlank()) {
            AlliancePosition.NONE
        } else {
            // Normalize the string (lowercase and remove spaces) to compare
            val cleanAssignment = assignment.replace(" ", "").lowercase()

            AlliancePosition.entries.find { enumPos ->
                enumPos.label.replace(" ", "").lowercase() == cleanAssignment
            } ?: AlliancePosition.NONE
        }
    }

    if (showDialog) {
        ScouterSelectionDialog(
            names = allNames,
            onDismiss = { showDialog = false },
            onNameSelected = { scouterName = it; showDialog = false }
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
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

            OutlinedTextField(
                value = scouterName,
                onValueChange = { scouterName = it },
                label = { Text("Your Name") },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    IconButton(onClick = { showDialog = true }) {
                        Icon(painterResource(R.drawable.rightarrow), null)
                    }
                }
            )

            MyNextMatchCard(
                currentMatch = currentMatch,
                matches = matches,
                myPosition = myPosition,
                onStartScouting = { match, robot ->
                    scoutingVm.prepNewMatch(match, robot)
                    navController.navigate("scouting_screen")
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
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
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