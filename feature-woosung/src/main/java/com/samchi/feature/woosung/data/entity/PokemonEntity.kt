package com.samchi.feature.woosung.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "WSPokemon")
data class PokemonEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    val name: String,
    val url: String,
    val isFavorite: Boolean
)
