package dev.brunofelix.lumina.domain.util.extension

private val EMAIL_REGEX = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

/**
 * Whether this string is a well-formed email address. Surrounding whitespace is not trimmed.
 */
fun String.isValidEmail(): Boolean = EMAIL_REGEX.matches(this)
