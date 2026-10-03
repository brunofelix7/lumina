package dev.brunofelix.lumina.core.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import dev.brunofelix.lumina.core.designsystem.theme.BorderGlassFrost
import dev.brunofelix.lumina.core.designsystem.theme.LuminaTheme
import dev.brunofelix.lumina.core.designsystem.theme.OnGlass
import dev.brunofelix.lumina.core.designsystem.theme.SurfaceGlassNightDense
import dev.brunofelix.lumina.core.designsystem.theme.shapeRounded16
import dev.brunofelix.lumina.core.designsystem.theme.size1
import dev.brunofelix.lumina.core.designsystem.theme.spacing14
import dev.brunofelix.lumina.core.designsystem.theme.spacing16

/**
 * Shows the messages of [hostState] as glass snackbars.
 */
@Composable
fun LuminaSnackbarHost(
    hostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    SnackbarHost(hostState = hostState, modifier = modifier) { snackbarData ->
        LuminaSnackbar(message = snackbarData.visuals.message)
    }
}

@Composable
fun LuminaSnackbar(
    message: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(shapeRounded16)
            .background(SurfaceGlassNightDense)
            .border(width = size1, color = BorderGlassFrost, shape = shapeRounded16)
            .padding(horizontal = spacing16, vertical = spacing14)
            .semantics { liveRegion = LiveRegionMode.Polite }
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = OnGlass
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF00152D)
@Composable
private fun LuminaSnackbarPreview() {
    LuminaTheme {
        LuminaSnackbar(
            message = "An account with this email already exists",
            modifier = Modifier.padding(spacing16)
        )
    }
}
