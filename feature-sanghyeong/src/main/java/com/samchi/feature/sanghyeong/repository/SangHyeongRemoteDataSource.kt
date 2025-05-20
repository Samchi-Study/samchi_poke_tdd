package com.samchi.feature.sanghyeong.repository

import com.samchi.feature.sanghyeong.data.asSangHyeongDomain
import com.samchi.feature.sanghyeong.model.SangHyeongPokemon
import com.samchi.poke.network.PokeApi
import javax.inject.Inject

class SangHyeongRemoteDataSource @Inject constructor(
    private val api: PokeApi,
) : SangHyeongDataSource.Remote {
    override suspend fun getPokemonList(limit: Int, offset: Int): List<SangHyeongPokemon> {
        return api.getPokemonList(limit = limit, offset = offset).results.map { it.asSangHyeongDomain() }
    }
}