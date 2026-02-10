package org.waltonrobotics.scoutingApp.pages.scheduleScreens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.waltonrobotics.scoutingApp.schedule.Match
import org.waltonrobotics.scoutingApp.schedule.MatchListItem

@Composable
fun CompSchedule(
    matches: List<Match>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(matches) { match ->
            MatchListItem(match = match)
        }
    }
}
