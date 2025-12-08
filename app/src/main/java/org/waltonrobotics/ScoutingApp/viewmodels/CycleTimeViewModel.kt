package org.waltonrobotics.ScoutingApp.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class CycleTimeViewModel : ViewModel() {
    val name = mutableStateOf("")
    val robotNumber = mutableStateOf("")
    val matchNumber = mutableStateOf("")
    val cycleTime = mutableStateOf("")
    val otherComments = mutableStateOf("")
}
