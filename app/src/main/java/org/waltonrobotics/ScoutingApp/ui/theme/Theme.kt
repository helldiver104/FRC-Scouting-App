import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import org.waltonrobotics.ScoutingApp.ui.theme.Blue100
import org.waltonrobotics.ScoutingApp.ui.theme.DarkBackground
import org.waltonrobotics.ScoutingApp.ui.theme.DarkBlue100
import org.waltonrobotics.ScoutingApp.ui.theme.DarkOnBackground
import org.waltonrobotics.ScoutingApp.ui.theme.DarkOnPrimary
import org.waltonrobotics.ScoutingApp.ui.theme.DarkOnSecondary
import org.waltonrobotics.ScoutingApp.ui.theme.DarkOnSurface
import org.waltonrobotics.ScoutingApp.ui.theme.DarkSurface
import org.waltonrobotics.ScoutingApp.ui.theme.DarkTeal100
import org.waltonrobotics.ScoutingApp.ui.theme.LightBackground
import org.waltonrobotics.ScoutingApp.ui.theme.LightOnBackground
import org.waltonrobotics.ScoutingApp.ui.theme.LightOnPrimary
import org.waltonrobotics.ScoutingApp.ui.theme.LightOnSecondary
import org.waltonrobotics.ScoutingApp.ui.theme.LightOnSurface
import org.waltonrobotics.ScoutingApp.ui.theme.LightSurface
import org.waltonrobotics.ScoutingApp.ui.theme.Teal100

val LightColorScheme = lightColorScheme(
    primary = Blue100,
    onPrimary = LightOnPrimary,
    secondary = Teal100,
    onSecondary = LightOnSecondary,
    background = LightBackground,
    onBackground = LightOnBackground,
    surface = LightSurface,
    onSurface = LightOnSurface
)

val DarkColorScheme = darkColorScheme(
    primary = DarkBlue100,
    onPrimary = DarkOnPrimary,
    secondary = DarkTeal100,
    onSecondary = DarkOnSecondary,
    background = DarkBackground,
    onBackground = DarkOnBackground,
    surface = DarkSurface,
    onSurface = DarkOnSurface
)


@Composable
fun ScoutingAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
            content = content
    )
}