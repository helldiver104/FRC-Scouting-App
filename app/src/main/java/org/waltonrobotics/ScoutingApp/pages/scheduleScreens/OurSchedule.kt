package org.waltonrobotics.ScoutingApp.pages.scheduleScreens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.waltonrobotics.ScoutingApp.schedule.Match
import org.waltonrobotics.ScoutingApp.schedule.MatchListItem

@Composable
fun OurSchedule(){
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        // TODO REPLACE WITH ACTUAL DATA
        val schedule = listOf(
            Match("1", listOf("8577", "6705", "2974"), listOf("1746", "6905", "5608")),
            Match("2", listOf("2974", "971", "1678"), listOf("148", "118", "2056")),
            Match("3", listOf("33", "118", "195"), listOf("1619", "2974", "1323")),
            Match("4", listOf("2056", "148", "2974"), listOf("254", "971", "33")),
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(
                items=schedule
            ) { match ->
                MatchListItem(match)
            }
        }

    }
}
