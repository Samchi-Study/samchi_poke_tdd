package com.samchi.feature.jungwon.data.model

import com.samchi.poke.model.Pokemon
import com.samchi.poke.network.dto.ResponsePokemonInfo

data class PokemonPage(
    val nextUrl: String?,
    val previousUrl: String?,
    val dataList: List<Pokemon>
) {
    val nextOffset: Int?
        get() = nextUrl?.let { url ->
            Regex("offset=(\\d+)").find(url)?.groupValues?.get(1)?.toIntOrNull()
        }

    val previousOffset: Int?
        get() = previousUrl?.let { url ->
            Regex("offset=(\\d+)").find(url)?.groupValues?.get(1)?.toIntOrNull()
        }
}