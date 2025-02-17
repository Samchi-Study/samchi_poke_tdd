package com.samchi.feature.woosung

import com.samchi.feature.woosung.data.repository.WoosungPokeRepository
import com.samchi.poke.model.Pokemon

class FakeWoosungPokeRepositoryImp : WoosungPokeRepository {
    override suspend fun getPokemonList(): List<Pokemon> = listOf(
        fakePokemon
    )
}


val fakePokemon = Pokemon(
    name = "bulbasaur",
    url = "https://pokeapi.co/api/v2/pokemon/1/"
)
