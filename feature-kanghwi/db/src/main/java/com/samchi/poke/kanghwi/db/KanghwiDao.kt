package com.samchi.poke.kanghwi.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.samchi.poke.kanghwi.db.entity.PokemonEntity


@Dao
interface KanghwiDao {

    @Query("SELECT * FROM KanghwiPokemon WHERE id =:id ")
    suspend fun getPokemon(id: Int): PokemonEntity

    @Query("SELECT * FROM KanghwiPokemon")
    suspend fun getPokemonList(): List<PokemonEntity>

    @Insert
    suspend fun insertPokemon(entity: PokemonEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemonList(list: List<PokemonEntity>)

    @Update
    suspend fun updatePokemon(entity: PokemonEntity)

    @Upsert
    suspend fun upsertPokemon(entity: PokemonEntity)

    @Delete
    suspend fun deletePokemon(entity: PokemonEntity)

}
