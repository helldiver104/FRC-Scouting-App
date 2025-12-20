package org.waltonrobotics.ScoutingApp.helpers.csv

import org.waltonrobotics.ScoutingApp.schedule.Match

sealed class CsvResult {
    data class Success(val matches: List<Match>) : CsvResult()
    data class Error(val message: String) : CsvResult()
}
