package com.samchi.poke.feature.jinkwang.data.local.favorite

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
internal interface FavoriteDao {

    @Upsert
    suspend fun upsert(name: FavoriteEntity)

    @Query("DELETE FROM favorite WHERE name = :name")
    suspend fun delete(name: String)

    @Query("SELECT * FROM favorite")
    fun getFavorites(): Flow<List<FavoriteEntity>>
}