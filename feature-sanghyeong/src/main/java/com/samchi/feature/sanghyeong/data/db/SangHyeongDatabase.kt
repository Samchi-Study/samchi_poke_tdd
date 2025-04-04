package com.samchi.feature.sanghyeong.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.samchi.feature.sanghyeong.data.db.entity.SangHyeongPokemonEntity

@Database(entities = [SangHyeongPokemonEntity::class], version = 1)
abstract class SangHyeongDatabase : RoomDatabase() {
    abstract fun sangHyeongDao(): SangHyeongDao
}