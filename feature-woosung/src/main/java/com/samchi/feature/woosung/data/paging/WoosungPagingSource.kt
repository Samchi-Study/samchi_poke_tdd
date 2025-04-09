package com.samchi.feature.woosung.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.samchi.feature.woosung.data.remote.WoosungPokeRemoteDataSource
import com.samchi.poke.model.Pokemon
import retrofit2.HttpException
import java.io.IOException

internal class WoosungPagingSource internal constructor(
    private val woosungPokeRemoteDataSource: WoosungPokeRemoteDataSource
) : PagingSource<Int, Pokemon>() {


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Pokemon> {
        return try {
            val currentPage = params.key ?: 1
            val pokemonList = woosungPokeRemoteDataSource.getPokemonList(
                page = currentPage
            )
            LoadResult.Page(
                data = pokemonList.results.map {
                    Pokemon(
                        name = it.name,
                        url = it.url
                    )
                },
                prevKey = if (currentPage == 1) null else extractLimit(pokemonList.previous),
                nextKey = if (pokemonList.results.isEmpty()) null else extractLimit(pokemonList.next)
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }

    }


    override fun getRefreshKey(state: PagingState<Int, Pokemon>): Int? {
        return state.anchorPosition
    }

    fun extractLimit(url: String?): Int? {
        return url?.substringAfter("?", "")
            ?.split("&")
            ?.mapNotNull { it.split("=").takeIf { it.size == 2 } }
            ?.firstOrNull { it[0] == "limit" }
            ?.get(1)
            ?.removeSuffix("\"") // 잘못된 인코딩 제거
            ?.toInt()
    }

}
