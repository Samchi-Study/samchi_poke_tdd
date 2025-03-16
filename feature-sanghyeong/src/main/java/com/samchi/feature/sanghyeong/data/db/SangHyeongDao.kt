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
    suspend fun insertPokemon(entity: SangHyeongPokemonEntity)

    @Delete
    suspend fun deletePokemon(entity: SangHyeongPokemonEntity)

    @Query("DELETE FROM SangHyeongPokemonTable WHERE id = :id")
    suspend fun deletePokemonById(id: Int)

    @Query("SELECT * FROM SangHyeongPokemonTable where id = :id")
    suspend fun getPokemon(id: Int): SangHyeongPokemonEntity

    @Query("SELECT * FROM SangHyeongPokemonTable")
    suspend fun getPokemonList(): List<SangHyeongPokemonEntity>
}