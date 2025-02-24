package com.samchi.poke.feature.jinkwang.data

import com.samchi.poke.network.PokeApi
import com.samchi.poke.network.dto.ResponsePokemon
import javax.inject.Inject

internal class JinKwangRepositoryImpl @Inject constructor(
    private val pokeApi: PokeApi,
    private val pokemonDao: PokemonDao,
) : JinKwangRepository {

    override suspend fun getPockemonList(
        limit: Int,
        offset: Int
    ): Result<List<Pokemon>> = runCatching {
        val favoritePokemonNames = pokemonDao.getAllPokemons().map { it.name }
        pokeApi.getPokemonList(
            limit = limit,
            offset = offset
        ).results.map {
            it.toPokemon(
                isFavorite = { favoritePokemonNames.contains(it.name) }
            )
        }
    }

    override suspend fun favoritePokemon(name: String) {
        pokemonDao.insert(PokemonEntity(name))
    }

    override suspend fun unFavoritePokemon(name: String) {
        pokemonDao.deletePokemonById(name)
    }

    private inline fun ResponsePokemon.toPokemon(
        isFavorite: () -> Boolean,
    ): Pokemon {
        return Pokemon(
            nameField = name,
            url = url,
            isFavorite = isFavorite(),
        )
    }
}