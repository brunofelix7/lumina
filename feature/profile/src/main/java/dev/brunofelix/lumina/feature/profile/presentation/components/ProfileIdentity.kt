package dev.brunofelix.lumina.feature.profile.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import dev.brunofelix.lumina.core.designsystem.theme.BorderGlass
import dev.brunofelix.lumina.core.designsystem.theme.HeadlineMediumBold
import dev.brunofelix.lumina.core.designsystem.theme.LuminaTheme
import dev.brunofelix.lumina.core.designsystem.theme.SurfaceGlassNight
import dev.brunofelix.lumina.core.designsystem.theme.shapeCircle
import dev.brunofelix.lumina.core.designsystem.theme.size1
import dev.brunofelix.lumina.core.designsystem.theme.size48
import dev.brunofelix.lumina.core.designsystem.theme.size96
import dev.brunofelix.lumina.core.designsystem.theme.spacing12
import dev.brunofelix.lumina.core.designsystem.theme.spacing24
import dev.brunofelix.lumina.core.designsystem.theme.spacing4
import dev.brunofelix.lumina.core.designsystem.R as DesignSystemR

@Composable
fun ProfileIdentity(
    name: String,
    email: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .padding(bottom = spacing12)
                .size(size96)
                .clip(shapeCircle)
                .background(SurfaceGlassNight)
                .border(width = size1, color = BorderGlass, shape = shapeCircle),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(DesignSystemR.drawable.ic_person),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(size48)
            )
        }
        Text(
            text = name,
            style = HeadlineMediumBold,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )
        Text(
            text = email,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = spacing4)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF00152D)
@Composable
private fun ProfileIdentityPreview() {
    LuminaTheme {
        ProfileIdentity(
            name = "Bruno Felix",
            email = "brunofelix.dev@gmail.com",
            modifier = Modifier.padding(spacing24)
        )
    }
}
