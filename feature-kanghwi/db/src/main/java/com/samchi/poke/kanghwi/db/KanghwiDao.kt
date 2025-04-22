package com.samchi.poke.kanghwi.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.samchi.poke.kanghwi.db.entity.PokemonEntity


@Dao
interface KanghwiDao {

    @Query("SELECT * FROM KanghwiPokemon WHERE id =:id ")
    suspend fun getPokemon(id: Int): PokemonEntity?

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

    @Query("DELETE FROM kanghwipokemon WHERE name =:name")
    suspend fun deletePokemon(name: String)

    @Query("SELECT * FROM KanghwiPokemon")
    fun getPagingSource(): PagingSource<Int, PokemonEntity>
}
