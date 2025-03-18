package com.samchi.feature.woosung.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.samchi.feature.woosung.data.remote.WoosungPokeRemoteDataSource
import com.samchi.feature.woosung.data.paging.WoosungPagingSource
import com.samchi.poke.model.Pokemon
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


internal class WoosungPokeRepositoryImp @Inject constructor(
    private val woosungPokeRemoteDataSource: WoosungPokeRemoteDataSource
) : WoosungPokeRepository {

    override fun getPokemonList(): Flow<PagingData<Pokemon>> {
        return Pager(
            config = PagingConfig(pageSize = 30, prefetchDistance = 2),
            pagingSourceFactory = {
                WoosungPagingSource(woosungPokeRemoteDataSource)
            }
        ).flow
    }

}
