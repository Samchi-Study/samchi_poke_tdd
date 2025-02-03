package com.samchi.poke.presentation

import com.samchi.poke.model.Pokemon


internal sealed class UiState {
    data class Success(
        val totalCount: Int,
        val pokemonList: List<Pokemon>
    ) : UiState()

    data class Error(
        val throwable: Throwable? = null
    ) : UiState()

    data object Loading : UiState()
}
