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
            val loadKey = when (loadType) {
                LoadType.REFRESH -> null
                LoadType.PREPEND -> return MediatorResult.Success(false)
                LoadType.APPEND -> {
                    state.lastItemOrNull()?.getNextOffset()
                        ?: return MediatorResult.Success(true)
                }
            }

            val response = loadKey?.let { offset ->
                pokeApi.getPokemonList(
                    offset = offset
                )
            } ?: pokeApi.getPokemonList(
                limit = state.config.initialLoadSize,
                offset = 0
            )

            response.results.sortedBy { it.name }
                .map {
                    it.toEntity(
                        previous = response.previous,
                        next = response.next
                    )
                }
                .also { entities ->
                    if (entities.isEmpty()) return@also

                    dao.insertPokemonList(entities)
                }

            MediatorResult.Success(response.results.isEmpty() || response.next == null)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}
