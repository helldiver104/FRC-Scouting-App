package org.waltonrobotics.ScoutingApp.helpers

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import java.io.File

fun createTempImageUri(context: Context): Uri {
    val tempFile = File.createTempFile(
        "robot_${System.currentTimeMillis()}", ".jpg",
        context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    )
    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        tempFile
    )
}