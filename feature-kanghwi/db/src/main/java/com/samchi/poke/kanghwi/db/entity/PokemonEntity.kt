package com.samchi.poke.kanghwi.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "KanghwiPokemon")
data class PokemonEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val name: String,
    val url: String,
    val previous: String?,
    val next: String?,
    val isFavorite: Boolean
) {

    fun getPreviousOffset() = previous?.let { url ->
        val regex = Regex("offset=\\d+")
        regex.find(url)?.value?.split("=")?.get(1)?.toInt()
    }

    fun getNextOffset() = next?.let { url ->
        val regex = Regex("offset=\\d+")
        regex.find(url)?.value?.split("=")?.get(1)?.toInt()
    }
}
