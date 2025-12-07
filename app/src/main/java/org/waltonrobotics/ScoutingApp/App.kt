package org.waltonrobotics.ScoutingApp

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable

@Serializable object ScoutingScreen
@Serializable object ScoutingScreenStart
@Serializable object ScoutingScreenAuton
@Serializable object ScoutingScreenTeleop
@Serializable object ScoutingScreenClosingComments
@Serializable object ScheduleScreen
@Serializable object AccountScreen

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
fun SegmentedSelector(
    label: String,
    options: List<String>
) {
    SegmentedSelector(
        label = label,
        options = options,
        defaultIndex = 0
    )
}

@Composable
fun SegmentedSelector(
    label: String,
    options: List<String>,
    defaultIndex: Int
) {
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

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController, startDestination = ScoutingScreen) {
        composable<ScoutingScreen> { ScoutingScreen() }
        composable<ScheduleScreen> { ScheduleScreen() }
        composable<AccountScreen> { AccountScreen() }
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

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun App() {
    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier.padding(top = 10.dp, start = 10.dp),
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate(ScoutingScreen) },
                    icon = { Icon(painterResource(R.drawable.add), "Scout") }
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
    ) {
        AppNavHost(navController)
    }
}

@Composable
fun ScoutingScreen() {
    val nav = rememberNavController()

    Scaffold(
        topBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = false,
                    onClick = { nav.navigate(ScoutingScreenStart) },
                    icon = { Icon(painterResource(R.drawable.start), "Start") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { nav.navigate(ScoutingScreenAuton) },
                    icon = { Icon(painterResource(R.drawable.robot), "Auton") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { nav.navigate(ScoutingScreenTeleop) },
                    icon = { Icon(painterResource(R.drawable.controller), "Teleop") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { nav.navigate(ScoutingScreenClosingComments) },
                    icon = { Icon(painterResource(R.drawable.stop), "Closing") }
                )
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            ScoutingScreenNavHost(nav)
        }
    }
}

@Composable
fun ScoutingScreenStart() {
    Column(Modifier.fillMaxHeight()) {

        Text("Name - First Last (Ex. Carson Beck)")
        TextField(state = rememberTextFieldState())

        Text("Robot #")
        TextField(state = rememberTextFieldState())

        Text("Match #")
        TextField(state = rememberTextFieldState())

        SegmentedSelector(
            "Did the robot show up?",
            listOf("Yes", "No")
        )

        SegmentedSelector(
            "What was the initial starting position of the robot?",
            listOf("Far Side", "Middle", "Close Side", "N/A"),
            1
        )
    }
}

@Composable
fun ScoutingScreenAuton() {
    Column {

        SegmentedSelector(
            "Did the robot leave the starting position?",
            listOf("Yes", "No")
        )

        CounterItem("How many pieces did the robot SCORE in the PROCESSOR during the auton period?")
        CounterItem("How many pieces did the robot SCORE in the NET during the auton period?")
        CounterItem("How many pieces did the robot SCORE in L1 during the auton period?")
        CounterItem("How many pieces did the robot SCORE in L2 during the auton period?")
        CounterItem("How many pieces did the robot SCORE in L3 during the auton period?")
        CounterItem("How many pieces did the robot SCORE in L4 during the auton period?")
    }
}

@Composable
fun ScoutingScreenTeleop() {
    Column {

        CounterItem("How many TOTAL pieces did the robot SCORE in the PROCESSOR?")
        CounterItem("How many TOTAL pieces did the robot SCORE in the NET?")
        CounterItem("How many TOTAL pieces did the robot SCORE in L1?")
        CounterItem("How many TOTAL pieces did the robot SCORE in L2?")
        CounterItem("How many TOTAL pieces did the robot SCORE in L3?")
        CounterItem("How many TOTAL pieces did the robot SCORE in L4?")

        SegmentedSelector(
            "Did the robot climb onto the BARGE?",
            listOf("Yes", "No"),
            1
        )

        SegmentedSelector(
            "Did the robot climb onto the DEEP cage or the SHALLOW cage? Fully suspended off the ground; deep=low, shallow = high",
            listOf("DEEP", "SHALLOW", "N/A"),
            2
        )

        SegmentedSelector(
            "Did the robot park under the BARGE? (Did it park within the tape?)",
            listOf("Yes", "No")
        )
    }
}

@Composable
fun ScoutingScreenClosingComments() {
    Column {

        SegmentedSelector(
            "Robot Playstyle",
            listOf("Scorer", "Passer", "Defense", "Hybrid", "N/A")
        )

        CounterItem("How many penalties did they earn?")

        SegmentedSelector(
            "How good was their movement/driving skill (1-5)",
            listOf("1", "2", "3", "4", "5"),
            defaultIndex = 3
        )

        SegmentedSelector(
            "Did the robot ever get an ALGAE or CORAL stuck inside of it?",
            listOf("Yes", "No")
        )

        Row {
            Text("Any other comments? (did they get defended, does a subsystem look broken/appear to have stopped working, rearrange any pieces, how they played alliance, etc)")
        }
        Row {
            TextField(state = rememberTextFieldState())
        }
        Button(
            onClick = {

            },

        ){
            Text("SUBMIT")
        }
    }
}

@Composable
fun ScheduleScreen() {
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Schedule")
    }
}

@Composable
fun AccountScreen() {
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Account")
    }
}
