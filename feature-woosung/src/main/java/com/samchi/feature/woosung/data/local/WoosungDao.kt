package com.samchi.feature.woosung.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.samchi.feature.woosung.data.entity.PokemonEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface WoosungDao {

    @Query("SELECT * FROM WSPokemon WHERE id =:id ")
    suspend fun getPokemon(id: Int): PokemonEntity?

    @Query("SELECT * FROM WSPokemon WHERE name = :name")
    suspend fun getPokemonByName(name: String): PokemonEntity?

    @Query("SELECT * FROM WSPokemon ORDER BY id ASC")
    fun getPagingSource(): PagingSource<Int, PokemonEntity>

    @Query("SELECT * FROM WSPokemon WHERE isFavorite = 1 ORDER BY name ASC")
    fun getFavoritePokemon(): Flow<List<PokemonEntity>>

    @Query("UPDATE WSPokemon SET isFavorite = :isFavorite WHERE name = :name")
    suspend fun updateFavoriteStatus(name: String, isFavorite: Boolean)

    @Insert
    suspend fun insertPokemon(entity: PokemonEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemonList(list: List<PokemonEntity>)

    @Update
    suspend fun updatePokemon(entity: PokemonEntity)

    @Delete
    suspend fun deletePokemon(entity: PokemonEntity)
}
