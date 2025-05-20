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
import com.samchi.poke.model.Pokemon
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class WoosungPokeRepositoryImp @Inject constructor(
    private val woosungPokeRemoteDataSource: WoosungPokeRemoteDataSource,
    private val woosungDao: WoosungDao
) : WoosungPokeRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getPokemonList(): Flow<PagingData<Pokemon>> {
        return Pager(
            config = PagingConfig(pageSize = 30, prefetchDistance = 2),
            remoteMediator = WoosungRemoteMediator(
                woosungDao = woosungDao,
                woosungPokeRemoteDataSource = woosungPokeRemoteDataSource
            ),
            pagingSourceFactory = { woosungDao.getPagingSource() }
        ).flow.map { pagingData ->
            pagingData.map { entity ->
                Pokemon(
                    name = entity.name,
                    url = entity.url
                )
            }
        }
    }
}
