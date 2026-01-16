package org.waltonrobotics.ScoutingApp.helpers.csv

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import org.waltonrobotics.ScoutingApp.R
import org.waltonrobotics.ScoutingApp.viewmodels.ScheduleViewModel
import org.waltonrobotics.ScoutingApp.viewmodels.ScouterViewModel

@Composable
fun CsvPickerButton(
    scheduleViewModel: ScheduleViewModel,
    onFilePicked: (Uri) -> Unit,
) {
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        uri?.let {
            if (isCsv(context, it)) {
                onFilePicked(it)
            }
        }
    }

    val mainButtonShape = RoundedCornerShape(
        topStart = 12.dp, bottomStart = 12.dp, topEnd = 0.dp, bottomEnd = 0.dp
    )

    val dropdownButtonShape = RoundedCornerShape(
        topStart = 0.dp, bottomStart = 0.dp, topEnd = 12.dp, bottomEnd = 12.dp
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(0.dp) // Ensures 0 space between items
    ) {
        Button(
            onClick = {
                launcher.launch(
                    arrayOf("text/comma-separated-values", "text/csv", "text/plain")
                )
            },
            modifier = Modifier.height(48.dp),
            shape = mainButtonShape,
        ) {

            Text("Import Match Schedule")
        }

        VerticalDivider(
            modifier = Modifier
                .height(48.dp)
                .width(1.dp)
                .zIndex(1f),
            color = MaterialTheme.colorScheme.outlineVariant
        )

        Button(
            onClick = { scheduleViewModel.clearMatches() },
            shape = dropdownButtonShape,
            modifier = Modifier.height(48.dp),
            contentPadding = PaddingValues(horizontal = 16.dp) // Adjust padding for icon balance
        ) {
            Icon(
                painter = painterResource(R.drawable.remove),
                contentDescription = "remove"
            )
        }
    }
}

@Composable
fun CsvPickerButton(
    scoutingViewModel: ScouterViewModel,
    onFilePicked: (Uri) -> Unit,
) {
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        uri?.let {
            if (isCsv(context, it)) {
                onFilePicked(it)
            }
        }
    }

    val mainButtonShape = RoundedCornerShape(
        topStart = 12.dp, bottomStart = 12.dp, topEnd = 0.dp, bottomEnd = 0.dp
    )

    val dropdownButtonShape = RoundedCornerShape(
        topStart = 0.dp, bottomStart = 0.dp, topEnd = 12.dp, bottomEnd = 12.dp
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(0.dp) // Ensures 0 space between items
    ) {
        Button(
            onClick = {
                launcher.launch(
                    arrayOf("text/comma-separated-values", "text/csv", "text/plain")
                )
            },
            modifier = Modifier.height(48.dp),
            shape = mainButtonShape,
        ) {

            Text("Import Match Schedule")
        }

        VerticalDivider(
            modifier = Modifier
                .height(48.dp)
                .width(1.dp)
                .zIndex(1f),
            color = MaterialTheme.colorScheme.outlineVariant
        )

        Button(
            onClick = { },
            shape = dropdownButtonShape,
            modifier = Modifier.height(48.dp),
            contentPadding = PaddingValues(horizontal = 16.dp) // Adjust padding for icon balance
        ) {
            Icon(
                painter = painterResource(R.drawable.remove),
                contentDescription = "remove"
            )
        }
    }
}