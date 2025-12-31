package org.waltonrobotics.ScoutingApp.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import org.waltonrobotics.ScoutingApp.appNavigation.AppScreen

@Composable
fun FAQScreen(
    navController: NavController
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(Modifier.height(8.dp))

        Text("FAQ", fontSize = 28.sp, fontWeight = FontWeight.Bold)

        FAQItem("What is the starting line?", "The black one nearest to the robots, NOT the colored ones.")

        FAQItem("I keep getting notifications for wrong matches. Why?", "Likely updating schedule mid-competition. Refresh the app or wait.")

        FAQItem("How do we know which robot is Red 1?", "Red 1 is the first number on the big screen, NOT the field order.")

        FAQItem("Far side / middle / processor side?", "Far side = further, middle = middle, processor side = near amp.")

        Spacer(Modifier.height(8.dp))

        Button(
            onClick = {
                navController.navigate(AppScreen.MainScreen.route) {
                    popUpTo(AppScreen.MainScreen.route) { inclusive = true }
                    launchSingleTop = true
                }
            },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Go back", fontWeight = FontWeight.Bold)
        }

        Spacer(Modifier.height(24.dp))
    }
}

@Composable
fun FAQItem(question: String, answer: String) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = "Q: $question",
            fontWeight = FontWeight.Bold,
            color = Color.White,
            fontSize = 16.sp
        )
        Text(
            text = "A: $answer",
            fontSize = 15.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        HorizontalDivider(modifier = Modifier.padding(top = 8.dp), thickness = 0.5.dp)
    }
}