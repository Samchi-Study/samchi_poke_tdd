package com.samchi.poke.kanghwi.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "KanghwiPokemon")
data class PokemonEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    val name: String,
    val url: String,
    val isFavorite: Boolean
)
