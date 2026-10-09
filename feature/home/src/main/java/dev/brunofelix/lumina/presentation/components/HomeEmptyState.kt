package dev.brunofelix.lumina.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import dev.brunofelix.lumina.designsystem.components.LuminaGradientBackground
import dev.brunofelix.lumina.designsystem.components.glowShadow
import dev.brunofelix.lumina.designsystem.theme.BodyMediumRelaxed
import dev.brunofelix.lumina.designsystem.theme.LuminaTheme
import dev.brunofelix.lumina.designsystem.theme.OnGlassSubtle
import dev.brunofelix.lumina.designsystem.theme.PrimaryGlassAura
import dev.brunofelix.lumina.designsystem.theme.PrimaryGlassBorderSoft
import dev.brunofelix.lumina.designsystem.theme.PrimaryGlassShadow
import dev.brunofelix.lumina.designsystem.theme.PureWhite
import dev.brunofelix.lumina.designsystem.theme.SurfaceGlassNightDense
import dev.brunofelix.lumina.designsystem.theme.TitleLargeBold
import dev.brunofelix.lumina.designsystem.theme.shapeCircle
import dev.brunofelix.lumina.designsystem.theme.size1
import dev.brunofelix.lumina.designsystem.theme.size12
import dev.brunofelix.lumina.designsystem.theme.size260
import dev.brunofelix.lumina.designsystem.theme.size32
import dev.brunofelix.lumina.designsystem.theme.size36
import dev.brunofelix.lumina.designsystem.theme.size480
import dev.brunofelix.lumina.designsystem.theme.size80
import dev.brunofelix.lumina.designsystem.theme.size96
import dev.brunofelix.lumina.designsystem.theme.spacing16
import dev.brunofelix.lumina.designsystem.theme.spacing24
import dev.brunofelix.lumina.designsystem.theme.spacing8
import dev.brunofelix.lumina.feature.home.R
import dev.brunofelix.lumina.core.designsystem.R as DesignSystemR

@Composable
fun HomeEmptyState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = size480)
            .padding(horizontal = spacing16),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        EmptyDecksIcon(modifier = Modifier.padding(bottom = spacing24))
        Text(
            text = stringResource(R.string.home_empty_title),
            style = TitleLargeBold,
            color = PureWhite,
            textAlign = TextAlign.Center
        )
        Text(
            text = stringResource(R.string.home_empty_description),
            style = BodyMediumRelaxed,
            color = OnGlassSubtle,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(top = spacing8)
                .widthIn(max = size260)
        )
    }
}

@Composable
private fun EmptyDecksIcon(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.size(size80),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .requiredSize(size96)
                .blur(radius = size12, edgeTreatment = BlurredEdgeTreatment.Unbounded)
                .background(Brush.radialGradient(colors = listOf(PrimaryGlassAura, Color.Transparent)))
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .glowShadow(
                    color = PrimaryGlassShadow,
                    blurRadius = size32,
                    shape = shapeCircle,
                    offsetY = spacing8
                )
                .clip(shapeCircle)
                .background(SurfaceGlassNightDense)
                .border(width = size1, color = PrimaryGlassBorderSoft, shape = shapeCircle),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(DesignSystemR.drawable.ic_style_filled),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(size36)
            )
        }
    }
}

@Preview(widthDp = 390, heightDp = 560)
@Composable
private fun HomeEmptyStatePreview() {
    LuminaTheme {
        LuminaGradientBackground {
            HomeEmptyState()
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF00152D)
@Composable
private fun EmptyDecksIconPreview() {
    LuminaTheme {
        EmptyDecksIcon(modifier = Modifier.padding(spacing24))
    }
}
