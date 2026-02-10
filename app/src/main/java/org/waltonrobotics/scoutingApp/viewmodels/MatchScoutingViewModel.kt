package org.waltonrobotics.scoutingApp.viewmodel

import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import org.waltonrobotics.scoutingApp.sheetsStuff.MatchDataDTO
import org.waltonrobotics.scoutingApp.sheetsStuff.MatchScoutingRetrofitClient


@Parcelize
data class CycleEntry(
    val id: Long = System.currentTimeMillis(),
    val pickupLocationIndex: Int? = null,
    val pickupAmountIndex: Int? = null,
    val shootingLocationIndex: Int? = null,
    val scoringRateIndex: Int? = null,
    val cycleTimeIndex: Int? = null
) : Parcelable

@Parcelize
data class MatchScoutingState(
    // Identifiers
    val name: String = "",
    val robotNumber: String = "",
    val matchNumber: String = "",

    // Pre-Match
    val hasRobotShowedUp: Boolean = true,
    val startPositionIndex: Int? = null,

    // Autonomous
    val autonCycles: List<CycleEntry> = emptyList(),
    val hasAutonLeftStart: Boolean = false,

    // Auton end
    val isAutonScoringFinished: Boolean = false,
    val autonClimbIndex: Int? = null,
    val autonClimbTimeIndex: Int? = null,
    val autonWindex: Int? = null,


    // Teleop
    val cycles: List<CycleEntry> = emptyList(),

    // Endgame (Rebuilt)
    val climbEndIndex: Int? = null,        // Yes/No
    val climbHeightIndex: Int? = null,     // L1, L2, L3, Attempted, N/A
    val climbTimeIndex: Int? = null,       // 5, 10, 15, N/A

    // Post-Match / Subjective (Rebuilt)
    val shootOnMoveIndex: Int? = null,     // 1-5
    val intakeOnMoveIndex: Int? = null,    // 1-5
    val intakeSpeedIndex: Int? = null,     // 1-5
    val defenseAbilityIndex: Int? = null,  // 1-5
    val driverSkillIndex: Int? = null,     // 1-5
    val otherComments: String = ""
) : Parcelable {
    fun isValidForSubmit(): Boolean {
        return name.isNotBlank() && robotNumber.toIntOrNull() != null && matchNumber.toIntOrNull() != null
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

    private val _uiState = MutableStateFlow(
        savedStateHandle.get<MatchScoutingState>(STATE_KEY) ?: MatchScoutingState()
    )
    val uiState: StateFlow<MatchScoutingState> = _uiState.asStateFlow()

    private val _events = Channel<MatchScoutingEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    private val climbHeights = listOf("L1", "L2", "L3", "Attempted", "N/A")
    private val climbTimes = listOf("5", "10", "15", "N/A")
    private val ratingScale = listOf("1", "2", "3", "4", "5")

    private val pickupLocations = listOf("Neutral Zone", "Alliance Zone")
    private val pickupAmounts = listOf("10", "20", "40")
    private val shootingLocations = listOf("Depot", "Tower", "Outpost", "Moving", "Neutral Zone")
    private val scoringRates = listOf("1", "2", "4")
    private val cycleTimes = listOf("5", "10", "15")

    private val autonPickupLocations = listOf("Preloaded", "Neutral Zone", "Alliance Zone", "Depot", "Outpost")

    init {
        viewModelScope.launch {
            _uiState.collect { state ->
                savedStateHandle[STATE_KEY] = state
            }
        }
    }

    private fun update(reducer: (MatchScoutingState) -> MatchScoutingState) {
        _uiState.update { current -> reducer(current) }
    }

    fun updateName(value: String) = update { it.copy(name = value) }
    fun updateRobotNumber(value: String) =
        update { it.copy(robotNumber = value.filter { it.isDigit() }) }

    fun updateMatchNumber(value: String) =
        update { it.copy(matchNumber = value.filter { it.isDigit() }) }

    fun setRobotShowedUp(value: Boolean) = update { it.copy(hasRobotShowedUp = value) }
    fun setStartPosition(index: Int?) = update { it.copy(startPositionIndex = index) }

    fun setAutonScoringFinished(value: Boolean) = update { it.copy(isAutonScoringFinished = value) }
    fun setAutonClimb(index: Int?) = update { it.copy(autonClimbIndex = index) }
    fun setAutonClimbTime(index: Int?) = update { it.copy(autonClimbTimeIndex = index) }
    fun setWinAuto(index: Int?) = update { it.copy(autonWindex = index) }

    fun addAutonCycle() = update { it.copy(autonCycles = it.autonCycles + CycleEntry()) }

    fun updateAutonCycle(updated: CycleEntry) = update { state ->
        state.copy(autonCycles = state.autonCycles.map { if (it.id == updated.id) updated else it })
    }
    fun addCycle() = update { it.copy(cycles = it.cycles + CycleEntry()) }

    fun updateCycle(updatedCycle: CycleEntry) = update { state ->
        state.copy(cycles = state.cycles.map { if (it.id == updatedCycle.id) updatedCycle else it })
    }

    fun setClimb(index: Int?) = update { it.copy(climbEndIndex = index) }
    fun setClimbHeight(index: Int?) = update { it.copy(climbHeightIndex = index)}
    fun setClimbTime(index: Int?) = update { it.copy(climbTimeIndex = index) }

    fun setShootOnMove(index: Int?) = update { it.copy(shootOnMoveIndex = index) }
    fun setIntakeOnMove(index: Int?) = update { it.copy(intakeOnMoveIndex = index) }
    fun setIntakeSpeed(index: Int?) = update { it.copy(intakeSpeedIndex = index) }
    fun setDefenseAbility(index: Int?) = update { it.copy(defenseAbilityIndex = index) }
    fun setDriverSkill(index: Int?) = update { it.copy(driverSkillIndex = index) }
    fun updateOtherComments(value: String) = update { it.copy(otherComments = value) }

    fun submit() {
        val state = _uiState.value
        if (!state.isValidForSubmit()) {
            viewModelScope.launch { _events.send(MatchScoutingEvent.ShowError("Check Team and Match Number")) }
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Format Teleop Cycles
                val cyclesFormatted = state.cycles.joinToString(" | ") { cycle ->
                    val pLoc = cycle.pickupLocationIndex?.let { pickupLocations.getOrNull(it) } ?: "N/A"
                    val pAmt = cycle.pickupAmountIndex?.let { pickupAmounts.getOrNull(it) } ?: "N/A"
                    val sLoc = cycle.shootingLocationIndex?.let { shootingLocations.getOrNull(it) } ?: "N/A"
                    val sRate = cycle.scoringRateIndex?.let { scoringRates.getOrNull(it) } ?: "N/A"
                    val cTime = cycle.cycleTimeIndex?.let { cycleTimes.getOrNull(it) } ?: "N/A"
                    "($pLoc, $pAmt, $sLoc, $sRate, $cTime)"
                }

                // Format Auton Cycles
                val autonCyclesFormatted = state.autonCycles.joinToString(" | ") { cycle ->
                    val pLoc = cycle.pickupLocationIndex?.let { autonPickupLocations.getOrNull(it) } ?: "N/A"
                    val sLoc = cycle.shootingLocationIndex?.let { shootingLocations.getOrNull(it) } ?: "N/A"
                    "($pLoc -> $sLoc)"
                }

                val dto = MatchDataDTO(
                    dataType = "MATCH",
                    scouterName = state.name,
                    teamNumber = state.robotNumber,
                    matchNumber = state.matchNumber,
                    climbed = if (state.climbEndIndex == 0) "Yes" else "No",
                    climbHeight = state.climbHeightIndex?.let { climbHeights.getOrNull(it) } ?: "N/A",
                    climbTime = state.climbTimeIndex?.let { climbTimes.getOrNull(it) } ?: "N/A",
                    shootOnMove = state.shootOnMoveIndex?.let { ratingScale.getOrNull(it) } ?: "0",
                    intakeOnMove = state.intakeOnMoveIndex?.let { ratingScale.getOrNull(it) } ?: "0",
                    intakeSpeed = state.intakeSpeedIndex?.let { ratingScale.getOrNull(it) } ?: "0",
                    defenseAbility = state.defenseAbilityIndex?.let { ratingScale.getOrNull(it) } ?: "0",
                    driverSkill = state.driverSkillIndex?.let { ratingScale.getOrNull(it) } ?: "0",
                    comments = "${state.otherComments} [Auton: $autonCyclesFormatted] [Teleop: $cyclesFormatted]"
                )

                val response = MatchScoutingRetrofitClient.scoutingApi.submitMatchData(dto)

                if (response.isSuccessful) {
                    _events.send(MatchScoutingEvent.SubmitSuccess)
                    resetForm()
                } else {
                    _events.send(MatchScoutingEvent.ShowError("Server Error: ${response.code()}"))
                }
            } catch (e: Exception) {
                _events.send(MatchScoutingEvent.ShowError("Network Failed: ${e.localizedMessage}"))
            }
        }
    }
    fun resetForm() {
        update { MatchScoutingState() }
    }

    fun prepNewMatch(matchNumber: String, robotNumber: String) {
        update { state ->
            MatchScoutingState(
                name = state.name, // Keep the scouter's name
                matchNumber = matchNumber,
                robotNumber = robotNumber,
                hasRobotShowedUp = true
            )
        }
    }
}
