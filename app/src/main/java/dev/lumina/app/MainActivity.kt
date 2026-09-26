package dev.lumina.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dev.lumina.core.designsystem.theme.LuminaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // Install the splash screen before super.onCreate()
        installSplashScreen()
        
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            LuminaTheme {
                NavigationGraph()
            }
        }
    }
}
