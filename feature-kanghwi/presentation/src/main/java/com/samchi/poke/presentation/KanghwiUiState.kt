package com.samchi.poke.presentation

import com.samchi.poke.model.Pokemon


sealed class KanghwiUiState {
    data class Success(
        val totalCount: Int,
        val pokemonList: List<Pokemon>
    ) : KanghwiUiState()

    data class Error(
        val throwable: Throwable? = null
    ) : KanghwiUiState()

    data object Loading : KanghwiUiState()
}
