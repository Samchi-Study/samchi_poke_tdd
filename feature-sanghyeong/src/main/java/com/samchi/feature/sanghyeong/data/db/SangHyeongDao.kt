package com.samchi.feature.sanghyeong.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.samchi.feature.sanghyeong.data.db.entity.SangHyeongPokemonEntity

@Dao
interface SangHyeongDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemonList(entities: List<SangHyeongPokemonEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemon(entity: SangHyeongPokemonEntity)

    @Delete
    suspend fun deletePokemon(entity: SangHyeongPokemonEntity)

    @Query("DELETE FROM SangHyeongPokemonTable WHERE name = :name")
    suspend fun deletePokemonByName(name: String)

    @Query("SELECT * FROM SangHyeongPokemonTable")
    suspend fun getPokemonList(): List<SangHyeongPokemonEntity>
}