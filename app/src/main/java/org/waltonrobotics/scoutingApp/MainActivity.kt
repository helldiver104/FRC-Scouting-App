package org.waltonrobotics.scoutingApp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import org.waltonrobotics.scoutingApp.ui.theme.ScoutingAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ScoutingAppTheme(darkTheme = true) {
                App()
            }

        }
    }
}