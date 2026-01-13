package org.waltonrobotics.ScoutingApp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import org.waltonrobotics.ScoutingApp.ui.theme.ScoutingAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(
                scrim = android.graphics.Color.BLACK,
            )
        )

        setContent {
            ScoutingAppTheme(darkTheme = true) {
                App()
            }

        }
    }
}