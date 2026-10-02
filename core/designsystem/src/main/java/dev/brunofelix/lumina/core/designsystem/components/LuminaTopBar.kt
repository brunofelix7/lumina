package dev.brunofelix.lumina.core.designsystem.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import dev.brunofelix.lumina.core.designsystem.R
import dev.brunofelix.lumina.core.designsystem.theme.LuminaTheme
import dev.brunofelix.lumina.core.designsystem.theme.OnGlass
import dev.brunofelix.lumina.core.designsystem.theme.TitleLargeBold
import dev.brunofelix.lumina.core.designsystem.theme.size24
import dev.brunofelix.lumina.core.designsystem.theme.spacing12
import dev.brunofelix.lumina.core.designsystem.theme.spacing16
import dev.brunofelix.lumina.core.designsystem.theme.spacing4
import dev.brunofelix.lumina.core.designsystem.theme.spacing8

@Composable
fun LuminaTopBar(
    title: String,
    onBackClick: () -> Unit,
    backContentDescription: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            // Pulls the 48dp touch target back by its inner inset so the arrow glyph
            // lines up with the screen's content edge.
            .offset(x = -spacing12)
            .padding(top = spacing8),
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
            color = OnGlass
        )
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
