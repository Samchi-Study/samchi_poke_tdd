package com.samchi.feature.woosung.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.samchi.feature.woosung.data.entity.PokemonEntity
import com.samchi.feature.woosung.data.local.WoosungDao
import com.samchi.feature.woosung.data.remote.WoosungPokeRemoteDataSource
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
internal class WoosungRemoteMediator(
    private val woosungDao: WoosungDao,
    private val woosungPokeRemoteDataSource: WoosungPokeRemoteDataSource
) : RemoteMediator<Int, PokemonEntity>() {

    private var currentPage = 1

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PokemonEntity>
    ): MediatorResult {
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> {
                    currentPage = 1
                    currentPage
                }

                LoadType.PREPEND -> {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }

                LoadType.APPEND -> {
                    currentPage++
                    currentPage
                }
            }

            val response = woosungPokeRemoteDataSource.getPokemonList(page = page)

            // 응답을 Entity로 변환
            val pokemonEntities = response.results.mapIndexed { index, pokemonItem ->
                // API에서 반환된 URL에서 포켓몬 ID 추출
                val pokemonId = extractPokemonIdFromUrl(pokemonItem.url)

                PokemonEntity(
                    id = pokemonId,
                    name = pokemonItem.name,
                    url = pokemonItem.url,
                    isFavorite = false
                )
            }

            // 로컬 DB에 저장
            if (loadType == LoadType.REFRESH) {
                // 테이블 비우는 로직이 필요하다면 여기에 추가
            }

            woosungDao.insertPokemonList(pokemonEntities)

            MediatorResult.Success(
                endOfPaginationReached = response.results.isEmpty()
            )
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }

    private fun extractPokemonIdFromUrl(url: String): Int {
        // URL 형식: "https://pokeapi.co/api/v2/pokemon/{id}/"
        val idString = url.trim('/').split("/").last()
        return idString.toIntOrNull() ?: 0
    }
}
