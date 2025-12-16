package org.waltonrobotics.ScoutingApp.viewmodels

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
    val matchNumber: String = "",
    val cycleTime: String = "",
    val otherComments: String = ""
) : Parcelable {
    fun isValid(): Boolean {
        return name.isNotBlank() && robotNumber.toIntOrNull() != null && matchNumber.toIntOrNull() != null && cycleTime.toDoubleOrNull() != null
    }
}

sealed class CycleTimeEvent {
    object SubmitSuccess : CycleTimeEvent()
    data class ShowError(val message: String) : CycleTimeEvent()
}

class CycleTimeViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {
        private const val STATE_KEY = "cycle_time_state"
    }

    private val _uiState: MutableStateFlow<CycleTimeState> = MutableStateFlow(
        savedStateHandle.get<CycleTimeState>(STATE_KEY) ?: CycleTimeState()
    )
    val uiState: StateFlow<CycleTimeState> = _uiState.asStateFlow()

    private val _events = Channel<CycleTimeEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    init {
        viewModelScope.launch {
            _uiState.collect { state ->
                savedStateHandle[STATE_KEY] = state
            }
        }
    }

    // ---- Field updaters ----

    fun updateName(value: String) = update { it.copy(name = value) }

    fun updateRobotNumber(value: String) =
        update { it.copy(robotNumber = value.filter(Char::isDigit)) }

    fun updateMatchNumber(value: String) =
        update { it.copy(matchNumber = value.filter(Char::isDigit)) }

    fun updateCycleTime(value: String) = update {
        it.copy(
            cycleTime = value.filter { ch ->
                ch.isDigit() || ch == '.'
            })
    }

    fun updateOtherComments(value: String) = update { it.copy(otherComments = value) }

    // ---- Submit ----

    fun submit() {
        viewModelScope.launch {
            val state = _uiState.value

            if (!state.isValid()) {
                _events.send(
                    CycleTimeEvent.ShowError(
                        "Name, robot #, match #, and valid cycle time are required"
                    )
                )
                return@launch
            }

            try {
                // TODO: repository.saveCycleTime(state)

                _events.send(CycleTimeEvent.SubmitSuccess)

                resetForm()
            } catch (t: Throwable) {
                _events.send(
                    CycleTimeEvent.ShowError(
                        "Failed to save: ${t.localizedMessage ?: t::class.simpleName}"
                    )
                )
            }
        }
    }

    fun resetForm() {
        _uiState.value = CycleTimeState()
    }

    private fun update(reducer: (CycleTimeState) -> CycleTimeState) {
        _uiState.update { current -> reducer(current) }
    }
}
