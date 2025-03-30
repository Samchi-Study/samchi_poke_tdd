package com.samchi.poke.kanghwi.pagingsource

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.samchi.poke.kanghwi.db.entity.PokemonEntity
import kotlin.math.truncate

//@OptIn(ExperimentalPagingApi::class)
//class PokemonPagingMediator : RemoteMediator<Int, PokemonEntity>() {
//
//    override suspend fun load(
//        loadType: LoadType,
//        state: PagingState<Int, PokemonEntity>
//    ): MediatorResult {
//        val loadKey = when (loadType) {
//            LoadType.REFRESH -> null
//            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
//            LoadType.APPEND -> {
//                val lastItem = state.lastItemOrNull()
//                    ?: return MediatorResult.Success(endOfPaginationReached = true)
//
//                lastItem.id
//            }
//        }
//    }
//}