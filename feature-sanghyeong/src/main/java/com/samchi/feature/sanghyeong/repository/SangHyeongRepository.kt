package com.samchi.feature.sanghyeong.repository

import com.samchi.poke.model.Pokemon
import kotlinx.coroutines.flow.Flow

interface SangHyeongRepository {
    fun getPokemonList(index: Int): Flow<List<Pokemon>>
    fun hasMoreData(): Boolean
}