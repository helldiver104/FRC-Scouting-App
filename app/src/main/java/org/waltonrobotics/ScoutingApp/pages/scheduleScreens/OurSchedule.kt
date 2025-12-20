package org.waltonrobotics.ScoutingApp.pages.scheduleScreens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.waltonrobotics.ScoutingApp.schedule.Match
import org.waltonrobotics.ScoutingApp.schedule.MatchListItem

fun List<Match>.filterByTeam(teamNumber: String): List<Match> {
    return filter {
        teamNumber in it.redAlliance || teamNumber in it.blueAlliance
    }
}

@Composable
fun OurSchedule(
    matches : List<Match>
){
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        val filteredMatches = remember(matches, "2974") {
            matches.filterByTeam("2974")
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(filteredMatches) { match ->
                MatchListItem(match)
            }
        }

    }
}
