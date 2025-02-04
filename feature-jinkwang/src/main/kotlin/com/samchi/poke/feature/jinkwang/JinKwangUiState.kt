package com.samchi.poke.feature.jinkwang

import com.samchi.poke.feature.jinkwang.data.Pokemon

internal sealed interface JinKwangUiState {

    data object Loading : JinKwangUiState

    data class Success(
        val pokemonList: List<Pokemon>
    ) : JinKwangUiState
}