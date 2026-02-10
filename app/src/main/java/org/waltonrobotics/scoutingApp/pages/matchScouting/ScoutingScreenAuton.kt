package org.waltonrobotics.scoutingApp.pages.matchScouting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.waltonrobotics.scoutingApp.R
import org.waltonrobotics.scoutingApp.helpers.DropdownSelector
import org.waltonrobotics.scoutingApp.helpers.SegmentedSelector
import org.waltonrobotics.scoutingApp.viewmodel.MatchScoutingViewModel

@Composable
fun ScoutingScreenAuton(
    vm: MatchScoutingViewModel
) {
    val uiState by vm.uiState.collectAsState()
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { uiState.autonCycles.size })

    // Initialize first auton cycle
    LaunchedEffect(Unit) {
        if (uiState.autonCycles.isEmpty()) vm.addAutonCycle()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        if (!uiState.isAutonScoringFinished) {
            // --- HEADER ---
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Auton Cycles", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)

                Button(
                    onClick = { vm.setAutonScoringFinished(true) },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("End Auton")
                    Icon(painterResource(R.drawable.rightarrow), null, modifier = Modifier.padding(start = 4.dp).size(18.dp))
                }
            }

            // --- CYCLE PAGER ---
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(horizontal = 16.dp),
                pageSpacing = 12.dp
            ) { pageIndex ->
                val cycle = uiState.autonCycles[pageIndex]

                CycleCard(
                    cycle = cycle,
                    customPickupOptions = listOf("Preloaded", "Neutral Zone", "Alliance Zone", "Depot", "Outpost"),
                    onUpdate = { vm.updateAutonCycle(it) },
                    onAddNewCycle = {
                        vm.addAutonCycle()
                        scope.launch { pagerState.animateScrollToPage(uiState.autonCycles.size) }
                    }
                )
            }
        } else {
            // --- SLIDE 2: AUTON SUMMARY ---
            Column(
                modifier = Modifier.padding(24.dp).verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Text("Auton Summary", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)

                SegmentedSelector(
                    label = "Climb?",
                    options = listOf("Yes", "No"),
                    selectedIndex = uiState.autonClimbIndex,
                    onSelect = vm::setAutonClimb
                )

                DropdownSelector(
                    label = "Time to climb",
                    options = listOf("5", "10", "15", "N/A"),
                    selectedIndex = uiState.autonClimbTimeIndex,
                    onSelect = vm::setAutonClimbTime
                )

                DropdownSelector(
                    label = "Win Auto?",
                    options = listOf("Yes", "No"),
                    selectedIndex = uiState.autonWindex,
                    onSelect = vm::setWinAuto
                )

                Spacer(modifier = Modifier.height(32.dp))

                OutlinedButton(
                    onClick = { vm.setAutonScoringFinished(false) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Back to Cycles")
                }
            }
        }
    }
}