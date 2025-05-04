package com.samchi.poke.kanghwi.repository

import androidx.paging.PagingData
import com.samchi.poke.kanghwi.db.entity.FavoritePokemonEntity
import com.samchi.poke.kanghwi.model.Pokemon
import kotlinx.coroutines.flow.Flow


interface KanghwiRepository {

    fun getPokemonPagingFlow(): Flow<PagingData<Pokemon>>

    suspend fun getPokemonList(): List<Pokemon>

    suspend fun upsertPokemon(model: Pokemon)

    suspend fun updatePokemon(model: Pokemon)

    suspend fun deletePokemon(model: Pokemon)

    fun getFavoritePokemonList(): Flow<List<FavoritePokemonEntity>>

    suspend fun insertFavoritePokemon(model: Pokemon)

    suspend fun deleteFavoritePokemon(model: Pokemon)
}
