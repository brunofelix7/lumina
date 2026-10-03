package dev.brunofelix.lumina.core.presentation.util.extension

import dev.brunofelix.lumina.core.domain.model.User

/**
 * The name to show for this user, falling back to the local part of the email when the account has no name.
 */
val User.displayName: String
    get() = name.trim().ifBlank { email.substringBefore('@') }

/**
 * The first word of [displayName].
 */
val User.firstName: String
    get() = displayName.substringBefore(' ')
