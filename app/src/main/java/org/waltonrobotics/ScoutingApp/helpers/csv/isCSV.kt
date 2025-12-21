package org.waltonrobotics.ScoutingApp.helpers.csv

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns

fun isCsv(context: Context, uri: Uri): Boolean {
    val name = context.contentResolver
        .query(uri, null, null, null, null)
        ?.use { cursor ->
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            cursor.moveToFirst()
            cursor.getString(nameIndex)
        }

    return name?.endsWith(".csv", ignoreCase = true) == true
}
