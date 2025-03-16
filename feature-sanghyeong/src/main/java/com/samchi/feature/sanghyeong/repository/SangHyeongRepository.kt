package com.samchi.feature.sanghyeong.repository

import androidx.paging.PagingData
import com.samchi.feature.sanghyeong.data.db.entity.SangHyeongPokemonEntity
import com.samchi.feature.sanghyeong.model.SangHyeongPokemon
import kotlinx.coroutines.flow.Flow

interface SangHyeongRepository {
    fun getPokemonListFlow(): Flow<PagingData<SangHyeongPokemon>>
    fun getFavoritePokemonsFlow(): Flow<List<SangHyeongPokemonEntity>>
    fun hasMoreData(): Boolean
    suspend fun toggleFavorite(pokemon: SangHyeongPokemon)
}