package org.waltonrobotics.scoutingApp.sheetsStuff

import kotlinx.serialization.Serializable

@Serializable
data class MatchDataDTO(
    val dataType: String,
    val scouterName: String,
    val teamNumber: String,
    val matchNumber: String,
    val climbed: String,
    val climbHeight: String,
    val climbTime: String,
    val shootOnMove: String,
    val intakeOnMove: String,
    val intakeSpeed: String,
    val defenseAbility: String,
    val driverSkill: String,
    val comments: String
)