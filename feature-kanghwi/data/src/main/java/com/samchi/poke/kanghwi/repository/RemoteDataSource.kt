package com.samchi.poke.kanghwi.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.map
import com.samchi.poke.kanghwi.db.KanghwiDao
import com.samchi.poke.kanghwi.paging.PokemonPagingMediator
import com.samchi.poke.kanghwi.toModel
import com.samchi.poke.network.PokeApi
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton


@OptIn(ExperimentalPagingApi::class)
@Singleton
internal class RemoteDataSource @Inject constructor(
    private val api: PokeApi,
    private val dao: KanghwiDao
) {

    fun getPokemonPagingFlow() = Pager(
        config = PagingConfig(
            pageSize = 100,
            enablePlaceholders = false
        ),
        remoteMediator = PokemonPagingMediator(api, dao),
        pagingSourceFactory = { dao.getPagingSource() }
    )
        .flow
        .map { pagingData ->
            pagingData.map { it.toModel() }
        }
}
