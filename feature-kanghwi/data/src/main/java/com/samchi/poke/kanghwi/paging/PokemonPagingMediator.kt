package com.samchi.poke.kanghwi.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.samchi.poke.kanghwi.db.KanghwiDao
import com.samchi.poke.kanghwi.db.entity.PokemonEntity
import com.samchi.poke.kanghwi.toEntity
import com.samchi.poke.network.PokeApi


@OptIn(ExperimentalPagingApi::class)
class PokemonPagingMediator(
    private val pokeApi: PokeApi,
    private val dao: KanghwiDao
) : RemoteMediator<Int, PokemonEntity>() {



    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PokemonEntity>
    ): MediatorResult {
        return try {
            when (loadType) {
                LoadType.REFRESH -> {
                    val localEntities = dao.getPokemonList()

                    if (localEntities.isNotEmpty()) {
                        return MediatorResult.Success(false)
                    } else {
                        val response = pokeApi.getPokemonList(
                            limit = state.config.initialLoadSize,
                            offset = 0
                        )

                        response.results
                            .takeIf { it.isNotEmpty() }
                            ?.sortedBy { it.name }
                            ?.map {
                                it.toEntity(
                                    previous = response.previous,
                                    next = response.next
                                )
                            }
                            ?.run {
                                dao.insertPokemonList(this)
                                return MediatorResult.Success(false)
                            } ?: return MediatorResult.Success(true)
                    }
                }

                LoadType.PREPEND -> return MediatorResult.Success(false)
                LoadType.APPEND -> {
                    val localEntities = dao.getPokemonList()

                    localEntities.lastOrNull()?.getNextOffset()?.let { nextKey ->
                        val response = pokeApi.getPokemonList(
                            limit = state.config.pageSize,
                            offset = nextKey
                        )

                        response.results
                            .takeIf { it.isNotEmpty() }
                            ?.sortedBy { it.name }
                            ?.map {
                                it.toEntity(
                                    previous = response.previous,
                                    next = response.next
                                )
                            }
                            ?.run {
                                dao.insertPokemonList(this)
                                return MediatorResult.Success(false)
                            } ?: return MediatorResult.Success(true)

                    } ?: return MediatorResult.Success(true)
                }
            }
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}
