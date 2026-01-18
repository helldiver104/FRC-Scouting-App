package org.waltonrobotics.scoutingApp.viewmodel


import android.net.Uri
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
import kotlinx.parcelize.RawValue

@Parcelize
data class PitScoutingState(
    val name: String = "",
    val robotNumber: String = "",
    val driverExperience: Int = 1, // Store the actual 1-5 value
    val autonsAvailable: String = "",
    val scoringFocus: String = "",
    val piecesPerMatch: String = "0",
    val playStyleIndex: Int? = null,
    val otherStrategies: String = "",
    val robotWeight: String = "",
    // Variable to store the photo location
    val robotPhotoUri: @RawValue Uri? = null
) : Parcelable {
    fun isValid(): Boolean {
        return name.isNotBlank() &&
                robotNumber.toIntOrNull() != null &&
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

    // Standard updates
    fun updateName(value: String) = update { it.copy(name = value) }
    fun updateRobotNumber(value: String) = update { it.copy(robotNumber = value.filter(Char::isDigit)) }
    fun setDriverExperience(value: Int) = update { it.copy(driverExperience = value) }

    // Photo Update Function
    fun updatePhoto(uri: Uri?) = update { it.copy(robotPhotoUri = uri) }

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
                _events.send(PitScoutingEvent.ShowError("Required fields missing"))
                return@launch
            }
            try {
                // Logic to save 'state' (including the Photo URI) goes here
                _events.send(PitScoutingEvent.SubmitSuccess("Form saved!"))
                resetForm()
            } catch (t: Throwable) {
                _events.send(PitScoutingEvent.ShowError("Save failed"))
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