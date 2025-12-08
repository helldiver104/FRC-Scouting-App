package org.waltonrobotics.ScoutingApp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.waltonrobotics.ScoutingApp.viewmodel.CycleTimeViewModel
import org.waltonrobotics.ScoutingApp.viewmodel.MatchScoutingViewModel
import org.waltonrobotics.ScoutingApp.viewmodel.PitScoutingViewModel

//--------------------------------
// SCREENS
//--------------------------------
object MainScreen
object ScoutingScreen
object ScoutingScreenStart
object ScoutingScreenAuton
object ScoutingScreenTeleop
object ScoutingScreenClosingComments
object CycleTimeForm
object PitScoutingForm
object FAQScreen
object ScheduleScreen
object AccountScreen

//--------------------------------
// HELPER THINGS
//--------------------------------
@Composable
fun TextFieldItem(state: MutableState<String>, label: String) {
    OutlinedTextField(
        value = state.value,
        onValueChange = { state.value = it },
        label = { Text(label) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    )
}

@Composable
fun CounterItem(label: String, state: MutableState<Int>) {
    Text(label)
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { if (state.value > 0) state.value-- }) {
            Icon(painter = painterResource(R.drawable.remove), contentDescription = "Remove")
        }
        Text(state.value.toString(), fontSize = 18.sp)
        IconButton(onClick = { state.value++ }) {
            Icon(painter = painterResource(R.drawable.add), contentDescription = "Add")
        }
    }
}

@Composable
fun SegmentedSelector(label: String, options: List<String>, selectedState: MutableState<Int>) {
    Text(label)
    Row(modifier = Modifier.padding(vertical = 4.dp)) {
        options.forEachIndexed { index, text ->
            Button(
                onClick = { selectedState.value = index },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedState.value == index)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.secondary
                ),
                modifier = Modifier.padding(end = 4.dp)
            ) {
                Text(text, fontSize = 12.sp)
            }
        }
    }
}

//--------------------------------
// NAV HOSTS
//--------------------------------
@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController, startDestination = MainScreen::class.simpleName!!) {
        composable(MainScreen::class.simpleName!!) { MainScreen(navController) }
        composable(ScoutingScreen::class.simpleName!!) { ScoutingScreen() }
        composable(ScheduleScreen::class.simpleName!!) { ScheduleScreen() }
        composable(AccountScreen::class.simpleName!!) { AccountScreen() }
        composable(PitScoutingForm::class.simpleName!!) { PitScoutingForm() }
        composable(CycleTimeForm::class.simpleName!!) { CycleTimeForm() }
        composable(FAQScreen::class.simpleName!!) { FAQScreen() }
    }
}

@Composable
fun ScoutingScreenNavHost(vm: MatchScoutingViewModel, navController: NavHostController) {
    NavHost(navController, startDestination = ScoutingScreenStart::class.simpleName!!) {
        composable(ScoutingScreenStart::class.simpleName!!) { ScoutingScreenStart(vm) }
        composable(ScoutingScreenAuton::class.simpleName!!) { ScoutingScreenAuton(vm) }
        composable(ScoutingScreenTeleop::class.simpleName!!) { ScoutingScreenTeleop(vm) }
        composable(ScoutingScreenClosingComments::class.simpleName!!) { ScoutingScreenClosingComments(vm) }
    }
}

//--------------------------------
// APP
//--------------------------------
@Composable
fun App() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomNavBar(navController) }
    ) { innerPadding ->
        AppNavHost(navController)
    }
}

data class BottomNavItem(val label: String, val iconRes: Int, val route: String)

@Composable
fun BottomNavBar(navController: NavHostController) {
    val items = listOf(
        BottomNavItem("Home", R.drawable.home, MainScreen::class.simpleName!!),
        BottomNavItem("Schedule", R.drawable.calendar_clock, ScheduleScreen::class.simpleName!!),
        BottomNavItem("Account", R.drawable.person, AccountScreen::class.simpleName!!)
    )
    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                selected = false,
                onClick = { navController.navigate(item.route) },
                icon = { Icon(painter = painterResource(item.iconRes), contentDescription = item.label) }
            )
        }
    }
}

