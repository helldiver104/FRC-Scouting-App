package org.waltonrobotics.scoutingApp.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.waltonrobotics.scoutingApp.TBAstuff.RetrofitClient
import org.waltonrobotics.scoutingApp.schedule.Match

class ScheduleViewModel(application: Application) : AndroidViewModel(application) {

    private val _matches = MutableStateFlow<List<Match>>(emptyList())
    val matches: StateFlow<List<Match>> = _matches.asStateFlow()

    private val _events = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val events: SharedFlow<String> = _events


    fun syncWithTba(eventKey: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitClient.tbaApi.getMatches(eventKey.trim().lowercase())

                val mappedMatches = response
                    .filter { it.compLevel.equals("qm", ignoreCase = true) }
                    .map { tba ->
                        // 1. Process the red alliance list
                        val redAllianceList = tba.alliances.red.teamKeys?.map { teamKey ->
                            teamKey.removePrefix("frc")
                        } ?: emptyList()

                        // 2. Process the blue alliance list
                        val blueAllianceList = tba.alliances.blue.teamKeys?.map { teamKey ->
                            teamKey.removePrefix("frc")
                        } ?: emptyList()

                        // 3. Create your Match object
                        Match(
                            matchNumber = tba.matchNumber.toString(),
                            redAlliance = redAllianceList,  // This is now a List<String>
                            blueAlliance = blueAllianceList // This is now a List<String>
                        )
                    }
                    .sortedBy { it.matchNumber.toIntOrNull() ?: 0 }

                withContext(Dispatchers.Main) {
                    _matches.value = mappedMatches
                    _events.emit("Synced ${mappedMatches.size} matches")
                }
            } catch (e: Exception) {
                _events.emit("Sync Error: ${e.localizedMessage}")
            }
        }
    }
}