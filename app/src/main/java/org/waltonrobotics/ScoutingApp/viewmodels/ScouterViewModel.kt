package org.waltonrobotics.ScoutingApp.viewmodels

import android.app.Application
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.waltonrobotics.ScoutingApp.schedule.ScouterAssignment
import java.io.File

class ScouterViewModel(application: Application) : AndroidViewModel(application) {
    private val _scouters = MutableStateFlow<List<ScouterAssignment>>(emptyList())
    val scouters = _scouters.asStateFlow()

    // Using stateIn properly to ensure UI has immediate access to sorted names
//    val allScouterNames = _scouters.map { list ->
//        list.map { it.scouterName }.sorted()
//    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
//
//    private val _userMap = MutableStateFlow<Map<String, String>>(emptyMap())
    private val file = File(application.filesDir, "matchschedule.csv")

    init {
        loadFromInternal()
    }

    private fun loadFromInternal() {
        if (file.exists()) {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val text = file.readText()
                    _scouters.update { parseCsv(text) }
                } catch (e: Exception) {
                    Log.e("ScouterVM", "Error loading internal file", e)
                }
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
                    _scouters.value = parseCsv(content)
                }
            } catch (e: Exception) {
                Log.e("ScouterVM", "Error importing CSV", e)
            }
        }
    }

    fun getAssignmentForMatch(name: String, match: Int): String? {
        val scouter = _scouters.value.find { it.scouterName.equals(name, ignoreCase = true) }
            ?: return null

        // Calculate index: (match - 1) / 5
        // This replaces the long when-block: 1-5 -> 0, 6-10 -> 1, etc.
        val index = if (match > 0) (match - 1) / 5 else 0

        return scouter.assignments.getOrNull(index)
    }

    private fun parseCsv(content: String): List<ScouterAssignment> {
        return content.lineSequence()
            .drop(1)
            .filter { it.isNotBlank() && it.contains(",") }
            .mapNotNull { line ->
                val parts = line.split(",").map { it.trim() }
                if (parts.isEmpty() || parts[0].isEmpty()) return@mapNotNull null

                ScouterAssignment(
                    scouterName = parts[0],
                    assignments = parts.drop(1) // All columns after name
                )
            }.toList()
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