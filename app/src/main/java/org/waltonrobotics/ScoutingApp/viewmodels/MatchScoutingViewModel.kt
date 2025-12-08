package org.waltonrobotics.ScoutingApp.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class MatchScoutingViewModel : ViewModel() {
    // Start screen
    val name = mutableStateOf("")
    val robotNumber = mutableStateOf("")
    val matchNumber = mutableStateOf("")
    val robotShowedUp = mutableStateOf(0)
    val startPosition = mutableStateOf(1)

    // Auton
    val autonLeftStart = mutableStateOf(0)
    val autonProcessor = mutableStateOf(0)
    val autonNet = mutableStateOf(0)
    val autonL1 = mutableStateOf(0)
    val autonL2 = mutableStateOf(0)
    val autonL3 = mutableStateOf(0)
    val autonL4 = mutableStateOf(0)

    // Teleop
    val teleopProcessor = mutableStateOf(0)
    val teleopNet = mutableStateOf(0)
    val teleopL1 = mutableStateOf(0)
    val teleopL2 = mutableStateOf(0)
    val teleopL3 = mutableStateOf(0)
    val teleopL4 = mutableStateOf(0)
    val climbBarge = mutableStateOf(1)
    val climbCage = mutableStateOf(2)
    val parkedBarge = mutableStateOf(0)

    // Closing Comments
    val playstyle = mutableStateOf(4)
    val penalties = mutableStateOf(0)
    val movementSkill = mutableStateOf(3)
    val stuckPieces = mutableStateOf(1)
    val otherComments = mutableStateOf("")
}
