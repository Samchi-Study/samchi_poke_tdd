package com.samchi.feature.sanghyeong.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.samchi.feature.sanghyeong.data.db.entity.SangHyeongPokemonEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SangHyeongDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoritePokemon(entity: SangHyeongPokemonEntity)

    @Delete
    suspend fun deleteFavoritePokemon(entity: SangHyeongPokemonEntity)

    @Query("DELETE FROM SangHyeongPokemonTable WHERE name = :name")
    suspend fun deleteFavoritePokemonByName(name: String)

    @Query("SELECT * FROM SangHyeongPokemonTable")
    fun getFavoritePokemonList(): Flow<List<SangHyeongPokemonEntity>>
}