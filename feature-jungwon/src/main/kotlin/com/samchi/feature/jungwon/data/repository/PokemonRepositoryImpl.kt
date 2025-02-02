package com.samchi.feature.jungwon.data.repository

import com.samchi.feature.jungwon.data.model.PokemonPage
import com.samchi.feature.jungwon.data.model.toPokemonPage
import com.samchi.poke.network.PokeApi
import com.samchi.poke.network.dto.ResponsePokemonInfo
import javax.inject.Inject

class PokemonRepositoryImpl @Inject constructor(
    private val pokeApi: PokeApi
): PokemonRepository {
    override suspend fun getPokemonPage(limit: Int, offset: Int): Result<PokemonPage> {
        return runCatching {
            val response: ResponsePokemonInfo = pokeApi.getPokemonList(limit, offset)

            response.toPokemonPage()
        }
    }
}