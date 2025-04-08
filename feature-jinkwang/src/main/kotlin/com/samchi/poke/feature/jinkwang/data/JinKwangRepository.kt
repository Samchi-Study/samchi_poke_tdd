package com.samchi.poke.feature.jinkwang.data

internal interface JinKwangRepository {

    suspend fun getPockemonList(
        limit: Int,
        offset: Int
    ): Result<List<Pokemon>>

    suspend fun favoritePokemon(name: String)

    suspend fun unFavoritePokemon(name: String)
}