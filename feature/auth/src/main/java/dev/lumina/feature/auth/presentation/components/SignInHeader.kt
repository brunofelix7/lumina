package dev.lumina.feature.auth.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import dev.lumina.core.designsystem.theme.LuminaTheme
import dev.lumina.core.designsystem.theme.size204
import dev.lumina.core.designsystem.theme.spacing16
import dev.lumina.core.designsystem.theme.spacing24
import dev.lumina.feature.auth.R
import dev.lumina.core.designsystem.R as DesignSystemR

@Composable
fun SignInHeader(modifier: Modifier = Modifier) {
    Box(modifier = modifier.padding(top = spacing16, bottom = spacing24)) {
        Image(
            painter = painterResource(DesignSystemR.drawable.lumina_logo_glow),
            contentDescription = stringResource(R.string.sign_in_logo_content_description),
            modifier = Modifier
                .padding(bottom = spacing16)
                .size(size204)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF00152D)
@Composable
private fun SignInHeaderPreview() {
    LuminaTheme {
        SignInHeader()
    }
}
