package com.samchi.poke.kanghwi

import com.samchi.poke.kanghwi.db.entity.FavoritePokemonEntity
import com.samchi.poke.kanghwi.db.entity.PokemonEntity
import com.samchi.poke.kanghwi.model.Pokemon
import com.samchi.poke.kanghwi.model.PokemonInfo
import com.samchi.poke.network.dto.ResponsePokemon
import com.samchi.poke.network.dto.ResponsePokemonInfo


internal fun ResponsePokemonInfo.toModel() = PokemonInfo(
    count = count,
    results = results.map {
        it.toModel(
            before = this.previous,
            next = this.next
        )
    }
)

internal fun ResponsePokemon.toModel(
    before: String? = null,
    next: String? = null
) = Pokemon(
    id = 0,
    name = name,
    url = url,
    previous = before,
    next = next,
    isFavorite = false
)

internal fun ResponsePokemon.toEntity(
    previous: String? = null,
    next: String? = null
) = PokemonEntity(
    name = name,
    url = url,
    previous = previous,
    next = next,
    isFavorite = false
)

internal fun Pokemon.toEntity() = PokemonEntity(
    id = id,
    name = name,
    url = getImageUrl(),
    previous = previous,
    next = next,
    isFavorite = isFavorite
)

internal fun Pokemon.toFavoriteEntity() = FavoritePokemonEntity(
    id = id,
    isFavorite = isFavorite
)
