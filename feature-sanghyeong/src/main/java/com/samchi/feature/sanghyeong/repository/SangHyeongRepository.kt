package com.samchi.feature.sanghyeong.repository

import com.samchi.poke.model.Pokemon
import com.samchi.poke.model.PokemonInfo
import kotlinx.coroutines.flow.Flow

interface SangHyeongRepository {
    fun getPokemonPage(
        index: Int,
        onStart: () -> Unit,
        onCompletion: () -> Unit,
        onError: (Throwable) -> Unit,
    ): Flow<List<Pokemon>>
    fun hasMoreData(): Boolean
}