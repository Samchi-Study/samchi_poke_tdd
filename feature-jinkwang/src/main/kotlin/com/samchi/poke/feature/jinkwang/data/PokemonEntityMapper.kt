package com.samchi.poke.feature.jinkwang.data

import com.samchi.poke.feature.jinkwang.data.local.pokemon.PokemonEntity
import com.samchi.poke.network.dto.ResponsePokemon

internal fun ResponsePokemon.toEntity(isFavorite: Boolean): PokemonEntity {
    val index = url.split("/".toRegex()).dropLast(1).last()
    val imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/" +
            "pokemon/other/official-artwork/$index.png"
    return PokemonEntity(
        name = name,
        url = imageUrl,
        isFavorite = isFavorite
    )
}