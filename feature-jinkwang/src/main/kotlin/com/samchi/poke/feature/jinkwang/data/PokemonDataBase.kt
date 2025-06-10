package com.samchi.poke.feature.jinkwang.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.samchi.poke.feature.jinkwang.data.local.favorite.FavoriteDao
import com.samchi.poke.feature.jinkwang.data.local.favorite.FavoriteEntity
import com.samchi.poke.feature.jinkwang.data.local.pokemon.PokemonDao
import com.samchi.poke.feature.jinkwang.data.local.pokemon.PokemonEntity

@Database(
    entities = [
        PokemonEntity::class,
        FavoriteEntity::class,
    ], version = 1
)
internal abstract class PokemonDatabase : RoomDatabase() {
    abstract fun pokemonDao(): PokemonDao
    abstract fun favoriteDao(): FavoriteDao

    companion object {
        private var INSTANCE: PokemonDatabase? = null

        fun getDatabase(context: Context): PokemonDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PokemonDatabase::class.java,
                    "pokemon_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}