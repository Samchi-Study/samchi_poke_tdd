package com.samchi.feature.sanghyeong.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "SangHyeongPokemonTable")
data class SangHyeongPokemonEntity(
    @PrimaryKey val name: String,
    val url: String,
    val isFavorite: Boolean,
)
