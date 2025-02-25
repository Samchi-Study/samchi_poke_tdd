package com.samchi.feature.sanghyeong.repository

import com.samchi.poke.model.Pokemon
import kotlinx.coroutines.flow.Flow

interface SangHyeongRepository {
    fun getPokemonList(
        index: Int,
        onStart: (() -> Unit)? = null,
        onCompletion: (() -> Unit)? = null,
        onError: ((Throwable) -> Unit)? = null,
    ): Flow<List<Pokemon>>
    fun hasMoreData(): Boolean
}