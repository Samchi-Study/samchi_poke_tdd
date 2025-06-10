package com.samchi.feature.jungwon.presentation.list

import com.samchi.poke.model.Pokemon

sealed class PokemonListUiState {
    data object Initial : PokemonListUiState()
    data object Loading : PokemonListUiState()
    data class Success(val data: List<Pokemon>) : PokemonListUiState()
    data class Error(val message: String) : PokemonListUiState()
}