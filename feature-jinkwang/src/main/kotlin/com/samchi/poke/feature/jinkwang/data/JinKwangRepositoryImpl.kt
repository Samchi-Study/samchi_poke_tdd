package com.samchi.poke.feature.jinkwang.data

import com.samchi.poke.network.PokeApi
import com.samchi.poke.network.dto.ResponsePokemon
import javax.inject.Inject

internal class JinKwangRepositoryImpl @Inject constructor(
    private val pokeApi: PokeApi
) : JinKwangRepository {

    override suspend fun getPockemonList(
        limit: Int,
        offset: Int
    ): Result<List<Pokemon>> = runCatching {
        pokeApi.getPokemonList(
            limit = limit,
            offset = offset
        ).results.map {
            it.toPokemon()
        }
    }

    private fun ResponsePokemon.toPokemon(): Pokemon {
        return Pokemon(
            nameField = name,
            url = url
        )
    }
}