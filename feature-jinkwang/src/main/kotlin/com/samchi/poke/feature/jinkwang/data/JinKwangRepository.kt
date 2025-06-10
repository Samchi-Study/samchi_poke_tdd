package com.samchi.poke.feature.jinkwang.data

import androidx.paging.PagingData
import com.samchi.poke.feature.jinkwang.data.local.pokemon.PokemonEntity
import kotlinx.coroutines.flow.Flow

internal interface JinKwangRepository {

    fun getPokemonFlow(): Flow<PagingData<Pokemon>>

    suspend fun getPokemonList(
        limit: Int,
        offset: Int
    ): Result<List<Pokemon>>

    suspend fun favoritePokemon(name: String)

    suspend fun unFavoritePokemon(name: String)
}