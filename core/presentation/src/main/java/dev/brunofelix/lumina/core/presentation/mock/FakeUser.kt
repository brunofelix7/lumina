package dev.brunofelix.lumina.core.presentation.mock

/**
 * Placeholder account shared by the screens until authentication and the user data source exist.
 */
object FakeUser {
    const val NAME = "Bruno Felix"
    const val EMAIL = "brunofelix.dev@gmail.com"
    const val PASSWORD = "supernova"

    val firstName: String = NAME.substringBefore(' ')
}
