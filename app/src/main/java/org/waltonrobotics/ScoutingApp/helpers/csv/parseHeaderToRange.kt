package org.waltonrobotics.ScoutingApp.helpers.csv

fun parseHeaderToRange(header: String): IntRange? {
    val regex = Regex("""Match (\d+)-(\d+)""")
    val result = regex.find(header)

    return if (result != null) {
        val (start, end) = result.destructured
        IntRange(start.toInt(), end.toInt())
    } else if (header.contains("Playoffs")) {
        IntRange(100, 200) // Assign a high range for playoff matches
    } else null
}