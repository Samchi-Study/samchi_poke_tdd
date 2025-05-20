package com.samchi.feature.sanghyeong.data

import com.samchi.feature.sanghyeong.data.db.entity.SangHyeongPokemonEntity
import com.samchi.feature.sanghyeong.model.SangHyeongPokemon
import com.samchi.poke.network.dto.ResponsePokemon

internal fun ResponsePokemon.asSangHyeongDomain(): SangHyeongPokemon {
    return SangHyeongPokemon(
        name = this.name,
        url = this.url,
        isFavorite = false,
    )
}

internal fun ResponsePokemon.asSangHyeongEntity(): SangHyeongPokemonEntity {
    return SangHyeongPokemonEntity(
        name = this.name,
        url = this.url,
        isFavorite = false,
    )
}

internal fun SangHyeongPokemon.asSangHyeongEntity(): SangHyeongPokemonEntity {
    return SangHyeongPokemonEntity(
        name = this.name,
        url = this.url,
        isFavorite = this.isFavorite
    )
}

internal fun SangHyeongPokemonEntity.asSangHyeongDomain(): SangHyeongPokemon {
    return SangHyeongPokemon(
        name = this.name,
        url = this.url,
        isFavorite = this.isFavorite,
    )
}