//--------------------------------
// MAIN SCREEN
//--------------------------------
@Composable
fun MainScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Hello! Click on the form you need!", modifier = Modifier.padding(bottom = 16.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Button(onClick = { navController.navigate(PitScoutingForm::class.simpleName!!) }) {
                    Icon(painter = painterResource(R.drawable.pitscouting), contentDescription = "Pit")
                }
                Text("Pit Scouting")
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Button(onClick = { navController.navigate(CycleTimeForm::class.simpleName!!) }) {
                    Icon(painter = painterResource(R.drawable.timer), contentDescription = "Cycle")
                }
                Text("Cycle Time")
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Button(onClick = { navController.navigate(ScoutingScreen::class.simpleName!!) }) {
                    Icon(painter = painterResource(R.drawable.robot), contentDescription = "Match")
                }
                Text("Match Scouting")
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Button(onClick = { navController.navigate(FAQScreen::class.simpleName!!) }) {
                    Icon(painter = painterResource(R.drawable.questionmark), contentDescription = "FAQ")
                }
                Text("FAQ")
            }
        }
    }
}

//--------------------------------
// MATCH SCOUTING
//--------------------------------
data class ScoutingTab(val label: String, val iconRes: Int, val route: String)

@Composable
fun MatchScoutingTopNav(
    scoutingNavController: NavHostController,
    selectedTab: MutableState<Int>,
    useIcons: Boolean = true
) {
    val tabs = listOf(
        ScoutingTab("Start", R.drawable.start, ScoutingScreenStart::class.simpleName!!),
        ScoutingTab("Auton", R.drawable.robot, ScoutingScreenAuton::class.simpleName!!),
        ScoutingTab("Teleop", R.drawable.controller, ScoutingScreenTeleop::class.simpleName!!),
        ScoutingTab("Closing", R.drawable.stop, ScoutingScreenClosingComments::class.simpleName!!)
    )
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        tabs.forEachIndexed { index, tab ->
            Button(
                onClick = {
                    selectedTab.value = index
                    scoutingNavController.navigate(tab.route) { launchSingleTop = true }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedTab.value == index)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.secondary
                )
            ) {
                if (useIcons) {
                    Icon(painter = painterResource(tab.iconRes), contentDescription = tab.label)
                } else {
                    Text(tab.label)
                }
            }
        }
    }
}

@Composable
fun ScoutingScreen() {
    val vm: MatchScoutingViewModel = viewModel()
    val scoutingNavController = rememberNavController()
    val selectedTab = remember { mutableStateOf(0) }
    Scaffold(
        topBar = { MatchScoutingTopNav(scoutingNavController, selectedTab) }
    ) { innerPadding ->
        Column(Modifier.padding(innerPadding)) {
            ScoutingScreenNavHost(vm, scoutingNavController)
        }
    }
}

@Composable
fun ScoutingScreenStart(vm: MatchScoutingViewModel) {
    Column(Modifier
        .fillMaxHeight()
        .padding(top = 24.dp, start = 16.dp, end = 16.dp)) {
        TextFieldItem(vm.name, "Name - First Last")
        TextFieldItem(vm.robotNumber, "Robot #")
        TextFieldItem(vm.matchNumber, "Match #")
        SegmentedSelector("Did the robot show up?", listOf("Yes", "No"), vm.robotShowedUp)
        SegmentedSelector("Starting position?", listOf("Far Side", "Middle", "Close Side", "N/A"), vm.startPosition)
    }
}

@Composable
fun ScoutingScreenAuton(vm: MatchScoutingViewModel) {
    Column(Modifier.padding(top = 24.dp, start = 16.dp, end = 16.dp)) {
        SegmentedSelector("Left starting position?", listOf("Yes","No"), vm.autonLeftStart)
        CounterItem("Processor Score", vm.autonProcessor)
        CounterItem("NET Score", vm.autonNet)
        CounterItem("L1 Score", vm.autonL1)
        CounterItem("L2 Score", vm.autonL2)
        CounterItem("L3 Score", vm.autonL3)
        CounterItem("L4 Score", vm.autonL4)
    }
}

