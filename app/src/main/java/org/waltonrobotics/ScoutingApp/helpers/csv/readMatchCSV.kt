package org.waltonrobotics.ScoutingApp.helpers.csv

import android.content.Context
import android.net.Uri
import org.waltonrobotics.ScoutingApp.schedule.Match

fun readMatchCsv(context: Context, uri: Uri): List<Match> {
    return context.contentResolver.openInputStream(uri)?.bufferedReader().use { reader ->
        reader?.lineSequence()?.drop(1)?.mapNotNull { line ->
            val parts = line.split(",")
            if (parts.size >= 7) {
                Match(
                    matchNumber = parts[0],
                    redAlliance = listOf(parts[1], parts[2], parts[3]),
                    blueAlliance = listOf(parts[4], parts[5], parts[6])
                )
            } else null
        }?.toList() ?: emptyList()
    }
}
