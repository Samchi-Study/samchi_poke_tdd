package com.samchi.poke.kanghwi.pagingsource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.samchi.poke.kanghwi.toModel
import com.samchi.poke.model.Pokemon
import com.samchi.poke.network.dto.ResponsePokemonInfo


class PokemonPagingSource(
    private val pageSize: Int,
    private val onRequestPage: suspend (Int, Int) -> ResponsePokemonInfo
) : PagingSource<Int, Pokemon>() {

    private var page = 0


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Pokemon> =
        try {
            page = params.key ?: START_KEY
            val result = onRequestPage(page, params.loadSize)

            if (page == START_KEY) {
                page += params.loadSize / pageSize
            }

            val previous = when (page) {
                START_KEY -> null
                else -> page - 1
            }

            val next = when (result.next) {
                null -> null
                else -> page + 1
            }

            LoadResult.Page(
                data = result.toModel().results,
                prevKey = previous,
                nextKey = next
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }

    override fun getRefreshKey(state: PagingState<Int, Pokemon>): Int {
        return page
    }


    companion object {

        private const val START_KEY = 0
    }
}
