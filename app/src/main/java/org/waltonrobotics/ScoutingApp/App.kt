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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.waltonrobotics.ScoutingApp.viewmodel.CycleTimeViewModel
import org.waltonrobotics.ScoutingApp.viewmodel.MatchScoutingViewModel
import org.waltonrobotics.ScoutingApp.viewmodel.PitScoutingViewModel

//--------------------------------
// SCREENS
//--------------------------------
sealed class AppScreen(val route: String) {
    object MainScreen : AppScreen("MainScreen")
    object ScoutingScreen : AppScreen("ScoutingScreen")
    object PitScoutingForm : AppScreen("PitScoutingForm")
    object CycleTimeForm : AppScreen("CycleTimeForm")
    object FAQScreen : AppScreen("FAQScreen")
    object ScheduleScreen : AppScreen("ScheduleScreen")
    object AccountScreen : AppScreen("AccountScreen")

    // SCOUTING SCREENS
    sealed class Scouting(route: String) : AppScreen(route) {
        object Start : Scouting("ScoutingScreenStart")
        object Auton : Scouting("ScoutingScreenAuton")
        object Teleop : Scouting("ScoutingScreenTeleop")
        object ClosingComments : Scouting("ScoutingScreenClosingComments")
    }
}

// --------------------------------
// HELPERS
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
fun NumericalFieldItem(state: MutableState<String>, label: String) {
    OutlinedTextField(
        value = state.value,
        onValueChange = { if (it.isDigitsOnly()) state.value = it },
        label = { Text(label) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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
            Icon(painterResource(R.drawable.remove), contentDescription = "Remove")
        }
        Text(state.value.toString(), fontSize = 18.sp)
        IconButton(onClick = { state.value++ }) {
            Icon(painterResource(R.drawable.add), contentDescription = "Add")
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
                    containerColor =
                        if (selectedState.value == index)
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
// NAVIGATION!!!!!!! bird.jpg
//--------------------------------

@Composable
fun AppNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController,
        startDestination = AppScreen.MainScreen.route, // Using sealed class here
        modifier = modifier
    ) {
        composable(AppScreen.MainScreen.route) { MainScreen(navController) }
        composable(AppScreen.ScoutingScreen.route) { ScoutingScreen() }
        composable(AppScreen.ScheduleScreen.route) { ScheduleScreen() }
        composable(AppScreen.AccountScreen.route) { AccountScreen() }
        composable(AppScreen.PitScoutingForm.route) { PitScoutingForm() }
        composable(AppScreen.CycleTimeForm.route) { CycleTimeForm() }
        composable(AppScreen.FAQScreen.route) { FAQScreen() }
    }
}


@Composable
fun ScoutingScreenNavHost(viewModel: MatchScoutingViewModel, navController: NavHostController) {
    NavHost(navController, startDestination = AppScreen.Scouting.Start.route) {
        composable(AppScreen.Scouting.Start.route) { ScoutingScreenStart(viewModel) }
        composable(AppScreen.Scouting.Auton.route) { ScoutingScreenAuton(viewModel) }
        composable(AppScreen.Scouting.Teleop.route) { ScoutingScreenTeleop(viewModel) }
        composable(AppScreen.Scouting.ClosingComments.route) {
            ScoutingScreenClosingComments(
                viewModel
            )
        }
    }
}


//--------------------------------
// BOTTOM NAV
//--------------------------------
data class BottomNavItem(val label: String, val iconRes: Int, val route: String)

@Composable
fun BottomNavBar(navController: NavHostController) {
    val items = listOf(
        BottomNavItem("Home", R.drawable.home, AppScreen.MainScreen.route),
        BottomNavItem("Schedule", R.drawable.calendar_clock, AppScreen.ScheduleScreen.route),
        BottomNavItem("Account", R.drawable.person, AppScreen.AccountScreen.route)
    )

    val currentRoute =
        navController.currentBackStackEntryAsState().value?.destination?.route

    NavigationBar {
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


//--------------------------------
// APP
//--------------------------------
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

//--------------------------------
// MAIN SCREEN
//--------------------------------
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
            Icon(painterResource(R.drawable.pitscouting), null)
            Spacer(Modifier.width(8.dp))
            Text("Pit Scouting")
        }

        Button(
            onClick = { navController.navigate(AppScreen.CycleTimeForm.route) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp)
        ) {
            Icon(painterResource(R.drawable.timer), null)
            Spacer(Modifier.width(8.dp))
            Text("Cycle Time")
        }

        Button(
            onClick = { navController.navigate(AppScreen.ScoutingScreen.route) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp)
        ) {
            Icon(painterResource(R.drawable.robot), null)
            Spacer(Modifier.width(8.dp))
            Text("Match Scouting")
        }

        Button(
            onClick = { navController.navigate(AppScreen.FAQScreen.route) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(painterResource(R.drawable.questionmark), null)
            Spacer(Modifier.width(8.dp))
            Text("FAQ")
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
        ScoutingTab("Start", R.drawable.start, AppScreen.Scouting.Start.route),
        ScoutingTab("Auton", R.drawable.robot, AppScreen.Scouting.Auton.route),
        ScoutingTab("Teleop", R.drawable.controller, AppScreen.Scouting.Teleop.route),
        ScoutingTab("Closing", R.drawable.stop, AppScreen.Scouting.ClosingComments.route)
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        tabs.forEachIndexed { index, tab ->
            Button(
                onClick = {
                    selectedTab.value = index
                    scoutingNavController.navigate(tab.route) { launchSingleTop = true }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor =
                        if (selectedTab.value == index)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.secondary
                )
            ) {
                if (useIcons) {
                    Icon(painterResource(tab.iconRes), null)
                } else {
                    Text(tab.label)
                }
            }
        }
    }
}

@Composable
fun ScoutingScreen() {
    val viewModel: MatchScoutingViewModel = viewModel()
    val scoutingNavController = rememberNavController()
    val selectedTab = remember { mutableIntStateOf(0) }

    Column(Modifier.fillMaxSize()) {
        MatchScoutingTopNav(scoutingNavController, selectedTab)
        ScoutingScreenNavHost(viewModel, scoutingNavController)
    }
}

@Composable
fun ScoutingScreenStart(viewModel: MatchScoutingViewModel) {
    Column(
        Modifier
            .fillMaxHeight()
            .padding(top = 24.dp, start = 16.dp, end = 16.dp)
    ) {
        // TODO ADD TIMESTAMP
        TextFieldItem(viewModel.name, "Name - First Last (Ex. Carson Beck)")
        NumericalFieldItem(viewModel.robotNumber, "Robot # (Ex. 2974)")
        NumericalFieldItem(viewModel.matchNumber, "Match #")
        SegmentedSelector("Did the robot show up?", listOf("Yes", "No"), viewModel.robotShowedUp)
        SegmentedSelector(
            "Starting position?",
            listOf("Far Side", "Middle", "Close Side", "N/A"),
            viewModel.startPosition
        )
    }
}

@Composable
fun ScoutingScreenAuton(viewModel: MatchScoutingViewModel) {
    Column(Modifier.padding(top = 24.dp, start = 16.dp, end = 16.dp)) {
        SegmentedSelector("Left starting position?", listOf("Yes", "No"), viewModel.autonLeftStart)
        CounterItem("Processor Score", viewModel.autonProcessor)
        CounterItem("NET Score", viewModel.autonNet)
        CounterItem("L1 Score", viewModel.autonL1)
        CounterItem("L2 Score", viewModel.autonL2)
        CounterItem("L3 Score", viewModel.autonL3)
        CounterItem("L4 Score", viewModel.autonL4)
    }
}

@Composable
fun ScoutingScreenTeleop(viewModel: MatchScoutingViewModel) {
    Column(Modifier.padding(top = 24.dp, start = 16.dp, end = 16.dp)) {
        CounterItem("Processor TOTAL", viewModel.teleopProcessor)
        CounterItem("NET TOTAL", viewModel.teleopNet)
        CounterItem("L1 TOTAL", viewModel.teleopL1)
        CounterItem("L2 TOTAL", viewModel.teleopL2)
        CounterItem("L3 TOTAL", viewModel.teleopL3)
        CounterItem("L4 TOTAL", viewModel.teleopL4)
        SegmentedSelector("Climb BARGE?", listOf("Yes", "No"), viewModel.climbBarge)
        SegmentedSelector(
            "Climb DEEP/SHALLOW/N/A?",
            listOf("DEEP", "SHALLOW", "N/A"),
            viewModel.climbCage
        )
        SegmentedSelector("Park under BARGE?", listOf("Yes", "No"), viewModel.parkedBarge)
    }
}

@Composable
fun ScoutingScreenClosingComments(viewModel: MatchScoutingViewModel) {
    Column(Modifier.padding(top = 24.dp, start = 16.dp, end = 16.dp)) {
        SegmentedSelector(
            "Playstyle",
            listOf("Scorer", "Passer", "Defense", "Hybrid", "N/A"),
            viewModel.playstyle
        )
        CounterItem("Penalties", viewModel.penalties)
        SegmentedSelector(
            "Movement Skill 1-5",
            listOf("1", "2", "3", "4", "5"),
            viewModel.movementSkill
        )
        SegmentedSelector("ALGAE/CORAL stuck?", listOf("Yes", "No"), viewModel.stuckPieces)
        TextFieldItem(viewModel.otherComments, "Other Comments")
        Spacer(Modifier.height(16.dp))
        Button(onClick = { /* Submit logic */ }) { Text("SUBMIT") }
    }
}

//--------------------------------
// PIT SCOUTING
//--------------------------------
@Composable
fun PitScoutingForm() {
    val viewModel: PitScoutingViewModel = viewModel()
    Column(
        Modifier
            .fillMaxHeight()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("Pit Scouting Form")
        TextFieldItem(viewModel.name, "Scouter's Name")
        // TODO add timestamps
        NumericalFieldItem(viewModel.teamNumber, "Team Number?")
        TextFieldItem(
            viewModel.driveExperience,
            "How much experience does your drive team have 1-5? (1 being bad, 5 being good)"
        )
        TextFieldItem(
            viewModel.autonsAvailable,
            "What autons do they have? (Leave, scoring, where do they score, their path, etc."
        )
        TextFieldItem(
            viewModel.scoringFocus,
            "Do they focus on scoring in the Coral, the Barge, or the Processor"
        )
        NumericalFieldItem(viewModel.piecesPerMatch, "Pieces scored per match (in total)?")
        SegmentedSelector("Playstyle?", listOf("N", "Y"), viewModel.playstyle)
        TextFieldItem(viewModel.otherStrategies, "Any other notable strategies or points to note?")
        NumericalFieldItem(viewModel.robotWeight, "Robot weight")
        //TODO
        // ADD IMAGE INPUT
        Spacer(Modifier.height(16.dp))

        Button(onClick = { /* Submit logic */ }) { Text("SUBMIT") }
    }
}

//--------------------------------
// OTHER SCREENS
//--------------------------------
@Composable
fun CycleTimeForm() {
    val viewModel: CycleTimeViewModel = viewModel()
    Column(Modifier.padding(16.dp)) {
        Text("Cycle Time Form")
        //TODO add timestamps
        TextFieldItem(viewModel.name, "Name - First, Last (Ex. Derek Spevak)")
        NumericalFieldItem(viewModel.robotNumber, "Robot # (Ex. 2974)")
        NumericalFieldItem(viewModel.matchNumber, "Match #")
        TextFieldItem(viewModel.cycleTime, "Cycle Time")
        TextFieldItem(
            viewModel.otherComments,
            "Other comments (Were their cycle times inconsistent)?"
        )
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