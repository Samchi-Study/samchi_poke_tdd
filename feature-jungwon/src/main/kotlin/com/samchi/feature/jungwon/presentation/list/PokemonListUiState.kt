package com.samchi.feature.jungwon.presentation.list

import com.samchi.feature.jungwon.data.model.PokemonPage

sealed class PokemonListUiState {
    data object Initial : PokemonListUiState()
    data object Loading : PokemonListUiState()
    data class Success(val data: PokemonPage) : PokemonListUiState()
    data class Error(val message: String) : PokemonListUiState()
}