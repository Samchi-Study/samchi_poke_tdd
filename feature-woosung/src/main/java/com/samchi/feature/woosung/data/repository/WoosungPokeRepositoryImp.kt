package com.samchi.feature.woosung.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.samchi.feature.woosung.data.entity.PokemonEntity
import com.samchi.feature.woosung.data.local.WoosungDao
import com.samchi.feature.woosung.data.local.WoosungDatabase
import com.samchi.feature.woosung.data.paging.WoosungRemoteMediator
import com.samchi.feature.woosung.data.remote.WoosungPokeRemoteDataSource
import com.samchi.feature.woosung.domain.model.WoosungPokemon
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class WoosungPokeRepositoryImp @Inject constructor(
    private val woosungPokeRemoteDataSource: WoosungPokeRemoteDataSource,
    private val woosungDatabase: WoosungDatabase,
    private val woosungDao: WoosungDao
) : WoosungPokeRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getPokemonList(): Flow<PagingData<WoosungPokemon>> {
        return Pager(
            config = PagingConfig(pageSize = 30, prefetchDistance = 2),
            remoteMediator = WoosungRemoteMediator(
                woosungDao = woosungDao,
                woosungPokeRemoteDataSource = woosungPokeRemoteDataSource
            ),
            pagingSourceFactory = { woosungDatabase.woosungDao().getPagingSource() }
        ).flow.map { pagingData ->
            pagingData.map { entity ->
                mapEntityToModel(entity)
            }
        }
    }

    override suspend fun toggleFavorite(name: String) {
        val pokemon = woosungDao.getPokemonByName(name)
        pokemon?.let {
            woosungDao.updateFavoriteStatus(name, !it.isFavorite)
        }
    }

    override fun getFavoritePokemonList(): Flow<List<WoosungPokemon>> {
        return woosungDao.getFavoritePokemon().map { entities ->
            entities.map { entity ->
                mapEntityToModel(entity)
            }
        }
    }

    private fun mapEntityToModel(entity: PokemonEntity): WoosungPokemon {
        val url = "https://pokeapi.co/api/v2/pokemon/${entity.id}/"
        return WoosungPokemon(
            name = entity.name,
            url = url,
            isFavorite = entity.isFavorite
        )
    }
}
