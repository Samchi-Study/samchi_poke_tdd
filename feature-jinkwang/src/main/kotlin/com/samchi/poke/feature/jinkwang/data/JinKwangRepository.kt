package com.samchi.poke.feature.jinkwang.data

internal interface JinKwangRepository {

    suspend fun getPockemonList(offset: Int): Result<List<Pokemon>>
}