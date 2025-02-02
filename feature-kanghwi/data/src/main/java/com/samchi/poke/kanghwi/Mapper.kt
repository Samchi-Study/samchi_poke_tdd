package com.samchi.poke.kanghwi

import com.samchi.poke.model.Pokemon
import com.samchi.poke.model.PokemonInfo
import com.samchi.poke.network.dto.ResponsePokemon
import com.samchi.poke.network.dto.ResponsePokemonInfo


internal fun ResponsePokemonInfo.toModel() = PokemonInfo(
    count = count,
    results = results.map { it.toModel() }
)

internal fun ResponsePokemon.toModel() = Pokemon(
    name = name,
    url = url
)
