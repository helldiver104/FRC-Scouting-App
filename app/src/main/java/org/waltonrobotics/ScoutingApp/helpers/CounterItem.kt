package org.waltonrobotics.ScoutingApp.helpers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import org.waltonrobotics.ScoutingApp.R

@Composable
fun CounterItem(label: String, state: MutableState<Int>) {
    Text(label)
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { if (state.value > 0) state.value-- }) {
            Icon(painterResource(R.drawable.remove), contentDescription = "Remove")
        }
        Text(state.value.toString(), fontSize = 18.sp)
        IconButton(onClick = { state.value++ }) {
            Icon(painterResource(R.drawable.add), contentDescription = "Add")
        }
    }
}
