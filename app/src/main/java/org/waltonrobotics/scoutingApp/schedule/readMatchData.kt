package org.waltonrobotics.scoutingApp.schedule

import java.io.InputStream

fun readCsv(inputStream: InputStream): List<Match> {
    val reader = inputStream.bufferedReader()
    reader.readLine()

    return reader.lineSequence()
        .filter { it.isNotBlank() }
        .map { line ->
            val parts = line.split(',')

            Match(
                matchNumber = parts[0].trim(),
                redAlliance = parts.slice(1..3).map { it.trim() },
                blueAlliance = parts.slice(4..6).map { it.trim() }
            )
        }
        .toList()
}
