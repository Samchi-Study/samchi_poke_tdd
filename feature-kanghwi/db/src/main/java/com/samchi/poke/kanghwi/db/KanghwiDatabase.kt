package com.samchi.poke.kanghwi.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.samchi.poke.kanghwi.db.entity.PokemonEntity


@Database(entities = [PokemonEntity::class], version = 1)
abstract class KanghwiDatabase : RoomDatabase() {

    abstract fun kanghwiDao(): KanghwiDao
}
