package org.waltonrobotics.ScoutingApp.helpers.csv

import android.content.Context
import android.net.Uri
import org.waltonrobotics.ScoutingApp.schedule.Match

fun readMatchCsv(context: Context, uri: Uri): List<Match> {
    return context.contentResolver.openInputStream(uri)?.use { inputStream ->
        inputStream.bufferedReader().lineSequence()
            .drop(1)
            .filter { it.isNotBlank() }
            .mapNotNull { line ->
                val parts = line.split(",")
                if (parts.size < 7) return@mapNotNull null // Safety check

                Match(
                    matchNumber = parts[0].trim(),
                    redAlliance = parts.slice(1..3).map { it.trim() },
                    blueAlliance = parts.slice(4..6).map { it.trim() }
                )
            }.toList()
    } ?: emptyList()
}