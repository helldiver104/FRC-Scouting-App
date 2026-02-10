package org.waltonrobotics.scoutingApp.pages.matchScouting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.waltonrobotics.scoutingApp.viewmodel.MatchScoutingViewModel

@Composable
fun ScoutingScreenTeleop(vm: MatchScoutingViewModel) {
    val uiState by vm.uiState.collectAsState()
    val scope = rememberCoroutineScope()

    val pagerState = rememberPagerState(pageCount = { uiState.cycles.size })

    LaunchedEffect(Unit) {
        if (uiState.cycles.isEmpty()) vm.addCycle()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Cycle ${pagerState.currentPage + 1} of ${uiState.cycles.size}",
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.secondary
        )

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(horizontal = 16.dp),
            pageSpacing = 12.dp
        ) { pageIndex ->
            uiState.cycles.getOrNull(pageIndex)?.let { cycle ->
                CycleCard(
                    cycle = cycle,
                    onUpdate = { updated -> vm.updateCycle(updated) },
                    onAddNewCycle = {
                        vm.addCycle()
                        scope.launch {
                            pagerState.animateScrollToPage(uiState.cycles.size)
                        }
                    }
                )
            }
        }
    }
}