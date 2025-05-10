package com.samchi.poke.kanghwi.presentation


internal sealed class UiState {

    data class Error(
        val throwable: Throwable? = null
    ) : UiState()

    data object Loading : UiState()
}
