package org.waltonrobotics.ScoutingApp.helpers.csv

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun CsvPickerButton(onFilePicked: (Uri) -> Unit) {
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri ?: return@rememberLauncherForActivityResult

        context.contentResolver.takePersistableUriPermission(
            uri,
            Intent.FLAG_GRANT_READ_URI_PERMISSION
        )
        if (isCsv(context, uri)) {
            onFilePicked(uri)
        }

    }

    Button(
        onClick = {
            launcher.launch(arrayOf("*/*"))
        }
    ) {
        Text("Pick CSV File")
    }
}
