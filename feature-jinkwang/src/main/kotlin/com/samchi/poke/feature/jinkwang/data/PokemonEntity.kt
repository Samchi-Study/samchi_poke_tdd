package com.samchi.poke.feature.jinkwang.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemon")
internal data class PokemonEntity(
    val name: String,
    @PrimaryKey(autoGenerate = true) val id: Int = 0,    // Pokemon의 고유 ID
)