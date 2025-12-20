package org.waltonrobotics.ScoutingApp.schedule

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MatchListItem(match: Match) {

    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp)
    ) {
        Column (
            modifier = Modifier
                .background(MaterialTheme.colorScheme.onPrimary)
                .padding(8.dp),
        ){
            Text(
                text = "Match ${match.matchNumber}",
                color = MaterialTheme.colorScheme.surface
            )

            Text(
                text = "Red: ${match.redAlliance.joinToString(", ")}",
                color = MaterialTheme.colorScheme.errorContainer
            )

            Text(
                text = "Blue: ${match.blueAlliance.joinToString(", ")}",
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
