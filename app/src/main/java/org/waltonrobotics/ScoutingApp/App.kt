package org.waltonrobotics.ScoutingApp

// IMPORTS
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable

//--------------------------------
// PAGES
//--------------------------------
@Serializable
object MainScreen
@Serializable
object ScoutingScreen
@Serializable
object ScoutingScreenStart
@Serializable
object ScoutingScreenAuton
@Serializable
object ScoutingScreenTeleop
@Serializable
object ScoutingScreenClosingComments
@Serializable
object CycleTimeForm
@Serializable
object PitScoutingForm
@Serializable
object FAQScreen
@Serializable
object ScheduleScreen
@Serializable
object AccountScreen

//--------------------------------
// BOILERPLATE INPUT THINGS
//--------------------------------
@Composable
fun CounterItem(label: String) {
    var value by remember { mutableStateOf(0) }
    Text(label)
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(onClick = { if (value > 0) value-- }) { Text("-") }
        Text(value.toString())
        Button(onClick = { value++ }) { Text("+") }
    }
}

@Composable
fun SegmentedSelector(label: String, options: List<String>) {
    SegmentedSelector(label, options, defaultIndex = 0)
}

@Composable
fun SegmentedSelector(label: String, options: List<String>, defaultIndex: Int) {
    var selected by remember { mutableIntStateOf(defaultIndex) }
    Text(label)
    SingleChoiceSegmentedButtonRow {
        options.forEachIndexed { index, text ->
            SegmentedButton(
                selected = index == selected,
                onClick = { selected = index },
                shape = SegmentedButtonDefaults.itemShape(index, options.size),
                label = { Text(text, fontSize = 12.sp) }
            )
        }
    }
}

//--------------------------------
// NAV HOSTS
//--------------------------------
@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController, startDestination = MainScreen) {
        composable<MainScreen> { MainScreen(navController) }
        composable<ScoutingScreen> { ScoutingScreen() }
        composable<ScheduleScreen> { ScheduleScreen() }
        composable<AccountScreen> { AccountScreen() }
        composable<PitScoutingForm> { PitScoutingForm() }
        composable<CycleTimeForm> { CycleTimeForm() }
        composable<FAQScreen> { FAQScreen() }
    }
}

@Composable
fun ScoutingScreenNavHost(navController: NavHostController) {
    NavHost(navController, startDestination = ScoutingScreenStart) {
        composable<ScoutingScreenStart> { ScoutingScreenStart() }
        composable<ScoutingScreenAuton> { ScoutingScreenAuton() }
        composable<ScoutingScreenTeleop> { ScoutingScreenTeleop() }
        composable<ScoutingScreenClosingComments> { ScoutingScreenClosingComments() }
    }
}

//--------------------------------
// APP SUPERSTRUCTURE
//--------------------------------
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun App() {
    val navController = rememberNavController()
    Scaffold(
        modifier = Modifier.padding(top = 10.dp),
        bottomBar = { BottomNavBar(navController) }
    ) { AppNavHost(navController) }
}

//--------------------------------
// BOTTOM NAVBAR
//--------------------------------
@Composable
fun BottomNavBar(navController: NavHostController) {
    NavigationBar {
        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate(MainScreen) },
            icon = { Icon(painterResource(R.drawable.home), "Home") }
        )
        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate(ScheduleScreen) },
            icon = { Icon(painterResource(R.drawable.calendar_clock), "Schedule") }
        )
        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate(AccountScreen) },
            icon = { Icon(painterResource(R.drawable.person), "Account") }
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
            .padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Hello! Click on the form you need!", modifier = Modifier.padding(bottom = 16.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Button(onClick = { navController.navigate(PitScoutingForm) }) {
                    Icon(painterResource(R.drawable.pitscouting), contentDescription = "Pit Scouting")
                }
                Text("Pit Scouting")
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Button(onClick = { navController.navigate(CycleTimeForm) }) {
                    Icon(painterResource(R.drawable.timer), contentDescription = "Cycle Time")
                }
                Text("Cycle Time")
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Button(onClick = { navController.navigate(ScoutingScreen) }) {
                    Icon(painterResource(R.drawable.robot), contentDescription = "Match Scouting")
                }
                Text("Match Scouting")
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Button(onClick = { navController.navigate(FAQScreen) }) {
                    Icon(painterResource(R.drawable.questionmark), contentDescription = "FAQ")
                }
                Text("FAQ")
            }
        }
    }
}




//--------------------------------
// MATCH SCOUTING
//--------------------------------
@Composable
fun ScoutingScreen() {
    val scoutingNavController = rememberNavController()
    Scaffold(
        modifier = Modifier.padding(top = 10.dp),
        topBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = false,
                    onClick = { scoutingNavController.navigate(ScoutingScreenStart) },
                    icon = { Icon(painterResource(R.drawable.robot), "Start") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { scoutingNavController.navigate(ScoutingScreenAuton) },
                    icon = { Icon(painterResource(R.drawable.robot), "Auton") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { scoutingNavController.navigate(ScoutingScreenTeleop) },
                    icon = { Icon(painterResource(R.drawable.controller), "Teleop") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { scoutingNavController.navigate(ScoutingScreenClosingComments) },
                    icon = { Icon(painterResource(R.drawable.stop), "Closing") }
                )
            }
        }
    ) { innerPadding ->
        Column(Modifier.padding(innerPadding)) {
            ScoutingScreenNavHost(scoutingNavController)
        }
    }
}

