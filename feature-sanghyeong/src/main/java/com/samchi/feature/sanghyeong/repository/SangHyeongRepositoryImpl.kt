package com.samchi.feature.sanghyeong.repository

import android.net.Uri
import com.samchi.feature.sanghyeong.data.toPokemon
import com.samchi.poke.model.Pokemon
import com.samchi.poke.network.PokeApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import javax.inject.Inject

class SangHyeongRepositoryImpl @Inject constructor(
    private val pokeApi: PokeApi
) : SangHyeongRepository {
    private var loading: Boolean = false
    private var offset: String? = null

    override fun getPokemonList(index: Int): Flow<List<Pokemon>> {
        return flow {
            if (loading.not()) {
                loading = true
                val result = pokeApi.getPokemonList(limit = LIMIT, offset = index * LIMIT)
                offset = getNextOffset(nextUrl = result.next ?: "")
                emit(value = result.results.map { it.toPokemon() })
            }
        }.onCompletion {
            loading = false
        }
    }

    override fun hasMoreData(): Boolean {
        return offset?.isNotEmpty() == true
    }

    private fun getNextOffset(nextUrl: String): String {
        return runCatching {
            val uri = Uri.parse(nextUrl)
            uri.getQueryParameter("offset") ?: ""
        }.getOrDefault(defaultValue = "")
    }

    companion object {
        private const val LIMIT = 30
    }
}