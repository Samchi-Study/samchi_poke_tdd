package com.samchi.poke.kanghwi

import com.samchi.poke.kanghwi.db.entity.PokemonEntity
import com.samchi.poke.model.Pokemon


fun PokemonEntity.toModel() = Pokemon(
    name = name,
    url = url,
    isFavorite = isFavorite
)
