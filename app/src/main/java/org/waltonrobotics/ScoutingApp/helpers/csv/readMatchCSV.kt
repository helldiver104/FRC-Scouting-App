package org.waltonrobotics.ScoutingApp.helpers.csv

import android.content.Context
import android.net.Uri
import org.waltonrobotics.ScoutingApp.schedule.Match

fun readMatchCsv(
    context: Context,
    uri: Uri
): List<Match> {
    context.contentResolver.openInputStream(uri)?.use { inputStream ->
        val reader = inputStream.bufferedReader()

        return reader.lineSequence()
            .drop(1) // skip header
            .filter { it.isNotBlank() }
            .map { line ->
                val parts = line.split(",")

                val matchNumber = parts[0].trim()
                val redAlliance = parts.subList(1, 4).map { it.trim() }
                val blueAlliance = parts.subList(4, 7).map { it.trim() }

                Match(
                    matchNumber = matchNumber,
                    redAlliance = redAlliance,
                    blueAlliance = blueAlliance
                )
            }
            .toList()
    }

    error("Unable to open CSV file")
}
