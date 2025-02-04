package com.samchi.poke.feature.jinkwang.data

internal interface JinKwangRepository {

    suspend fun getPockemonList(
        limit: Int,
        offset: Int
    ): Result<List<Pokemon>>
}