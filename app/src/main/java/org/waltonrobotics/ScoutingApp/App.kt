package org.waltonrobotics.ScoutingApp


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable

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
object ScheduleScreen

@Serializable
object AccountScreen


@Composable
fun AppNavHost(
    navController: NavHostController,
) {
    NavHost(
        navController,
        startDestination = ScoutingScreen
    ) {
        composable<ScoutingScreen> {
            ScoutingScreen()
        }
        composable<ScheduleScreen> {
            ScheduleScreen()
        }
        composable<AccountScreen> {
            AccountScreen()
        }

    }
}

@Composable
fun ScoutingScreenNavHost(
    navController: NavHostController,
) {
    NavHost(
        navController,
        startDestination = ScoutingScreenStart()
    ) {
        composable<ScoutingScreenStart> {
            ScoutingScreenStart()
        }
        composable<ScoutingScreenAuton> {
            ScoutingScreenAuton()
        }
        composable<ScoutingScreenTeleop> {
            ScoutingScreenTeleop()
        }
        composable<ScoutingScreenClosingComments> {
            ScoutingScreenClosingComments()
        }
    }
}

@Composable
fun App() {

    val navController = rememberNavController()
    Scaffold(
        modifier = Modifier.padding(30.dp),
        bottomBar = {
            NavigationBar(windowInsets = NavigationBarDefaults.windowInsets) {
                NavigationBarItem(
                    selected = false,
                    onClick = {
                        navController.navigate(ScoutingScreen)
                    },
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.add),
                            contentDescription = "Scout",
                        )
                    },

                    )
                NavigationBarItem(
                    selected = false,
                    onClick = {
                        navController.navigate(ScheduleScreen)
                    },
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.calendar_clock),
                            contentDescription = "Schedule",
                        )
                    },
                )
                NavigationBarItem(
                    selected = false,
                    onClick = {
                        navController.navigate(AccountScreen)
                    },
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.person),
                            contentDescription = "Account",
                        )
                    },
                )
            }

        }

    ) { contentPadding ->
        AppNavHost(navController)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScoutingScreen() {
    val navControllerScouting = rememberNavController()
    Scaffold(
        topBar = {
            NavigationBar(windowInsets = NavigationBarDefaults.windowInsets) {
                NavigationBarItem(
                    selected = false,
                    onClick = {
                        navControllerScouting.navigate(ScoutingScreenStart)
                    },
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.start),
                            contentDescription = "Scouting Screen Start",
                        )
                    },
                )
                NavigationBarItem(
                    selected = false,
                    onClick = {
                        navControllerScouting.navigate(ScoutingScreenAuton)
                    },
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.robot),
                            contentDescription = "Scouting Screen Auton",
                        )
                    },
                    )
                NavigationBarItem(
                    selected = false,
                    onClick = {
                        navControllerScouting.navigate(ScoutingScreenTeleop)
                    },
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.controller),
                            contentDescription = "Teleop",
                        )
                    },
                )
                NavigationBarItem(
                    selected = false,
                    onClick = {
                        navControllerScouting.navigate(ScoutingScreenClosingComments)
                    },
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.stop),
                            contentDescription = "Closing Comments",
                        )
                    },
                )
            }

        }
    ) {

        ScoutingScreenNavHost(navControllerScouting)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScoutingScreenStart() {
    Column {
        Text("Name - First Last (Ex. Carson Beck)")
        TextField(
            state = rememberTextFieldState()
        )
        Text("Robot #")
        TextField(
            state = rememberTextFieldState()
        )
        Text("Match #")
        TextField(
            state = rememberTextFieldState()
        )
        Text("Did the robot show up?")

        var selectedIndexOfShowUp by remember { mutableIntStateOf(0) }
        val showUpOptions = listOf("Yes", "No")

        SingleChoiceSegmentedButtonRow {
            showUpOptions.forEachIndexed { index, label ->
                SegmentedButton(
                    shape = SegmentedButtonDefaults.itemShape(
                        index = index,
                        count = showUpOptions.size
                    ),
                    onClick = { selectedIndexOfShowUp = index },
                    selected = index == selectedIndexOfShowUp,
                    label = { Text(label, fontSize = 12.sp) }
                )
            }
        }


        Text("What was the initial starting position of the robot?")
        var selectedIndexOfStartingPos by remember { mutableIntStateOf(3) }
        val startingPosOptions = listOf("Far Side", "Middle", "Close Side", "N/A")

        SingleChoiceSegmentedButtonRow {
            startingPosOptions.forEachIndexed { index, label ->
                SegmentedButton(
                    shape = SegmentedButtonDefaults.itemShape(
                        index = index,
                        count = startingPosOptions.size
                    ),
                    onClick = { selectedIndexOfStartingPos = index },
                    selected = index == selectedIndexOfStartingPos,
                    label = {
                        Text(label, fontSize = 12.sp)
                    }
                )
            }
        }



    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScoutingScreenAuton() {
    Text("Did the robot leave the starting position?")
    var selectedIndexOfLeftStart by remember { mutableIntStateOf(0) }
    val leftStartOptions = listOf("Yes", "No")

    SingleChoiceSegmentedButtonRow {
        leftStartOptions.forEachIndexed { index, label ->
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(
                    index = index,
                    count = leftStartOptions.size
                ),
                onClick = { selectedIndexOfLeftStart = index },
                selected = index == selectedIndexOfLeftStart,
                label = { Text(label, fontSize = 12.sp) }
            )
        }
    }
    Text("How many pieces did the robot SCORE in the PROCESSOR during the auton period?", fontSize = 20.sp, color = Color.Blue)
    var numberOfPoints = 0
    Row {
        Column {
            Button(
                onClick = {
                    numberOfPoints++
                }
            ) {
                Text("-")
            }
        }
        Column {
            Text(numberOfPoints.toString())
        }

        Column {
            Button(
                onClick = {
                    numberOfPoints--
                }
            ) {
                Text("+")

            }
        }
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScoutingScreenTeleop() {

    Text("Teleop")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScoutingScreenClosingComments() {
    Text("Closing COmments")

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleScreen() {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Schedule")
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen() {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Account")
    }

}