package org.waltonrobotics.ScoutingApp.states

import kotlinx.serialization.Serializable


@Serializable
data class MatchScoutingState(
    val name: String = "",
    val robotNumber: String = "",
    val matchNumber: String = "",
    val robotShowedUpIndex: Int? = null,
    val startPositionIndex: Int? = null,

    // Auton
    val autonLeftStartIndex: Int? = null,
    val autonProcessor: Int = 0,
    val autonNet: Int = 0,
    val autonL1: Int = 0,
    val autonL2: Int = 0,
    val autonL3: Int = 0,
    val autonL4: Int = 0,

    // Teleop
    val teleopProcessor: Int = 0,
    val teleopNet: Int = 0,
    val teleopL1: Int = 0,
    val teleopL2: Int = 0,
    val teleopL3: Int = 0,
    val teleopL4: Int = 0,
    val climbBargeIndex: Int? = null,
    val climbCageIndex: Int? = null,
    val parkedBargeIndex: Int? = null,

    // Closing
    val playstyleIndex: Int? = null,
    val penalties: Int = 0,
    val movementSkillIndex: Int? = null,
    val stuckPiecesIndex: Int? = null,
    val otherComments: String = ""
) {
    fun isValid(): Boolean {
        return name.isNotBlank() &&
                robotNumber.toIntOrNull() != null &&
                matchNumber.toIntOrNull() != null
    }
}
