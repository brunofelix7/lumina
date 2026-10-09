package dev.brunofelix.lumina.designsystem.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.offset
import dev.brunofelix.lumina.core.designsystem.R
import dev.brunofelix.lumina.designsystem.theme.LuminaTheme
import dev.brunofelix.lumina.designsystem.theme.OnGlass
import dev.brunofelix.lumina.designsystem.theme.TitleLargeBold
import dev.brunofelix.lumina.designsystem.theme.size24
import dev.brunofelix.lumina.designsystem.theme.spacing12
import dev.brunofelix.lumina.designsystem.theme.spacing16
import dev.brunofelix.lumina.designsystem.theme.spacing4
import dev.brunofelix.lumina.designsystem.theme.spacing8

@Composable
fun LuminaTopBar(
    title: String,
    onBackClick: () -> Unit,
    backContentDescription: String,
    modifier: Modifier = Modifier,
    actions: @Composable RowScope.() -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = spacing8)
            .bleedHorizontally(spacing12),
        horizontalArrangement = Arrangement.spacedBy(spacing4),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBackClick) {
            Icon(
                painter = painterResource(R.drawable.ic_arrow_back),
                contentDescription = backContentDescription,
                tint = OnGlass,
                modifier = Modifier.size(size24)
            )
        }
        Text(
            text = title,
            style = TitleLargeBold,
            color = OnGlass,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )
        CompositionLocalProvider(LocalContentColor provides OnGlass) {
            actions()
        }
    }
}

/**
 * Widens the layout by [inset] on both sides, so the glyphs inside 48dp icon buttons
 * line up with the screen's content edges instead of their touch target edges.
 */
private fun Modifier.bleedHorizontally(inset: Dp): Modifier = layout { measurable, constraints ->
    val insetPx = inset.roundToPx()
    val placeable = measurable.measure(constraints.offset(horizontal = insetPx * 2))
    layout(placeable.width - insetPx * 2, placeable.height) {
        placeable.place(-insetPx, 0)
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF00152D)
@Composable
private fun LuminaTopBarPreview() {
    LuminaTheme {
        LuminaTopBar(
            title = "Profile",
            onBackClick = {},
            backContentDescription = "Go back",
            modifier = Modifier.padding(horizontal = spacing16)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF00152D)
@Composable
private fun LuminaTopBarWithActionPreview() {
    LuminaTheme {
        LuminaTopBar(
            title = "Create Deck",
            onBackClick = {},
            backContentDescription = "Go back",
            modifier = Modifier.padding(horizontal = spacing16),
            actions = {
                IconButton(onClick = {}) {
                    Icon(
                        painter = painterResource(R.drawable.ic_check),
                        contentDescription = "Save deck",
                        modifier = Modifier.size(size24)
                    )
                }
            }
        )
    }
}
