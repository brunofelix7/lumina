package dev.brunofelix.lumina.feature.deck.presentation

data class CreateDeckUiState(
    val name: String = "",
    val isSaving: Boolean = false
) {
    val isSaveEnabled: Boolean
        get() = name.isNotBlank() && !isSaving
}
