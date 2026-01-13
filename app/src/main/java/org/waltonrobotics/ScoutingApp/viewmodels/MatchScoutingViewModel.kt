package org.waltonrobotics.ScoutingApp.viewmodel

import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize

/**
 * UI state representing the entire Match Scouting form.
 * Immutable data class so the ViewModel exposes a single source-of-truth StateFlow.
 */
@Parcelize
data class MatchScoutingState(
    val name: String = "",
    val robotNumber: String = "",
    val matchNumber: String = "",
    val robotShowedUpIndex: Int? = null,
    val startPositionIndex: Int? = null,

    val autonLeftStartIndex: Int? = null,
    val autonProcessor: Int = 0,
    val autonNet: Int = 0,
    val autonL1: Int = 0,
    val autonL2: Int = 0,
    val autonL3: Int = 0,
    val autonL4: Int = 0,

    val teleopProcessor: Int = 0,
    val teleopNet: Int = 0,
    val teleopL1: Int = 0,
    val teleopL2: Int = 0,
    val teleopL3: Int = 0,
    val teleopL4: Int = 0,
    val climbBargeIndex: Int? = null,
    val climbCageIndex: Int? = null,
    val parkedBargeIndex: Int? = null,

    val playstyleIndex: Int? = null,
    val penalties: String = "0",
    val movementSkillIndex: Int? = null,
    val stuckPiecesIndex: Int? = null,
    val otherComments: String = ""
) : Parcelable {
    fun isValidForSubmit(): Boolean {
        return name.isNotBlank()
                && robotNumber.toIntOrNull() != null
                && matchNumber.toIntOrNull() != null
    }
}


sealed class MatchScoutingEvent {
    object SubmitSuccess : MatchScoutingEvent()
    data class ShowError(val message: String) : MatchScoutingEvent()
}

class MatchScoutingViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val STATE_KEY = "match_scouting_state"

    private val _uiState: MutableStateFlow<MatchScoutingState> =
        MutableStateFlow(savedStateHandle.get<MatchScoutingState>(STATE_KEY) ?: MatchScoutingState())
    val uiState: StateFlow<MatchScoutingState> = _uiState.asStateFlow()

    private val _events = Channel<MatchScoutingEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    private val _scouterNames = MutableStateFlow<List<String>>(emptyList())
    val scouterNames: StateFlow<List<String>> = _scouterNames


    init {
        viewModelScope.launch {
            _uiState.collect { state ->
                savedStateHandle[STATE_KEY] = state
            }
        }
    }

    // --- Simple field updaters (normalization/validation close to model) ---
    fun updateName(value: String) = update { it.copy(name = value) }

    // keep only digits for numeric fields; UI can also set keyboard type
    fun updateRobotNumber(value: String) = update { it.copy(robotNumber = value.filter { ch -> ch.isDigit() }) }
    fun updateMatchNumber(value: String) = update { it.copy(matchNumber = value.filter { ch -> ch.isDigit() }) }

    fun updateOtherComments(value: String) = update { it.copy(otherComments = value) }

    // --- Selection setters (segmented selectors / radio-like state) ---
    fun setRobotShowedUp(index: Int?) = update { it.copy(robotShowedUpIndex = index) }
    fun setStartPosition(index: Int?) = update { it.copy(startPositionIndex = index) }
    fun setAutonLeftStart(index: Int?) = update { it.copy(autonLeftStartIndex = index) }

    fun setClimbBarge(index: Int?) = update { it.copy(climbBargeIndex = index) }
    fun setClimbCage(index: Int?) = update { it.copy(climbCageIndex = index) }
    fun setParkedBarge(index: Int?) = update { it.copy(parkedBargeIndex = index) }

    fun setPlaystyle(index: Int?) = update { it.copy(playstyleIndex = index) }
    fun setMovementSkill(index: Int?) = update { it.copy(movementSkillIndex = index) }
    fun setStuckPieces(index: Int?) = update { it.copy(stuckPiecesIndex = index) }

    fun incrementAutonProcessor() = update { it.copy(autonProcessor = it.autonProcessor + 1) }
    fun decrementAutonProcessor() = update { it.copy(autonProcessor = maxOf(0, it.autonProcessor - 1)) }

    fun incrementAutonNet() = update { it.copy(autonNet = it.autonNet + 1) }
    fun decrementAutonNet() = update { it.copy(autonNet = maxOf(0, it.autonNet - 1)) }

    fun incrementAutonL1() = update { it.copy(autonL1 = it.autonL1 + 1) }
    fun decrementAutonL1() = update { it.copy(autonL1 = maxOf(0, it.autonL1 - 1)) }

    fun incrementAutonL2() = update { it.copy(autonL2 = it.autonL2 + 1) }
    fun decrementAutonL2() = update { it.copy(autonL2 = maxOf(0, it.autonL2 - 1)) }

    fun incrementAutonL3() = update { it.copy(autonL3 = it.autonL3 + 1) }
    fun decrementAutonL3() = update { it.copy(autonL3 = maxOf(0, it.autonL3 - 1)) }

    fun incrementAutonL4() = update { it.copy(autonL4 = it.autonL4 + 1) }
    fun decrementAutonL4() = update { it.copy(autonL4 = maxOf(0, it.autonL4 - 1)) }

    fun incrementTeleopProcessor() = update { it.copy(teleopProcessor = it.teleopProcessor + 1) }
    fun decrementTeleopProcessor() = update { it.copy(teleopProcessor = maxOf(0, it.teleopProcessor - 1)) }

    fun incrementTeleopNet() = update { it.copy(teleopNet = it.teleopNet + 1) }
    fun decrementTeleopNet() = update { it.copy(teleopNet = maxOf(0, it.teleopNet - 1)) }

    fun incrementTeleopL1() = update { it.copy(teleopL1 = it.teleopL1 + 1) }
    fun decrementTeleopL1() = update { it.copy(teleopL1 = maxOf(0, it.teleopL1 - 1)) }

    fun incrementTeleopL2() = update { it.copy(teleopL2 = it.teleopL2 + 1) }
    fun decrementTeleopL2() = update { it.copy(teleopL2 = maxOf(0, it.teleopL2 - 1)) }

    fun incrementTeleopL3() = update { it.copy(teleopL3 = it.teleopL3 + 1) }
    fun decrementTeleopL3() = update { it.copy(teleopL3 = maxOf(0, it.teleopL3 - 1)) }

    fun incrementTeleopL4() = update { it.copy(teleopL4 = it.teleopL4 + 1) }
    fun decrementTeleopL4() = update { it.copy(teleopL4 = maxOf(0, it.teleopL4 - 1)) }

    fun updatePenalties(value: String) = update {
        it.copy(penalties = value.filter { ch -> ch.isDigit() })
    }

    fun decrementPenalties() = update {
        val current = it.penalties.toIntOrNull() ?: 0
        it.copy(penalties = maxOf(0, current - 1).toString())
    }

    fun incrementPenalties() = update {
        val current = it.penalties.toIntOrNull() ?: 0
        it.copy(penalties = (current + 1).toString())
    }


    // --- Submit: validate, persist (TODO), emit event ---
    fun submit() {
        viewModelScope.launch {
            val state = _uiState.value
            if (!state.isValidForSubmit()) {
                _events.send(MatchScoutingEvent.ShowError("Name, robot # and match # are required"))
                return@launch
            }

            try {
                // TODO: Save logic
                _events.send(MatchScoutingEvent.SubmitSuccess)

                // Auto-increment match number for the next round
                val nextMatch = (state.matchNumber.toIntOrNull() ?: 0) + 1
                prepNewMatch(nextMatch.toString(), "")
            } catch (t: Throwable) {
                _events.send(MatchScoutingEvent.ShowError("Failed to save"))
            }
        }
    }

    fun resetForm() {
        val currentName = _uiState.value.name
        _uiState.value = MatchScoutingState(name = currentName)
    }

    private fun update(reducer: (MatchScoutingState) -> MatchScoutingState) {
        _uiState.update { current -> reducer(current) }
    }


    fun prepNewMatch(matchNum: String, robotNum: String) {
        android.util.Log.d("SCOUTING_DEBUG", "Prep called with: Match $matchNum, Robot $robotNum")
        _uiState.update { currentState ->
            // We create a fresh state object, but copy the 'name' over
            MatchScoutingState(
                name = currentState.name, // Keeps scouter name persistent
                matchNumber = matchNum,
                robotNumber = robotNum,
                robotShowedUpIndex = 0,    // Defaults to "Yes"
                startPositionIndex = 4,    // Defaults to "N/A"

                // Explicitly reset all scoring counters to 0
                autonLeftStartIndex = 1,   // Defaults to "No"
                autonProcessor = 0,
                autonNet = 0,
                autonL1 = 0,
                autonL2 = 0,
                autonL3 = 0,
                autonL4 = 0,

                teleopProcessor = 0,
                teleopNet = 0,
                teleopL1 = 0,
                teleopL2 = 0,
                teleopL3 = 0,
                teleopL4 = 0,

                // Reset comments and endgame
                climbCageIndex = 0,
                climbBargeIndex = 0,
                parkedBargeIndex = 0,
                otherComments = ""
            )
        }
    }


}