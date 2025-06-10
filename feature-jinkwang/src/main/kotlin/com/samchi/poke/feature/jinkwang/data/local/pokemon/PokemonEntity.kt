package com.samchi.poke.feature.jinkwang.data.local.pokemon

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemon")
internal data class PokemonEntity(
    @PrimaryKey
    val name: String,
    val url: String,
    val isFavorite: Boolean,
)