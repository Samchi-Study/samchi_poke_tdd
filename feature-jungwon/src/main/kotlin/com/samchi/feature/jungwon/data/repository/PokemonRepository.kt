package com.samchi.feature.jungwon.data.repository

import com.samchi.feature.jungwon.data.model.PokemonPage
import com.samchi.poke.model.Pokemon
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {
    suspend fun getPokemonPage(limit: Int = 20, offset: Int = 0): Result<PokemonPage>
    
    // 좋아요 관련 메서드 추가
    suspend fun toggleFavorite(pokemon: Pokemon)
    fun getFavoritePokemonIds(): Flow<Set<String>>
    suspend fun isFavorite(pokemon: Pokemon): Boolean
}