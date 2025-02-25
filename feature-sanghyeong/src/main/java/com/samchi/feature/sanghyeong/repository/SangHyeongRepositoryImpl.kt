package com.samchi.feature.sanghyeong.repository

import android.net.Uri
import com.samchi.feature.sanghyeong.data.asDomain
import com.samchi.poke.model.Pokemon
import com.samchi.poke.network.PokeApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class SangHyeongRepositoryImpl @Inject constructor(
    private val pokeApi: PokeApi
) : SangHyeongRepository {
    private var offset: String? = null
    private var currentPokemonList = mutableListOf<Pokemon>()

    override fun getPokemonList(
        index: Int,
        onStart: (() -> Unit)?,
        onCompletion: (() -> Unit)?,
        onError: ((Throwable) -> Unit)?,
    ): Flow<List<Pokemon>> {
        return flow {
            kotlin.runCatching {
                pokeApi.getPokemonList(limit = LIMIT, offset = index * LIMIT)
            }.onSuccess { result ->
                offset = getNextOffset(offset = result.next ?: "")
                currentPokemonList.addAll(result.results.asDomain())
                emit(value = currentPokemonList)
            }.onFailure { throwable ->
                onError?.invoke(throwable)
            }
        }.onStart {
            onStart?.invoke()
        }.onCompletion {
            onCompletion?.invoke()
        }
    }

    override fun hasMoreData(): Boolean {
        return offset?.isNotEmpty() == true
    }

    companion object {
        private const val LIMIT = 30
    }
}

private fun getNextOffset(offset: String): String {
    return runCatching {
        val uri = Uri.parse(offset)
        uri.getQueryParameter("offset") ?: ""
    }.getOrDefault(defaultValue = "")
}