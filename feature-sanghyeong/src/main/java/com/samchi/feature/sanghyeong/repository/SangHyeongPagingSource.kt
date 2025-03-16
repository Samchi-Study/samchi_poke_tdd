package com.samchi.feature.sanghyeong.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.samchi.feature.sanghyeong.model.SangHyeongPokemon
import javax.inject.Inject

class SangHyeongPagingSource @Inject constructor(
    private val remote: SangHyeongDataSource.Remote
) : PagingSource<Int, SangHyeongPokemon>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SangHyeongPokemon> {
        return try {
            val page = params.key ?: 0
            val response = remote.getPokemonList(limit = LIMIT, offset = LIMIT * page)

            LoadResult.Page(
                data = response,
                prevKey = if (page == 0) null else page - 1,
                nextKey = if (response.isEmpty()) null else page + 1
            )
        } catch(e: Exception) {
            LoadResult.Error(throwable = e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, SangHyeongPokemon>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition = anchorPosition)?.prevKey
        }
    }

    companion object {
        private const val LIMIT = 30
    }
}