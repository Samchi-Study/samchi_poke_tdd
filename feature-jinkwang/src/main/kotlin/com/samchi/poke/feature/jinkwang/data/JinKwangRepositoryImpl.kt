package com.samchi.poke.feature.jinkwang.data

import com.samchi.poke.network.PokeApi
import com.samchi.poke.network.dto.ResponsePokemon
import javax.inject.Inject

internal class JinKwangRepositoryImpl @Inject constructor(
    private val pokeApi: PokeApi
) : JinKwangRepository {

    override suspend fun getPockemonList(offset: Int): Result<List<Pokemon>> = runCatching {
        pokeApi.getPokemonList(
            offset = offset
        ).results.map {
            it.toPokemon()
        }
    }

    private fun ResponsePokemon.toPokemon(): Pokemon {
        return Pokemon(
            name = name,
            url = url
        )
    }
}