package org.waltonrobotics.ScoutingApp.pages.scheduleScreens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import org.waltonrobotics.ScoutingApp.schedule.MatchListItem
import org.waltonrobotics.ScoutingApp.schedule.readCsv

@Composable
fun CompSchedule() {
    val context = LocalContext.current

    val matches = remember {
        context.assets.open("schedule.csv").use {
            readCsv(it)
        }
    }

    LazyColumn {
        items(matches) { match ->
            MatchListItem(match)
        }
    }
}
