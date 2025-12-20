package org.waltonrobotics.ScoutingApp.pages.scheduleScreens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import org.waltonrobotics.ScoutingApp.schedule.Match
import org.waltonrobotics.ScoutingApp.schedule.MatchListItem

@Composable
fun CompSchedule(
    matches: List<Match>
) {

    LazyColumn {
        items(matches) { match ->
            MatchListItem(match)
        }
    }
}
