package dev.brunofelix.lumina.core.presentation.util

import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource

/**
 * Like [pluralStringResource], but reads [zeroResId] when [count] is zero. Plural rules follow the
 * device locale, and some locales (Portuguese, for instance) classify zero as "one", which would
 * render the English singular ("0 Deck").
 */
@Composable
fun pluralStringResourceWithZero(
    @PluralsRes id: Int,
    @StringRes zeroResId: Int,
    count: Int
): String {
    return if (count == 0) stringResource(zeroResId) else pluralStringResource(id, count, count)
}
