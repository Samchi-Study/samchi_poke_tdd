package com.samchi.feature.woosung.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.samchi.feature.woosung.data.entity.PokemonEntity


@Dao
interface WoosungDao {

    @Query("SELECT * FROM WSPokemon WHERE id =:id ")
    suspend fun getPokemon(id: Int): PokemonEntity

    @Insert
    suspend fun insertPokemon(entity: PokemonEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemonList(list: List<PokemonEntity>)

    @Update
    suspend fun updatePokemon(entity: PokemonEntity)

    @Delete
    suspend fun deletePokemon(entity: PokemonEntity)
}
