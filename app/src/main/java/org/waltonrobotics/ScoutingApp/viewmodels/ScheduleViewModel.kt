package org.waltonrobotics.ScoutingApp.viewmodels

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
import org.waltonrobotics.ScoutingApp.schedule.Match
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
            .drop(1)
            .filter { it.isNotBlank() && it.contains(",") }
            .mapNotNull { line ->
                val parts = line.split(",").map { it.trim() }
                if (parts.size < 7) return@mapNotNull null
                Match(
                    matchNumber = parts[0],
                    redAlliance = listOf(parts[1], parts[2], parts[3]),
                    blueAlliance = listOf(parts[4], parts[5], parts[6])
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