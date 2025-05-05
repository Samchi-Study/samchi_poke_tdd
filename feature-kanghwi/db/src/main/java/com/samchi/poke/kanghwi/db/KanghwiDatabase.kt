package com.samchi.poke.kanghwi.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.samchi.poke.kanghwi.db.entity.FavoritePokemonEntity
import com.samchi.poke.kanghwi.db.entity.PokemonEntity


@Database(
    entities = [
        PokemonEntity::class,
        FavoritePokemonEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class KanghwiDatabase : RoomDatabase() {

    abstract fun kanghwiDao(): KanghwiDao
}
