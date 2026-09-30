package dev.brunofelix.lumina.feature.auth.presentation.components

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import dev.brunofelix.lumina.core.designsystem.theme.LuminaTheme
import dev.brunofelix.lumina.core.designsystem.theme.PureWhite
import dev.brunofelix.lumina.core.designsystem.theme.TitleLargeBold
import dev.brunofelix.lumina.core.designsystem.theme.size24
import dev.brunofelix.lumina.core.designsystem.theme.spacing12
import dev.brunofelix.lumina.core.designsystem.theme.spacing20
import dev.brunofelix.lumina.core.designsystem.theme.spacing24
import dev.brunofelix.lumina.core.designsystem.theme.spacing4
import dev.brunofelix.lumina.feature.auth.R
import dev.brunofelix.lumina.core.designsystem.R as DesignSystemR

@Composable
fun SignUpHeader(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .offset(x = -spacing12)
            .padding(top = spacing12, bottom = spacing20),
        horizontalArrangement = Arrangement.spacedBy(spacing4),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBackClick) {
            Icon(
                painter = painterResource(DesignSystemR.drawable.ic_arrow_back),
                contentDescription = stringResource(R.string.sign_up_go_back),
                tint = PureWhite,
                modifier = Modifier.size(size24)
            )
        }
        Text(
            text = stringResource(R.string.sign_up_title),
            style = TitleLargeBold,
            color = PureWhite
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF00152D)
@Composable
private fun SignUpHeaderPreview() {
    LuminaTheme {
        SignUpHeader(
            onBackClick = {},
            modifier = Modifier.padding(horizontal = spacing24)
        )
    }
}
