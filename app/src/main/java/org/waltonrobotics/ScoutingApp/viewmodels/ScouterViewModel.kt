package org.waltonrobotics.ScoutingApp.viewmodels

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.waltonrobotics.ScoutingApp.schedule.ScouterAssignment
import java.io.File
class ScouterViewModel(application: Application) : AndroidViewModel(application) {
    private val _scouters = MutableStateFlow<List<ScouterAssignment>>(emptyList())
    val scouters = _scouters.asStateFlow()

    val allScouterNames = _scouters.map { list ->
        list.map { it.scouterName }.sorted()
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private val file = File(application.filesDir, "scouter_schedule.csv")

    init {
        loadFromInternal()
    }

    private fun loadFromInternal() {
        if (file.exists()) {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val text = file.readText()
                    _scouters.value = parseCsv(text)
                } catch (e: Exception) { e.printStackTrace() }
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
            } catch (e: Exception) { e.printStackTrace() }
        }
    }

    fun getAssignmentForMatch(name: String, match: Int): String? {
        val list = _scouters.value
        val scouter = list.find { it.scouterName.equals(name, ignoreCase = true) } ?: return null

        val index = when (match) {
            in 1..5 -> 0
            in 6..10 -> 1
            in 11..15 -> 2
            in 16..20 -> 3
            in 21..25 -> 4
            in 26..30 -> 5
            in 31..35 -> 6
            in 36..40 -> 7
            in 41..45 -> 8
            in 46..50 -> 9
            in 51..55 -> 10
            in 56..60 -> 11
            else -> 12
        }
        return scouter.assignments.getOrNull(index)
    }

    private fun parseCsv(content: String): List<ScouterAssignment> {
        return content.lineSequence()
            .drop(1)
            .filter { it.contains(",") }
            .mapNotNull { line ->
                // FIX: Use 0 for limit or omit it to avoid the negative limit error
                val parts = line.split(",").map { it.trim() }
                if (parts.isEmpty() || parts[0].isBlank()) return@mapNotNull null

                ScouterAssignment(
                    scouterName = parts[0],
                    assignments = parts.drop(1)
                )
            }.toList()
    }
}