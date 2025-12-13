package org.waltonrobotics.ScoutingApp.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class PitScoutingViewModel : ViewModel() {
    val name = mutableStateOf("")
    val teamNumber = mutableStateOf("")
    val driveExperience = mutableStateOf("")
    val autonsAvailable = mutableStateOf("")
    val scoringFocus = mutableStateOf("")
    val piecesPerMatch = mutableStateOf("")
    val playstyle = mutableStateOf(0)
    val otherStrategies = mutableStateOf("")
    val robotWeight = mutableStateOf("")
}