@Composable
fun ScoutingScreenTeleop(vm: MatchScoutingViewModel) {
    Column(Modifier.padding(top = 24.dp, start = 16.dp, end = 16.dp)) {
        CounterItem("Processor TOTAL", vm.teleopProcessor)
        CounterItem("NET TOTAL", vm.teleopNet)
        CounterItem("L1 TOTAL", vm.teleopL1)
        CounterItem("L2 TOTAL", vm.teleopL2)
        CounterItem("L3 TOTAL", vm.teleopL3)
        CounterItem("L4 TOTAL", vm.teleopL4)
        SegmentedSelector("Climb BARGE?", listOf("Yes","No"), vm.climbBarge)
        SegmentedSelector("Climb DEEP/SHALLOW/N/A?", listOf("DEEP","SHALLOW","N/A"), vm.climbCage)
        SegmentedSelector("Park under BARGE?", listOf("Yes","No"), vm.parkedBarge)
    }
}

@Composable
fun ScoutingScreenClosingComments(vm: MatchScoutingViewModel) {
    Column(Modifier.padding(top = 24.dp, start = 16.dp, end = 16.dp)) {
        SegmentedSelector("Playstyle", listOf("Scorer","Passer","Defense","Hybrid","N/A"), vm.playstyle)
        CounterItem("Penalties", vm.penalties)
        SegmentedSelector("Movement Skill 1-5", listOf("1","2","3","4","5"), vm.movementSkill)
        SegmentedSelector("ALGAE/CORAL stuck?", listOf("Yes","No"), vm.stuckPieces)
        TextFieldItem(vm.otherComments, "Other Comments")
        Spacer(Modifier.height(16.dp))
        Button(onClick = { /* Submit logic */ }) { Text("SUBMIT") }
    }
}

//--------------------------------
// PIT SCOUTING
//--------------------------------
@Composable
fun PitScoutingForm() {
    val vm: PitScoutingViewModel = viewModel()
    Column(
        Modifier
            .fillMaxHeight()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("Pit Scouting Form")
        TextFieldItem(vm.name, "Name")
        TextFieldItem(vm.teamNumber, "Team #")
        TextFieldItem(vm.driveExperience, "Drive team experience (1-5)")
        TextFieldItem(vm.autonsAvailable, "Autons available")
        TextFieldItem(vm.scoringFocus, "Scoring focus (Coral/Barge/Processor)")
        TextFieldItem(vm.piecesPerMatch, "Pieces scored per match")
        SegmentedSelector("Playstyle?", listOf("N", "Y"), vm.playstyle)
        TextFieldItem(vm.otherStrategies, "Other notable strategies")
        TextFieldItem(vm.robotWeight, "Robot weight")
        Spacer(Modifier.height(16.dp))
        Button(onClick = { /* Submit logic */ }) { Text("SUBMIT") }
    }
}

//--------------------------------
// OTHER SCREENS
//--------------------------------
@Composable
fun CycleTimeForm() {
    val vm: CycleTimeViewModel = viewModel()
    Column(Modifier.padding(16.dp)) {
        Text("Cycle Time Form")
        TextFieldItem(vm.name, "Name")
        TextFieldItem(vm.robotNumber, "Robot #")
        TextFieldItem(vm.matchNumber, "Match #")
        TextFieldItem(vm.cycleTime, "Cycle Time")
        TextFieldItem(vm.otherComments, "Other comments")
        Spacer(Modifier.height(16.dp))
        Button(onClick = { /* Submit logic */ }) { Text("SUBMIT") }
    }
}

@Composable
fun FAQScreen() {
    Column(Modifier.padding(16.dp)) {
        Text("FAQ")
        Spacer(Modifier.height(10.dp))
        Text("Q: What is the starting line?")
        Text("A: The black one nearest to the robots, NOT the colored ones")
        Text("Q: I keep getting notifications for wrong matches. Why?")
        Text("A: Likely updating schedule mid-competition, refresh app or wait")
        Text("Q: How do we know which robot is Red 1?")
        Text("A: Red 1 is the first number on the big screen, NOT field order")
        Text("Q: Far side / middle / processor side?")
        Text("A: Far side = further, middle = middle, processor side = near amp")
    }
}

@Composable
fun ScheduleScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Schedule Screen")
    }
}

@Composable
fun AccountScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Account / Admin Screen")
    }
}
