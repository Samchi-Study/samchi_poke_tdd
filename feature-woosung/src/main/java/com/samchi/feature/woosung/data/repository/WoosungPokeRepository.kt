package com.samchi.feature.woosung.data.repository

import androidx.paging.PagingData
import com.samchi.feature.woosung.domain.model.WoosungPokemon
import kotlinx.coroutines.flow.Flow

interface WoosungPokeRepository {

    fun getPokemonList(): Flow<PagingData<WoosungPokemon>>

    suspend fun toggleFavorite(name: String)

    fun getFavoritePokemonList(): Flow<List<WoosungPokemon>>
}
