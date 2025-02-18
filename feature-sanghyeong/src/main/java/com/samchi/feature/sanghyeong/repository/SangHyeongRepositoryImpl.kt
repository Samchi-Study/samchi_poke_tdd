package com.samchi.feature.sanghyeong.repository

import android.net.Uri
import com.samchi.feature.sanghyeong.data.toPokemonInfo
import com.samchi.poke.model.PokemonInfo
import com.samchi.poke.network.PokeApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SangHyeongRepositoryImpl @Inject constructor(
    private val pokeApi: PokeApi
) : SangHyeongRepository {
    private var offset: String? = null

    override suspend fun getPokemonPage(): Flow<Result<PokemonInfo>> {
        return flow {
            kotlin.runCatching {
                pokeApi.getPokemonList(offset = 0)
            }.onSuccess { result ->
                offset = getNextOffset(offset = result.next ?: "")
                emit(Result.success(value = result.toPokemonInfo()))
            }.onFailure { throwable ->
                emit(Result.failure(exception = throwable))
            }
        }
    }

    override suspend fun getNextPokemonPage(): Flow<Result<PokemonInfo>> {
        return flow {
            kotlin.runCatching {
                pokeApi.getPokemonList(offset = offset?.toInt() ?: 0)
            }.onSuccess { result ->
                offset = getNextOffset(offset = result.next ?: "")
                emit(Result.success(value = result.toPokemonInfo()))
            }.onFailure { throwable ->
                emit(Result.failure(exception = throwable))
            }
        }
    }

    override fun hasMoreData(): Boolean {
        return offset?.isNotEmpty() == true
    }
}

private fun getNextOffset(offset: String): String {
    return runCatching {
        val uri = Uri.parse(offset)
        uri.getQueryParameter("offset") ?: ""
    }.getOrDefault(defaultValue = "")
}