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

@Parcelize
data class PitScoutingState(
    val name: String = "",
    val robotNumber: String = "",
    val driverExperienceIndex: Int? = null,
    val autonsAvailable: String = "",
    val scoringFocus: String = "",
    val piecesPerMatch: String = "",
    val playStyleIndex: Int? = null,
    val otherStrategies: String = "",
    val robotWeight: String = ""
) : Parcelable {
    fun isValid(): Boolean {
        return name.isNotBlank() &&
                robotNumber.toIntOrNull() != null &&
                driverExperienceIndex != null &&
                playStyleIndex != null
    }
}

sealed class PitScoutingEvent {
    data class SubmitSuccess(val message: String) : PitScoutingEvent()
    data class ShowError(val message: String) : PitScoutingEvent()
}

class PitScoutingViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val STATE_KEY = "pit_scouting_state"

    private val _uiState: MutableStateFlow<PitScoutingState> = MutableStateFlow(
        savedStateHandle.get<PitScoutingState>(STATE_KEY) ?: PitScoutingState()
    )
    val uiState: StateFlow<PitScoutingState> = _uiState.asStateFlow()

    private val _events = Channel<PitScoutingEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    init {
        viewModelScope.launch {
            uiState.collect { state ->
                savedStateHandle[STATE_KEY] = state
            }
        }
    }

    fun updateName(value: String) = update { it.copy(name = value) }
    fun updateRobotNumber(value: String) = update { it.copy(robotNumber = value.filter(Char::isDigit)) }
    fun setDriverExperience(index: Int?) = update { it.copy(driverExperienceIndex = index) }
    fun updateAvailableAutons(value: String) = update { it.copy(autonsAvailable = value) }
    fun updatePiecesPerMatch(value: String) = update { it.copy(piecesPerMatch = value.filter(Char::isDigit)) }
    fun setPlaystyle(index: Int?) = update { it.copy(playStyleIndex = index) }
    fun updateScoringFocus(value: String) = update { it.copy(scoringFocus = value) }
    fun updateOtherStrategies(value: String) = update { it.copy(otherStrategies = value) }
    fun updateWeight(value: String) = update { it.copy(robotWeight = value.filter(Char::isDigit)) }


    fun submit() {
        viewModelScope.launch {
            val state = _uiState.value

            if (!state.isValid()) {
                _events.send(
                    PitScoutingEvent.ShowError(
                        "Name, robot number, driver experience, and playstyle are required"
                    )
                )
                return@launch
            }

            try {
                // TODO: save data (repository, database, etc)

                _events.send(PitScoutingEvent.SubmitSuccess("Success!"))

                resetForm()
            } catch (t: Throwable) {
                _events.send(
                    PitScoutingEvent.ShowError(
                        "Failed to save: ${t.localizedMessage ?: t::class.simpleName}"
                    )
                )
            }
        }
    }

    fun resetForm() {
        _uiState.value = PitScoutingState()
    }

    private fun update(reducer: (PitScoutingState) -> PitScoutingState) {
        _uiState.update { current -> reducer(current) }
    }
}
