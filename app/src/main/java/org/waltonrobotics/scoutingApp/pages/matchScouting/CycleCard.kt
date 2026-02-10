package org.waltonrobotics.scoutingApp.pages.matchScouting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.waltonrobotics.scoutingApp.R
import org.waltonrobotics.scoutingApp.helpers.DropdownSelector
import org.waltonrobotics.scoutingApp.viewmodel.CycleEntry

@Composable
fun CycleCard(
    cycle: CycleEntry,
    customPickupOptions: List<String>? = null, // Add this parameter
    onUpdate: (CycleEntry) -> Unit,
    onAddNewCycle: () -> Unit
) {
    val defaultPickups = listOf("Neutral Zone", "Alliance Zone")
    val pickupOptions = customPickupOptions ?: defaultPickups

    Card(
        modifier = Modifier.fillMaxSize(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                "Cycle Record",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold
            )

            DropdownSelector(
                label = "Pick up location",
                options = pickupOptions,
                selectedIndex = cycle.pickupLocationIndex,
                onSelect = { onUpdate(cycle.copy(pickupLocationIndex = it)) }
            )

            DropdownSelector(
                label = "Pick up amount",
                options = listOf("10", "20", "40"),
                selectedIndex = cycle.pickupAmountIndex,
                onSelect = { onUpdate(cycle.copy(pickupAmountIndex = it)) }
            )

            DropdownSelector(
                label = "Shooting location",
                options = listOf("Depot", "Tower", "Outpost", "Moving", "Neutral Zone"),
                selectedIndex = cycle.shootingLocationIndex,
                onSelect = { onUpdate(cycle.copy(shootingLocationIndex = it)) }
            )

            DropdownSelector(
                label = "Scoring rate",
                options = listOf("1", "2", "4"),
                selectedIndex = cycle.scoringRateIndex,
                onSelect = { onUpdate(cycle.copy(scoringRateIndex = it)) }
            )

            DropdownSelector(
                label = "Time of cycle",
                options = listOf("5", "10", "15"),
                selectedIndex = cycle.cycleTimeIndex,
                onSelect = { onUpdate(cycle.copy(cycleTimeIndex = it)) }
            )

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = onAddNewCycle,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(painterResource(R.drawable.add), contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Another Cycle", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            // Helpful visual indicator
            Text(
                "Swipe left to view previous cycles",
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}