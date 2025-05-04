package com.samchi.poke.kanghwi.repository

import androidx.paging.PagingData
import androidx.paging.map
import com.samchi.poke.kanghwi.db.entity.FavoritePokemonEntity
import com.samchi.poke.kanghwi.model.Pokemon
import com.samchi.poke.kanghwi.toEntity
import com.samchi.poke.kanghwi.toFavoriteEntity
import com.samchi.poke.kanghwi.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


internal class KanghwiRepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : KanghwiRepository {

    override fun getPokemonPagingFlow(): Flow<PagingData<Pokemon>> =
        remoteDataSource.getPokemonPagingFlow()
            .map { pagingData ->
                pagingData.map { entity ->
                    entity.toModel()
                }
            }

    override suspend fun getPokemonList(): List<Pokemon> =
        localDataSource.getPokemonList().map { it.toModel() }

    override suspend fun upsertPokemon(model: Pokemon) =
        localDataSource.updatePokemon(model.toEntity())

    override suspend fun updatePokemon(model: Pokemon) =
        localDataSource.updatePokemon(model.toEntity())

    override suspend fun deletePokemon(model: Pokemon) =
        localDataSource.deletePokemon(model.toEntity())

    override suspend fun getFavoritePokemonList(): List<FavoritePokemonEntity> =
        localDataSource.getFavoritePokemonList()

    override suspend fun insertFavoritePokemon(model: Pokemon) =
        localDataSource.insertFavoritePokemon(model.toFavoriteEntity())

    override suspend fun deleteFavoritePokemon(model: Pokemon) =
        localDataSource.deleteFavoritePokemon(model.toFavoriteEntity())
}
