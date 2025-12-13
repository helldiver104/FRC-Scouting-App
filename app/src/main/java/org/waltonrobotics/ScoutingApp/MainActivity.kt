package org.waltonrobotics.ScoutingApp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import org.waltonrobotics.ScoutingApp.ui.theme.ScoutingAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            ScoutingAppTheme(darkTheme = true) {
                App()
            }

        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}