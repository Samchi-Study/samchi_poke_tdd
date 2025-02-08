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

/*
* TODO: ResponsePokemonInfo 에 next, previous 정보를 추가해서 PokemonPage 정보를 제거 작업 필요
* */
fun ResponsePokemonInfo.toPokemonPage(): PokemonPage {
    return PokemonPage(
        nextUrl = next,
        previousUrl = previous,
        dataList = results.map {
            Pokemon(
                name = it.name,
                url = it.url
            )
        }
    )
}