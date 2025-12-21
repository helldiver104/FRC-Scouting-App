package org.waltonrobotics.ScoutingApp.viewmodels

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.waltonrobotics.ScoutingApp.helpers.csv.readMatchCsv
import org.waltonrobotics.ScoutingApp.schedule.Match

class ScheduleViewModel : ViewModel() {

    private val _matches = MutableStateFlow<List<Match>>(emptyList())
    val matches: StateFlow<List<Match>> = _matches.asStateFlow()
    private val _events = MutableSharedFlow<String>()
    val events: SharedFlow<String> = _events

    fun loadMatchesFromCsv(context: Context, uri: Uri) {
        viewModelScope.launch {
            try {
                val newMatches = readMatchCsv(context, uri)
                _matches.value = newMatches
                _events.emit("Loaded ${newMatches.size} matches")
            } catch (e: Exception) {
                _events.emit("Failed to load CSV")
            }
        }
    }

    fun clearMatches() {
        _matches.value = emptyList()
    }
}
