package org.waltonrobotics.scoutingApp.viewmodels

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.waltonrobotics.scoutingApp.schedule.Match
import java.io.File

class ScheduleViewModel(application: Application) : AndroidViewModel(application) {

    private val _matches = MutableStateFlow<List<Match>>(emptyList())
    val matches: StateFlow<List<Match>> = _matches.asStateFlow()

    private val _events = MutableSharedFlow<String>()
    val events: SharedFlow<String> = _events

    private val file = File(application.filesDir, "schedule_persistent.csv")

    init {
        viewModelScope.launch(Dispatchers.IO) {
            if (file.exists()) {
                _matches.value = parseCsv(file.readText())
            }
        }
    }

    fun importCsv(context: Context, uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val content = context.contentResolver.openInputStream(uri)?.use {
                    it.bufferedReader().readText()
                } ?: ""

                if (content.isNotBlank()) {
                    file.writeText(content)
                    _matches.value = parseCsv(content)
                    _events.emit("Loaded ${_matches.value.size} matches")
                }
            } catch (e: Exception) {
                _events.emit("Failed to load CSV")
            }
        }
    }

    private fun parseCsv(content: String): List<Match> {
        return content.lineSequence()
            .drop(1) // Skip header
            .map { it.split(",").map { part -> part.trim() } }
            // Ensure row has enough columns AND comp_level (index 3) is "qm"
            .filter { parts ->
                parts.size >= 12 && parts[3].equals("qm", ignoreCase = true)
            }
            .mapNotNull { parts ->
                Match(
                    // Index 4: match_number
                    matchNumber = parts[4],

                    // Red Alliance: red1(6), red2(8), red3(10)
                    redAlliance = listOf(parts[6], parts[8], parts[10]),

                    // Blue Alliance: blue1(7), blue2(9), blue3(11)
                    blueAlliance = listOf(parts[7], parts[9], parts[11])
                )
            }.toList()
    }

    fun clearMatches() {
        viewModelScope.launch(Dispatchers.IO) {
            if (file.exists()) file.delete()
            _matches.value = emptyList()
            _events.emit("Schedule cleared")
        }
    }
}