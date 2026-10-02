package dev.brunofelix.lumina.feature.deck.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import dev.brunofelix.lumina.core.designsystem.components.LuminaGlassTextField
import dev.brunofelix.lumina.core.designsystem.components.LuminaGlassTextFieldStyle
import dev.brunofelix.lumina.core.designsystem.theme.LuminaTheme
import dev.brunofelix.lumina.core.designsystem.theme.OnGlass
import dev.brunofelix.lumina.core.designsystem.theme.OnGlassSubtle
import dev.brunofelix.lumina.core.designsystem.theme.size20
import dev.brunofelix.lumina.core.designsystem.theme.spacing16
import dev.brunofelix.lumina.feature.deck.R
import dev.brunofelix.lumina.core.designsystem.R as DesignSystemR

@Composable
fun DeckNameTextField(
    value: String,
    onValueChange: (String) -> Unit,
    onClearClick: () -> Unit,
    onDone: () -> Unit,
    modifier: Modifier = Modifier
) {
    LuminaGlassTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = stringResource(R.string.create_deck_name_placeholder),
        leadingIcon = painterResource(DesignSystemR.drawable.ic_style),
        modifier = modifier,
        style = LuminaGlassTextFieldStyle.Haze,
        leadingIconTint = OnGlass,
        trailingContent = if (value.isNotEmpty()) {
            {
                IconButton(onClick = onClearClick) {
                    Icon(
                        painter = painterResource(DesignSystemR.drawable.ic_close),
                        contentDescription = stringResource(R.string.create_deck_clear_name),
                        tint = OnGlassSubtle,
                        modifier = Modifier.size(size20)
                    )
                }
            }
        } else {
            null
        },
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Sentences,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(onDone = { onDone() })
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF00152D)
@Composable
private fun DeckNameTextFieldPreview() {
    LuminaTheme {
        Column(
            modifier = Modifier.padding(spacing16),
            verticalArrangement = Arrangement.spacedBy(spacing16)
        ) {
            DeckNameTextField(value = "", onValueChange = {}, onClearClick = {}, onDone = {})
            DeckNameTextField(value = "Spanish Travel", onValueChange = {}, onClearClick = {}, onDone = {})
        }
    }
}
