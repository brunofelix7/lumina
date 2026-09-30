package dev.brunofelix.lumina.feature.auth.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.brunofelix.lumina.core.designsystem.theme.LabelSmallWide
import dev.brunofelix.lumina.core.designsystem.theme.LuminaTheme
import dev.brunofelix.lumina.core.designsystem.theme.size1
import dev.brunofelix.lumina.core.designsystem.theme.spacing16

@Composable
fun AuthDivider(
    text: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(spacing16),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            thickness = size1,
            color = MaterialTheme.colorScheme.surfaceContainerHighest
        )
        Text(
            text = text.uppercase(),
            style = LabelSmallWide,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            thickness = size1,
            color = MaterialTheme.colorScheme.surfaceContainerHighest
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF00152D)
@Composable
private fun AuthDividerPreview() {
    LuminaTheme {
        AuthDivider(
            text = "Or continue with",
            modifier = Modifier.padding(spacing16)
        )
    }
}
