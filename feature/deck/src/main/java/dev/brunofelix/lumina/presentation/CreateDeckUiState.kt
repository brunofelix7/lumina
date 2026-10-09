package dev.brunofelix.lumina.presentation

data class CreateDeckUiState(
    val name: String = "",
    val isSaving: Boolean = false
) {
    val isSaveEnabled: Boolean
        get() = name.isNotBlank() && !isSaving
}
