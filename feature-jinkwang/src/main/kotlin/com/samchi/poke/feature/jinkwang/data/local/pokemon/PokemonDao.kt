package com.samchi.poke.feature.jinkwang.data.local.pokemon

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
internal interface PokemonDao {

    @Query("SELECT * FROM pokemon")
    fun getPokemonPagingSource(): PagingSource<Int, PokemonEntity>

    // Pokemon 정보를 추가 (중복되면 덮어쓰기)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pokemon: PokemonEntity)

    @Query("UPDATE pokemon SET isFavorite = :isFavorite WHERE name = :name")
    suspend fun updateFavorite(name: String, isFavorite: Boolean)

    // 특정 Pokemon 정보를 가져오기
    @Query("SELECT * FROM pokemon WHERE name = :name")
    suspend fun getPokemonById(name: String): PokemonEntity?

    // 모든 Pokemon 정보 가져오기
    @Query("SELECT * FROM pokemon")
    suspend fun getAllPokemons(): List<PokemonEntity>

    // 즐겨찾기 상태 업데이트
    @Update
    suspend fun updatePokemon(pokemon: PokemonEntity)

    // 특정 Pokemon 삭제
    @Query("DELETE FROM pokemon WHERE name = :name")
    suspend fun deletePokemonById(name: String)

    // 모든 Pokemon 삭제
    @Query("DELETE FROM pokemon")
    suspend fun deleteAllPokemons()


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(pokemons: List<PokemonEntity>)

    @Query("DELETE FROM pokemon")
    suspend fun clearAll()
}