@Composable
fun ScoutingScreenStart() {
    Column(Modifier
        .fillMaxHeight()
        .padding(16.dp)) {
        Text("Name - First Last (Ex. Carson Beck)")
        TextField(state = rememberTextFieldState())
        Text("Robot #")
        TextField(state = rememberTextFieldState())
        Text("Match #")
        TextField(state = rememberTextFieldState())
        SegmentedSelector("Did the robot show up?", listOf("Yes", "No"))
        SegmentedSelector(
            "What was the initial starting position?",
            listOf("Far Side", "Middle", "Close Side", "N/A"),
            1
        )
    }
}

@Composable
fun ScoutingScreenAuton() {
    Column(Modifier.padding(16.dp)) {
        SegmentedSelector("Did the robot leave the starting position?", listOf("Yes", "No"))
        CounterItem("How many pieces did the robot SCORE in the PROCESSOR during auton?")
        CounterItem("How many pieces did the robot SCORE in the NET during auton?")
        CounterItem("How many pieces did the robot SCORE in L1 during auton?")
        CounterItem("How many pieces did the robot SCORE in L2 during auton?")
        CounterItem("How many pieces did the robot SCORE in L3 during auton?")
        CounterItem("How many pieces did the robot SCORE in L4 during auton?")
    }
}

@Composable
fun ScoutingScreenTeleop() {
    Column(Modifier.padding(16.dp)) {
        CounterItem("How many TOTAL pieces did the robot SCORE in the PROCESSOR?")
        CounterItem("How many TOTAL pieces did the robot SCORE in the NET?")
        CounterItem("How many TOTAL pieces did the robot SCORE in L1?")
        CounterItem("How many TOTAL pieces did the robot SCORE in L2?")
        CounterItem("How many TOTAL pieces did the robot SCORE in L3?")
        CounterItem("How many TOTAL pieces did the robot SCORE in L4?")
        SegmentedSelector("Did the robot climb onto the BARGE?", listOf("Yes", "No"), 1)
        SegmentedSelector(
            "Did the robot climb onto the DEEP or SHALLOW cage? Fully suspended off the ground",
            listOf("DEEP", "SHALLOW", "N/A"),
            2
        )
        SegmentedSelector("Did the robot park under the BARGE? (Within tape?)", listOf("Yes", "No"))
    }
}

@Composable
fun ScoutingScreenClosingComments() {
    Column(Modifier.padding(16.dp)) {
        SegmentedSelector("Robot Playstyle", listOf("Scorer", "Passer", "Defense", "Hybrid", "N/A"))
        CounterItem("How many penalties did they earn?")
        SegmentedSelector(
            "How good was their movement/driving skill (1-5)",
            listOf("1", "2", "3", "4", "5"),
            3
        )
        SegmentedSelector(
            "Did the robot ever get an ALGAE or CORAL stuck inside?",
            listOf("Yes", "No")
        )
        Text("Any other comments? (Defended, broken subsystems, etc)")
        TextField(state = rememberTextFieldState())
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { /* Submit logic */ }) { Text("SUBMIT") }
    }
}


//--------------------------------
// PIT SCOUTING FORM
//--------------------------------
@Composable
fun PitScoutingForm() {
    Column(modifier = Modifier
        .fillMaxHeight()
        .padding(16.dp)) {
        Text("Pit Scouting Form")
        Spacer(modifier = Modifier.height(10.dp))
        Text("Name")
        TextField(state = rememberTextFieldState())
        Text("Team #")
        TextField(state = rememberTextFieldState())
        Text("Drive team experience (1-5)")
        TextField(state = rememberTextFieldState())
        Text("Autons available")
        TextField(state = rememberTextFieldState())
        Text("Scoring focus (Coral/Barge/Processor)")
        TextField(state = rememberTextFieldState())
        Text("Pieces scored per match")
        TextField(state = rememberTextFieldState())
        SegmentedSelector("Playstyle?", listOf("N", "Y"))
        Text("Other notable strategies")
        TextField(state = rememberTextFieldState())
        Text("Robot weight")
        TextField(state = rememberTextFieldState())
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { /* Submit logic */ }) { Text("SUBMIT") }
    }
}

//--------------------------------
// CYCLE TIME FORM
//--------------------------------
@Composable
fun CycleTimeForm() {
    Column(modifier = Modifier
        .fillMaxHeight()
        .padding(16.dp)) {
        Text("Cycle Time Form")
        Text("Name")
        TextField(state = rememberTextFieldState())
        Text("Robot #")
        TextField(state = rememberTextFieldState())
        Text("Match #")
        TextField(state = rememberTextFieldState())
        Text("Cycle Time")
        TextField(state = rememberTextFieldState())
        Text("Other comments")
        TextField(state = rememberTextFieldState())
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { /* Submit logic */ }) { Text("SUBMIT") }
    }
}

//--------------------------------
// FAQ SCREEN
//--------------------------------
@Composable
fun FAQScreen() {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("FAQ")
        Spacer(modifier = Modifier.height(10.dp))
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

//--------------------------------
// SCHEDULE SCREEN
//--------------------------------
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

//--------------------------------
// ACCOUNT / ADMIN SCREEN
//--------------------------------
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
