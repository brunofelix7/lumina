package dev.brunofelix.lumina

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import dev.brunofelix.lumina.core.designsystem.theme.LuminaTheme
import dev.brunofelix.lumina.core.presentation.viewmodel.NavigationViewModel

private const val SYSTEM_SPLASH_FADE_OUT_MILLIS = 400L

@AndroidEntryPoint
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
                val navigationViewModel: NavigationViewModel = hiltViewModel()
                val backStack by navigationViewModel.backStack.collectAsStateWithLifecycle()

                NavigationGraph(
                    backStack = backStack,
                    onNavigate = navigationViewModel::navigateTo,
                    onReplace = navigationViewModel::replaceCurrent,
                    onReset = navigationViewModel::resetTo,
                    onBack = navigationViewModel::popBackStack
                )
            }
        }
    }
}
