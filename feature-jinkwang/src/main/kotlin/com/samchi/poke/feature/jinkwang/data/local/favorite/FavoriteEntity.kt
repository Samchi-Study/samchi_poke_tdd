package com.samchi.poke.feature.jinkwang.data.local.favorite

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite")
internal data class FavoriteEntity(
    val name: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)