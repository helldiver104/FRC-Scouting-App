package org.waltonrobotics.ScoutingApp.schedule

data class ScouterAssignment(
    val scouterName: String,
    val matchRange: IntRange,
    val position: String // e.g., "Red 1", "Blue 2"
)