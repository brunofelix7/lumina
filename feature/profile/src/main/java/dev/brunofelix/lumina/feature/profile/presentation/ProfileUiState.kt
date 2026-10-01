package dev.brunofelix.lumina.feature.profile.presentation

data class ProfileUiState(
    val name: String = "",
    val email: String = "",
    val deckCount: Int = 0,
    val cardCount: Int = 0,
    val appVersion: String = ""
)
