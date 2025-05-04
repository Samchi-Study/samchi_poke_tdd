package com.samchi.poke.kanghwi.repository

import androidx.paging.PagingData
import com.samchi.poke.kanghwi.db.entity.FavoritePokemonEntity
import com.samchi.poke.kanghwi.model.Pokemon
import kotlinx.coroutines.flow.Flow


internal class KanghwiRepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : KanghwiRepository {

    override fun getPokemonPagingFlow(): Flow<PagingData<Pokemon>> =
        remoteDataSource.getPokemonPagingFlow()

    override suspend fun getPokemonList(): List<Pokemon> =
        localDataSource.getPokemonList()

    override suspend fun upsertPokemon(model: Pokemon) =
        localDataSource.updatePokemon(model)

    override suspend fun updatePokemon(model: Pokemon) =
        localDataSource.updatePokemon(model)

    override suspend fun deletePokemon(model: Pokemon) =
        localDataSource.deletePokemon(model)

    override suspend fun getFavoritePokemonList(): List<FavoritePokemonEntity> =
        localDataSource.getFavoritePokemonList()

    override suspend fun insertFavoritePokemon(model: Pokemon) =
        localDataSource.insertFavoritePokemon(model)

    override suspend fun deleteFavoritePokemon(model: Pokemon) =
        localDataSource.deleteFavoritePokemon(model)
}
