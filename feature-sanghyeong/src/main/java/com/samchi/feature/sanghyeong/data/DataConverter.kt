package com.samchi.feature.sanghyeong.data

import com.samchi.poke.model.Pokemon
import com.samchi.poke.model.PokemonInfo
import com.samchi.poke.network.dto.ResponsePokemon
import com.samchi.poke.network.dto.ResponsePokemonInfo

internal fun ResponsePokemonInfo.toPokemonInfo(): PokemonInfo {
    return PokemonInfo(
        count = this.count,
        results = this.results.map { it.toPokemon() }
    )
}

private fun ResponsePokemon.toPokemon(): Pokemon {
    return Pokemon(
        name = this.name,
        url = this.url,
    )
}