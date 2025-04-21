package com.samchi.feature.jungwon.data.repository

import com.samchi.poke.model.Pokemon
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {
    fun getPokemonListFlow(): Flow<List<Pokemon>>
    suspend fun loadNextPage()
    // 좋아요 관련 메서드 추가
    suspend fun toggleFavorite(pokemon: Pokemon)
}