package com.samchi.poke.kanghwi

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.map
import androidx.room.RoomDatabase
import com.samchi.poke.kanghwi.db.KanghwiDatabase
import com.samchi.poke.kanghwi.paging.PokemonPagingMediator
import com.samchi.poke.network.PokeApi
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton


@OptIn(ExperimentalPagingApi::class)
@Singleton
class RemoteDataSource @Inject constructor(
    private val api: PokeApi,
    private val db: RoomDatabase
) {

    fun getPokemonPagingFlow() = Pager(
        config = PagingConfig(
            pageSize = 30,
            prefetchDistance = 90,
            enablePlaceholders = false
        ),
        remoteMediator = PokemonPagingMediator(api, db),
        pagingSourceFactory = { (db as KanghwiDatabase).kanghwiDao().getPagingSource() }
    )
        .flow
        .map { pagingData ->
            pagingData.map { it.toModel() }
        }
}
