package org.waltonrobotics.scoutingApp.helpers.csv

import org.waltonrobotics.scoutingApp.schedule.Match

sealed class CsvResult {
    data class Success(val matches: List<Match>) : CsvResult()
    data class Error(val message: String) : CsvResult()
}
