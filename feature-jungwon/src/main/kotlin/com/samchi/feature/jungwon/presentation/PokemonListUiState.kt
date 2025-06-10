package com.samchi.feature.jungwon.presentation

import com.samchi.feature.jungwon.data.model.PokemonPage

sealed class PokemonListUiState {
    object Initial : PokemonListUiState()
    object Loading : PokemonListUiState()
    data class Success(val data: PokemonPage) : PokemonListUiState()
    data class Error(val message: String) : PokemonListUiState()
}