package com.samchi.poke.kanghwi

import com.samchi.poke.kanghwi.db.entity.PokemonEntity
import com.samchi.poke.kanghwi.model.Pokemon
import com.samchi.poke.kanghwi.model.PokemonInfo
import com.samchi.poke.network.dto.ResponsePokemon
import com.samchi.poke.network.dto.ResponsePokemonInfo


internal fun ResponsePokemonInfo.toModel() = PokemonInfo(
    count = count,
    results = results.map { it.toModel() }
)

internal fun ResponsePokemon.toModel() = Pokemon(
    name = name,
    url = url,
    isFavorite = false
)

internal fun Pokemon.toEntity() = PokemonEntity(
    name = name,
    url = getImageUrl(),
    isFavorite = isFavorite
)