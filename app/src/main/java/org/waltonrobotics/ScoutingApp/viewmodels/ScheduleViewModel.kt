package org.waltonrobotics.ScoutingApp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.waltonrobotics.ScoutingApp.schedule.Match
import org.waltonrobotics.ScoutingApp.schedule.readCsv
import java.io.InputStream


sealed class ScheduleEvent {
    data class ShowError(val message: String) : ScheduleEvent()
}
class ScheduleViewModel : ViewModel() {

    private val _matches = MutableStateFlow<List<Match>>(emptyList())
    val matches: StateFlow<List<Match>> = _matches

    fun loadCsv(inputStream: InputStream) {
        viewModelScope.launch {
            try {
                _matches.value = readCsv(inputStream)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}
