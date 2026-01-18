package org.waltonrobotics.scoutingApp.viewmodels

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
data class CycleTimeState(
    val name: String = "",
    val robotNumber: String = "",
    val matchNumber: String = "1", // Default to Match 1
    val cycleTime: String = "",
    val otherComments: String = ""
) : Parcelable {
    fun isValid(): Boolean {
        // Basic validation: ensure names aren't empty and numbers exist
        return name.isNotBlank() &&
                robotNumber.isNotBlank() &&
                cycleTime.isNotBlank()
    }
}

sealed class CycleTimeEvent {
    object SubmitSuccess : CycleTimeEvent()
    data class ShowError(val message: String) : CycleTimeEvent()
}
class CycleTimeViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val STATE_KEY = "cycle_time_state"

    private val _uiState: MutableStateFlow<CycleTimeState> = MutableStateFlow(
        savedStateHandle.get<CycleTimeState>(STATE_KEY) ?: CycleTimeState()
    )
    val uiState: StateFlow<CycleTimeState> = _uiState.asStateFlow()

    private val _events = Channel<CycleTimeEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    init {
        viewModelScope.launch {
            uiState.collect { state ->
                savedStateHandle[STATE_KEY] = state
            }
        }
    }

    fun updateName(value: String) = update { it.copy(name = value) }

    fun updateRobotNumber(value: String) = update {
        it.copy(robotNumber = value.filter { it.isDigit() })
    }

    fun updateMatchNumber(v: String) = update { it.copy(matchNumber = v) }
    fun updateCycleTime(value: String) = update {
        val filtered = value.filterIndexed { index, char ->
            char.isDigit() || (char == '.' && value.indexOf('.') == index)
        }
        it.copy(cycleTime = filtered)
    }

    fun updateOtherComments(value: String) = update { it.copy(otherComments = value) }

    fun submit() {
        viewModelScope.launch {
            val state = _uiState.value

            if (!state.isValid()) {
                _events.send(CycleTimeEvent.ShowError("Please fill in all required fields"))
                return@launch
            }

            try {
//                val timeAsDouble = state.cycleTime.toDoubleOrNull() ?: 0.0

                // TODO: Save to database or CSV

                _events.send(CycleTimeEvent.SubmitSuccess)
                resetForm()
            } catch (e: Exception) {
                _events.send(CycleTimeEvent.ShowError("Submit failed: ${e.message}"))
            }
        }
    }

    fun resetForm() {
        // When resetting, we usually keep the name/match but increment the match
        _uiState.update {
            it.copy(
                matchNumber = it.matchNumber,
                cycleTime = "",
                otherComments = ""
            )
        }
    }

    private fun update(reducer: (CycleTimeState) -> CycleTimeState) {
        _uiState.update { reducer(it) }
    }
} //