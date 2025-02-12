package com.samchi.feature.sanghyeong.ui

import com.samchi.poke.model.Pokemon

data class SangHyeongUiState(
    val loading: Boolean = false,
    val error: Throwable? = null,

    val pokemonList: List<Pokemon> = listOf(),
)
