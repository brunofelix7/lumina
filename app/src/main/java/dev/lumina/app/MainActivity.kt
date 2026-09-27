package dev.lumina.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dev.lumina.core.designsystem.theme.LuminaTheme

private const val SYSTEM_SPLASH_FADE_OUT_MILLIS = 400L

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // Install the splash screen before super.onCreate()
        // The system splash only supports a solid color, so it fades out over the gradient SplashScreen.
        installSplashScreen().setOnExitAnimationListener { splashScreenView ->
            splashScreenView.view.animate()
                .alpha(0f)
                .setDuration(SYSTEM_SPLASH_FADE_OUT_MILLIS)
                .withEndAction { splashScreenView.remove() }
                .start()
        }
        
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            LuminaTheme {
                NavigationGraph()
            }
        }
    }
}
