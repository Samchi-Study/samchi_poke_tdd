package com.samchi.poke.kanghwi.pagingsource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.samchi.poke.kanghwi.LocalDataSource
import com.samchi.poke.kanghwi.toModel
import com.samchi.poke.model.Pokemon
import com.samchi.poke.network.PokeApi
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class PokemonPagingSource @Inject constructor(
    private val api: PokeApi,
    private val localDataSource: LocalDataSource
) : PagingSource<Int, Pokemon>() {

    private var previousPage: Int = 0

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Pokemon> =
        try {
            val page = params.key ?: START_KEY
            val response = api.getPokemonList(limit = params.loadSize, offset = page)

            val models = response.toModel().results
            val entities = localDataSource.getPokemonList()

            val result = models.fold(mutableListOf<Pokemon>()) { acc, model ->

                acc.add(
                    entities.find { entity -> model.name == entity.name } ?: model
                )

                acc
            }

            val previous = when {
                page == START_KEY || response.previous == null -> null
                else -> previousPage
            }
                .also { previousPage = page }

            val next = when (response.next) {
                null -> null
                else -> page + params.loadSize
            }

            LoadResult.Page(
                data = result,
                prevKey = previous,
                nextKey = next
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }

    override fun getRefreshKey(state: PagingState<Int, Pokemon>): Int {
        val loadResult = state.closestPageToPosition(state.anchorPosition ?: 0)

        if (loadResult == null) return START_KEY

        val size = loadResult.data.size

        return when {
            loadResult.nextKey != null -> loadResult.nextKey!! - size
            loadResult.prevKey != null -> loadResult.prevKey!! + size
            else -> START_KEY
        }
    }


    companion object {

        private const val START_KEY = 0
    }
}
