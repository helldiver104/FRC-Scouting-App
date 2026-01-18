package org.waltonrobotics.scoutingApp.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.waltonrobotics.scoutingApp.helpers.csv.CsvPickerButton
import org.waltonrobotics.scoutingApp.viewmodels.AuthViewModel
import org.waltonrobotics.scoutingApp.viewmodels.ScheduleViewModel
import org.waltonrobotics.scoutingApp.viewmodels.ScouterViewModel

@Composable
fun AccountScreen(
    email: String,
    name: String,
    viewmodel: AuthViewModel,
    scheduleViewModel: ScheduleViewModel,
    scouterViewModel: ScouterViewModel,
    isSudo: Boolean
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Profile",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
        HorizontalDivider(
            modifier = Modifier.padding(vertical = 16.dp), thickness = 1.dp, color = Color.White
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(20.dp))
            Text(
                text = name,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = email, style = MaterialTheme.typography.bodyMedium, color = Color.Gray
            )

            Spacer(Modifier.height(40.dp))

            SectionLabel("Account Information")
            AccountInfoCard(name, email)
            Spacer(Modifier.height(16.dp))
            SectionLabel("Edit App CSVs")
            
            if (isSudo) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CsvPickerButton(scheduleViewModel) { uri ->
                            scheduleViewModel.importCsv(context, uri)
                        }
                    }
//                    Spacer(modifier = Modifier.height(16.dp))
//                    Row (
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(horizontal = 16.dp)
//                    ){
//                        CsvPickerButton(scouterViewModel) { uri ->
//                            scouterViewModel.importCsv(context, uri)
//                        }
//                    }
                }

            }
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = { viewmodel.signOut() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 32.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.error
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("LOG OUT", fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text,
        modifier = Modifier.fillMaxWidth(),
        style = MaterialTheme.typography.labelLarge,
        color = Color.White,
        fontWeight = FontWeight.SemiBold
    )
    Spacer(Modifier.height(16.dp))
}

@Composable
private fun AccountInfoCard(name: String, email: String) {
    Card(
        modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        ), shape = RoundedCornerShape(16.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            InfoRow("Display Name", name)
            HorizontalDivider(Modifier.padding(vertical = 12.dp), thickness = 0.5.dp)
            InfoRow("Email Address", email)
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Column {
        Text(label, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
        Text(value, style = MaterialTheme.typography.bodyLarge)
    }
}

