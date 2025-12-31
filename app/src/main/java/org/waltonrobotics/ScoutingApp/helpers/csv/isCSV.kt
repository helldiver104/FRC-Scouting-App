package org.waltonrobotics.ScoutingApp.helpers.csv

import android.content.Context
import android.net.Uri

fun isCsv(context: Context, uri: Uri): Boolean {
    val type = context.contentResolver.getType(uri)
    return type?.contains("csv") == true || type?.contains("text") == true || uri.path?.endsWith(".csv") == true
}