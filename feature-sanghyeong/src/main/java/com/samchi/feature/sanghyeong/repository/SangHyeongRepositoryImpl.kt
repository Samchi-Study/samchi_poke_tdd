package com.samchi.feature.sanghyeong.repository

import android.net.Uri
import com.samchi.feature.sanghyeong.data.db.SangHyeongDao
import com.samchi.feature.sanghyeong.data.asSangHyeongDomain
import com.samchi.feature.sanghyeong.data.asSangHyeongEntity
import com.samchi.feature.sanghyeong.model.SangHyeongPokemon
import com.samchi.poke.network.PokeApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import javax.inject.Inject

class SangHyeongRepositoryImpl @Inject constructor(
    private val pokeApi: PokeApi,
    private val dao: SangHyeongDao,
) : SangHyeongRepository {
    private var loading: Boolean = false
    private var offset: String? = null

    override fun getPokemonList(index: Int): Flow<List<SangHyeongPokemon>> {
        return flow {
            if (loading.not()) {
                loading = true
                val result = pokeApi.getPokemonList(limit = LIMIT, offset = index * LIMIT)
                offset = getNextOffset(nextUrl = result.next ?: "")

                dao.insertPokemonList(entities = result.results.map { it.asSangHyeongEntity() })

                emit(value = result.results.map { it.asSangHyeongDomain() })
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