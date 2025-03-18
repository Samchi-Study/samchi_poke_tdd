package com.samchi.feature.woosung.mapper

import com.samchi.poke.model.Pokemon
import com.samchi.poke.model.PokemonInfo
import com.samchi.poke.network.dto.ResponsePokemon


internal fun ResponsePokemon.toPokemon() = Pokemon(
    name = this.name,
    url = this.url
)
