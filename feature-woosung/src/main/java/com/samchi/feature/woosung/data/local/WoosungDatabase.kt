package com.samchi.feature.woosung.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.samchi.feature.woosung.data.entity.PokemonEntity


@Database(entities = [PokemonEntity::class], version = 1)
abstract class WoosungDatabase  : RoomDatabase() {

    abstract fun woosungDao(): WoosungDao

}
