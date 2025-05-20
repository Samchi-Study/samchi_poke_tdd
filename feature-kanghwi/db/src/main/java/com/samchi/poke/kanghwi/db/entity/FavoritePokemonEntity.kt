package com.samchi.poke.kanghwi.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "FavoritePokemon")
data class FavoritePokemonEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val isFavorite: Boolean
)
