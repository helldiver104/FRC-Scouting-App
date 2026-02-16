package org.waltonrobotics.scoutingApp.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.waltonrobotics.scoutingApp.schedule.ScouterAssignment
import org.waltonrobotics.scoutingApp.sheetsStuff.MatchScoutingRetrofitClient

class ScouterViewModel(application: Application) : AndroidViewModel(application) {
    private val _scouters = MutableStateFlow<List<ScouterAssignment>>(emptyList())
    val scouters = _scouters.asStateFlow()

    init {
        fetchAssignments()
    }

    fun fetchAssignments() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val rawData = MatchScoutingRetrofitClient.waltScheduleAPI.getAssignments()
                val processed = rawData.map { row ->
                    val name = row["Names"] ?: "Unknown"
                    val matchData = row.keys
                        .filter { it.contains("match", ignoreCase = true) }
                        .mapNotNull { key -> row[key] } // Extract the robot number for that header
                        .filter { it.isNotBlank() }

                    ScouterAssignment(scouterName = name, assignments = matchData)
                }

                _scouters.value = processed

            } catch (e: Exception) {
                Log.d("BONK", e.toString())
            }
        }
    }

    fun getAssignmentForMatch(name: String, match: Int): String? {
        val scouter = _scouters.value.find { it.scouterName.equals(name, ignoreCase = true) }
            ?: return null

        val index = if (match > 0) (match - 1) / 5 else 0

        return scouter.assignments.getOrNull(index)
    }

    fun getNameByEmail(email: String?): String {
        if (email == null || !email.contains(".")) return "Unknown Scouter"

        val nameParts = email.substringBefore("@").split(".")

        val fullNameQuery = nameParts.joinToString(" ") { part ->
            part.replaceFirstChar { it.uppercase() }
        }

        val scouter = _scouters.value.find { scouter ->
            scouter.scouterName.contains(fullNameQuery, ignoreCase = true) ||
                    // Also check reverse just in case CSV is "Doe, John"
                    scouter.scouterName.contains(nameParts.last(), ignoreCase = true) &&
                    scouter.scouterName.contains(nameParts.first(), ignoreCase = true)
        }

        return scouter?.scouterName ?: "Unknown Scouter"
    }

    fun isSudo(email: String?): Boolean {
        return email?.lowercase() == "gabriella.angryk@waltonrobotics.org" ||
                email?.lowercase() == "max.gurung@waltonrobotics.org"
    }
}