package com.samchi.poke.feature.jinkwang.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.samchi.poke.feature.jinkwang.data.local.pokemon.PokemonDao
import com.samchi.poke.feature.jinkwang.data.local.pokemon.PokemonEntity
import com.samchi.poke.network.PokeApi

@OptIn(ExperimentalPagingApi::class)
internal class PokemonRemoteMediator(
    private val database: PokemonDatabase,
    private val pokeApi: PokeApi,
    private val pokemonDao: PokemonDao,
) : RemoteMediator<Int, PokemonEntity>() {

    private var next: String? = null
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PokemonEntity>
    ): MediatorResult {

        return try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> REFRESH_LOAD_KEY
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> next
            }

            val nextUrl = loadKey ?: return MediatorResult.Success(endOfPaginationReached = true)

            val response = pokeApi.get(nextUrl)

            val pokemonEntities = response.results.map {
                it.toEntity(false)
            }

            next = response.next

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    pokemonDao.clearAll()
                }
                pokemonDao.insertAll(pokemonEntities)
            }

            MediatorResult.Success(
                endOfPaginationReached = response.next == null
            )
        } catch (e: Exception) {
            e.printStackTrace()
            MediatorResult.Error(e)
        }
    }

    companion object {
        internal const val PAGING_SIZE = 30
        private const val REFRESH_LOAD_KEY = "https://pokeapi.co/api/v2/pokemon?offset=0&limit=30"
    }
}