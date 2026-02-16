package org.waltonrobotics.scoutingApp.schedule

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ScouterAssignment(
    val scouterName: String,
    val assignments: List<String>
) : Parcelable